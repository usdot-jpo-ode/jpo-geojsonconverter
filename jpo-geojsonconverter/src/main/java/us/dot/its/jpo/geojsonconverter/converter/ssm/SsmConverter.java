package us.dot.its.jpo.geojsonconverter.converter.ssm;

import com.networknt.schema.ValidationMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import us.dot.its.jpo.asn.j2735.r2024.Common.*;
import us.dot.its.jpo.asn.j2735.r2024.SignalStatusMessage.*;
import us.dot.its.jpo.geojsonconverter.pojos.ProcessedValidationMessage;
import us.dot.its.jpo.geojsonconverter.pojos.common.ProcessedPrioritizationResponseStatus;
import us.dot.its.jpo.geojsonconverter.pojos.ssm.ProcessedSignalStatus;
import us.dot.its.jpo.geojsonconverter.pojos.ssm.ProcessedSsm;
import us.dot.its.jpo.geojsonconverter.validator.JsonValidatorResult;

import java.time.Duration;
import java.time.Instant;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static us.dot.its.jpo.geojsonconverter.converter.FieldConversions.*;

@Component
@Slf4j
public class SsmConverter {



    public ProcessedSsm processSsm(final SignalStatusMessageMessageFrame ssmFrame) {
        // We don't know what year it is; pass in the current time as a basis to guess the year.
        var now = ZonedDateTime.now();
        return processSsm(ssmFrame, now);
    }

    /**
     * Process an SSM MessageFrame
     * @param ssmFrame SSM Message Frame
     * @param ingestTime The message ingest time to use to guess the year
     * @return the ProcessedSsm
     */
    public ProcessedSsm processSsm(final SignalStatusMessageMessageFrame ssmFrame, final ZonedDateTime ingestTime) {
        if (ssmFrame == null) {
            log.error("SSM Message Frame is null");
            return null;
        }

        SignalStatusMessage ssm = ssmFrame.getValue();

        if (ssm == null) {
            log.error("SignalStatusMessage is null");
            return null;
        }

        ProcessedSsm processedSsm = new ProcessedSsm();

        final Integer sequenceNumber = convertMsgCount(ssm.getSequenceNumber());
        processedSsm.setSequenceNumber(sequenceNumber);

        MinuteOfTheYear moy = ssm.getTimeStamp();
        DSecond dsec = ssm.getSecond();

        final ZonedDateTime timeStamp = convertMinuteOfYearAndDSecond(moy, ingestTime, dsec);
        final int year = timeStamp != null ? timeStamp.getYear() : ZonedDateTime.now(ZoneOffset.UTC).getYear();
        processedSsm.setTimeStamp(timeStamp);

        SignalStatusList sslist = ssm.getStatus();
        if (sslist == null) {
            log.error("SignalStatusList is null");
            return processedSsm;
        }

        if (sslist.size() > 0) {
            SignalStatus signalStatus = sslist.getFirst();
            final Integer statusSequenceNumber = convertMsgCount(signalStatus.getSequenceNumber());
            final RegionIntersectionId regInt = convertIntersectionReferenceID(signalStatus.getId());

            processedSsm.setStatusSequenceNumber(statusSequenceNumber);
            processedSsm.setRegion(regInt.region());
            processedSsm.setIntersectionId(regInt.intersectionId());

            SignalStatusPackageList packageList = signalStatus.getSigStatus();
            if (packageList == null) {
                log.error("SignalStatusPackageList is null");
                return processedSsm;
            }

            List<ProcessedSignalStatus> processedSignalStatusList = new ArrayList<>();
            for (SignalStatusPackage pkg : packageList) {
                var processed = new ProcessedSignalStatus();
                processSignalStatusPackage(pkg, processed, year);
                processedSignalStatusList.add(processed);
            }
            processedSsm.setStatusList(processedSignalStatusList);

            // Warn if more than one intersection in SSM
            if (sslist.size() > 1) {
                log.warn("There is more than one intersection in the SSM.  This is unsupported.  The ProcessedSsm only " +
                        "contains the first intersection: {}", ssmFrame);
            }
        } else {
            // Abnormal case: no intersections
            log.error("The SignalStatusList is empty");
        }

        return processedSsm;
    }


    private void processSignalStatusPackage(final SignalStatusPackage pkg, final ProcessedSignalStatus processed, final int year) {
        if (pkg == null) { return; }
        SignalRequesterInfo requester = pkg.getRequester();
        processRequester(requester, processed);

        AccessPointID inbound = convertIntersectionAccessPointID(pkg.getInboundOn());
        processed.setInboundOnLaneID(inbound.laneID());
        processed.setInboundOnApproachID(inbound.approachID());
        processed.setInboundOnLaneConnectionID(inbound.connectionID());

        AccessPointID outbound = convertIntersectionAccessPointID(pkg.getOutboundOn());
        processed.setOutboundOnLaneID(outbound.laneID());
        processed.setOutboundOnApproachID(outbound.approachID());
        processed.setOutboundOnLaneConnectionID(outbound.connectionID());

        PrioritizationResponseStatus status = pkg.getStatus();
        if (status != null) {
            ProcessedPrioritizationResponseStatus processedStatus
                    = ProcessedPrioritizationResponseStatus.fromName(status.getName());
            processed.setStatus(processedStatus);
        }

        // ETA
        processETA(pkg, processed, year);

    }

    private void processRequester(final SignalRequesterInfo requester, final ProcessedSignalStatus processed) {
        if (requester == null) { return; }
        VehicleID vehicleId = requester.getId();
        processed.setVehicleID(convertVehicleID(vehicleId));

        MsgCount requestSequenceNumber = requester.getSequenceNumber();
        processed.setRequesterSequenceNumber(convertMsgCount(requestSequenceNumber));

        RequestID requestId = requester.getRequest();
        if (requestId != null) {
            processed.setRequestID((int)requestId.getValue());
        }

        processed.setRequesterRole(convertBasicVehicleRole(requester.getRole()));

        RequestorType requestorType = requester.getTypeData();
        processRequestorType(requestorType, processed);
    }


    private void processETA(final SignalStatusPackage pkg, final ProcessedSignalStatus processed, final int year) {
        if (pkg == null) { return; }
        ZonedDateTime ts = convertMinuteOfYearAndDSecond(pkg.getMinute(), year, pkg.getSecond());
        processed.setEstimatedTimeOfArrival(ts);
        if (pkg.getDuration() != null) {
            Duration duration = Duration.ofMillis(pkg.getDuration().getValue());
            processed.setEstimatedTimeOfArrivalDurationSeconds(duration);
        }
    }

    private void processRequestorType(final RequestorType requestorType, final ProcessedSignalStatus processed) {
        // Use this role if top-level role is missing
        if (requestorType == null) { return; }
        if (processed.getRequesterRole() == null) {
            processed.setRequesterRole(convertBasicVehicleRole(requestorType.getRole()));
        }
        processed.setRequesterSubrole(convertRequestSubRole(requestorType.getSubrole()));
        processed.setRequesterHpmsType(convertVehicleType(requestorType.getHpmsType()));
        Iso3833VehicleType iso = requestorType.getIso3883();
        if (iso != null) {
            processed.setRequesterIso3833VehicleType((int)iso.getValue());
        }
        processed.setRequestImportanceLevel(convertRequestImportanceLevel(requestorType.getRequest()));
    }

    /**
     * Add JSON schema validation results for J2735 and Metadata validation.
     * @param properties The properties to add validation messages to
     * @param validatorResult the schema validator result
     */
    public void jsonValidation(ProcessedSsm properties, JsonValidatorResult validatorResult) {
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
