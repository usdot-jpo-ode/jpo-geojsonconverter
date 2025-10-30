package us.dot.its.jpo.geojsonconverter.converter.srm;

import com.networknt.schema.ValidationMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import us.dot.its.jpo.asn.j2735.r2024.Common.*;
import us.dot.its.jpo.asn.j2735.r2024.SignalRequestMessage.*;
import us.dot.its.jpo.geojsonconverter.pojos.ProcessedValidationMessage;
import us.dot.its.jpo.geojsonconverter.pojos.common.ProcessedTransmissionState;
import us.dot.its.jpo.geojsonconverter.pojos.geojson.Point;
import us.dot.its.jpo.geojsonconverter.pojos.geojson.srm.*;
import us.dot.its.jpo.geojsonconverter.utils.BitstringUtils;
import us.dot.its.jpo.geojsonconverter.validator.JsonValidatorResult;

import java.time.Duration;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static us.dot.its.jpo.geojsonconverter.converter.FieldConversions.*;

@Component
@Slf4j
public class SrmConverter {

    public ProcessedSrm processSrm(final SignalRequestMessageMessageFrame ssmFrame) {
        // We don't know what year it is; pass in the current time as a basis to guess the year.
        var now = ZonedDateTime.now();
        return processSrm(ssmFrame, now);
    }

    public ProcessedSrm processSrm(final SignalRequestMessageMessageFrame srmFrame, final ZonedDateTime ingestTime) {
        var list = new ArrayList<ProcessedSrm>();

        if (srmFrame == null) {
            log.error("MessageFrame is null");
            return null;
        }

        SignalRequestMessage srm = srmFrame.getValue();

        if (srm == null) {
            log.error("SignalRequestMessage is null");
            return null;
        }

        final Integer sequenceNumber = convertMsgCount(srm.getSequenceNumber());
        MinuteOfTheYear moy = srm.getTimeStamp();
        DSecond dsec = srm.getSecond();
        final ZonedDateTime timeStamp = convertMinuteOfYearAndDSecond(moy, ingestTime, dsec);
        final int year = timeStamp != null ? timeStamp.getYear() : ZonedDateTime.now(ZoneOffset.UTC).getYear();

        RequestorDescription requestor = srm.getRequestor();
        var props = new SrmProperties();
        processRequestorDescription(requestor, props);
        props.setTimeStamp(timeStamp);
        props.setSequenceNumber(sequenceNumber);

        SignalRequestList requests = srm.getRequests();
        List<ProcessedSignalRequest> processedRequests = new ArrayList<>();
        for (SignalRequestPackage pkg : requests) {
            var processedSignalRequest = new ProcessedSignalRequest();
            processSignalRequestPackage(pkg, processedSignalRequest, year);
            processedRequests.add(processedSignalRequest);
        }
        props.setRequests(processedRequests);

        Point geometry;
        if (props.getLongitude() != null && props.getLatitude() != null) {
            geometry = new Point(props.getLongitude(), props.getLatitude());
        } else {
            geometry = null;
        }
        var processed = new ProcessedSrm(geometry, props);
        list.add(processed);

        return processed;
    }

    private void processRequestorDescription(final RequestorDescription requestor, SrmProperties props) {
        if (requestor == null) { return; }
        RequestorPositionVector vector = requestor.getPosition();
        processRequestorPositionVector(vector, props);

        VehicleID id = requestor.getId();
        props.setVehicleID(convertVehicleID(id));

        DescriptiveName name = requestor.getName();
        if (name != null) {
            props.setName(name.getValue());
        }

        DescriptiveName routeName = requestor.getRouteName();
        if (routeName != null) {
            props.setRouteName(routeName.getValue());
        }

        TransitVehicleOccupancy occupancy = requestor.getTransitOccupancy();
        if (occupancy != null) {
            props.setTransitOccupancy(ProcessedTransitVehicleOccupancy.fromName(occupancy.getName()));
        }

        props.setTransitScheduleSeconds(convertDeltaTime(requestor.getTransitSchedule()));

        TransitVehicleStatus status = requestor.getTransitStatus();
        ProcessedTransitVehicleStatus processedStatus = new ProcessedTransitVehicleStatus();
        BitstringUtils.processBitstring(processedStatus, status);

        RequestorType type = requestor.getType();
        processRequestorType(type, props);
    }

    private void processRequestorPositionVector(RequestorPositionVector vector, SrmProperties props) {
        if (vector != null) {
            if (vector.getHeading() != null) {
                props.setHeading(convertHeading(vector.getHeading().getValue()));
            }

            TransmissionAndSpeed tSpeed = vector.getSpeed();
            if (tSpeed != null) {
                Velocity speed = tSpeed.getSpeed();
                if (speed != null) {
                    props.setSpeedMetersPerSecond(convertSpeed(speed.getValue()));
                }
                TransmissionState transmission = tSpeed.getTransmisson();
                if (transmission != null) {
                    props.setTransmission(ProcessedTransmissionState.fromName(transmission.getName()));
                }
            }

            Position3D position = vector.getPosition();
            if (position != null) {
                if (position.getLong_() != null) {
                    props.setLongitude(convertLong(position.getLong_().getValue()));
                }
                if (position.getLat() != null) {
                    props.setLatitude(convertLat(position.getLat().getValue()));
                }
                if (position.getElevation() != null) {
                    props.setElevation(convertElevation(position.getElevation().getValue()));
                }
            }
        }
    }

    private void processSignalRequestPackage(final SignalRequestPackage pkg, final ProcessedSignalRequest processed, final int year) {
        if (pkg == null) { return; }
        processSignalRequest(pkg.getRequest(), processed);
        processETA(pkg, processed, year);
    }

    private void processSignalRequest(final SignalRequest request, final ProcessedSignalRequest processed) {
        if (request == null) return;

        RegionIntersectionId id = convertIntersectionReferenceID(request.getId());
        processed.setRegion(id.region());
        processed.setIntersectionId(id.intersectionId());

        RequestID requestId = request.getRequestID();
        if (requestId != null) {
            processed.setRequestID((int)requestId.getValue());
        }

        PriorityRequestType requestType = request.getRequestType();
        if (requestType != null) {
            processed.setPriorityRequestType(ProcessedPriorityRequestType.fromName(requestType.getName()));
        }

        AccessPointID inbound = convertIntersectionAccessPointID(request.getInBoundLane());
        processed.setInboundLaneID(inbound.laneID());
        processed.setInboundApproachID(inbound.approachID());
        processed.setInboundLaneConnectionID(inbound.connectionID());

        AccessPointID outbound = convertIntersectionAccessPointID(request.getOutBoundLane());
        processed.setOutboundLaneID(outbound.laneID());
        processed.setOutboundApproachID(outbound.approachID());
        processed.setOutboundLaneConnectionID(outbound.connectionID());
    }

    private void processETA(final SignalRequestPackage pkg, final ProcessedSignalRequest processed, final int year) {
        ZonedDateTime ts = convertMinuteOfYearAndDSecond( pkg.getMinute(), year, pkg.getSecond());
        processed.setEstimatedTimeOfArrival(ts);
        if (pkg.getDuration() != null) {
            Duration duration = Duration.ofMillis(pkg.getDuration().getValue());
            processed.setEstimatedTimeOfArrivalDurationSeconds(duration);
        }
    }

    private void processRequestorType(RequestorType requestorType, SrmProperties props) {
        if (requestorType == null) return;
        props.setRole(convertBasicVehicleRole(requestorType.getRole()));
        props.setSubrole(convertRequestSubRole(requestorType.getSubrole()));
        props.setHpmsType(convertVehicleType(requestorType.getHpmsType()));
        Iso3833VehicleType iso = requestorType.getIso3883();
        if (iso != null) {
            props.setIso3833VehicleType((int)iso.getValue());
        }
        props.setImportanceLevel(convertRequestImportanceLevel(requestorType.getRequest()));
    }

    /**
     * Add JSON schema validation results for J2735 and Metadata validation.
     * @param properties The properties to add validation messages to
     * @param validatorResult the schema validator result
     */
    public void jsonValidation(SrmProperties properties, JsonValidatorResult validatorResult) {
        var messages = new ArrayList<ProcessedValidationMessage>();
        for (Exception exception : validatorResult.getExceptions()) {
            var msg = new ProcessedValidationMessage();
            msg.setMessage(exception.getMessage());
            msg.setException(Arrays.toString(exception.getStackTrace()));
            messages.add(msg);
        }
        for (ValidationMessage vm : validatorResult.getValidationMessages()) {
            var msg = new ProcessedValidationMessage();
            msg.setMessage(vm.getMessage());
            msg.setSchemaPath(vm.getSchemaPath());
            msg.setJsonPath(vm.getPath());
            messages.add(msg);
        }
        properties.addValidationMessages(messages);
    }

}
