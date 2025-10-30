package us.dot.its.jpo.geojsonconverter.converter.spat;

import us.dot.its.jpo.asn.j2735.r2024.Common.IntersectionReferenceID;
import us.dot.its.jpo.asn.j2735.r2024.Common.SpeedConfidence;
import us.dot.its.jpo.asn.j2735.r2024.SPAT.*;
import us.dot.its.jpo.geojsonconverter.partitioner.RsuIntersectionKey;
import us.dot.its.jpo.geojsonconverter.pojos.ProcessedValidationMessage;
import us.dot.its.jpo.geojsonconverter.pojos.common.ProcessedIntersectionReferenceID;
import us.dot.its.jpo.geojsonconverter.pojos.common.ProcessedSpeedConfidence;
import us.dot.its.jpo.geojsonconverter.pojos.spat.*;
import us.dot.its.jpo.geojsonconverter.utils.BitstringUtils;
import us.dot.its.jpo.geojsonconverter.utils.ProcessedSchemaVersions;
import us.dot.its.jpo.geojsonconverter.validator.CTI4501Validator;
import us.dot.its.jpo.geojsonconverter.validator.JsonValidatorResult;
import us.dot.its.jpo.ode.model.OdeMessageFrameData;
import us.dot.its.jpo.ode.model.OdeMessageFrameMetadata;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.kstream.Transformer;
import org.apache.kafka.streams.processor.ProcessorContext;
import org.apache.commons.lang3.exception.ExceptionUtils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.networknt.schema.ValidationMessage;

public class SpatProcessedJsonConverter
        implements Transformer<Void, DeserializedRawSpat, KeyValue<RsuIntersectionKey, ProcessedSpat>> {
    private static final Logger logger = LoggerFactory.getLogger(SpatProcessedJsonConverter.class);

    @Override
    public void init(ProcessorContext arg0) {}

    /**
     * Transform an ODE SPaT POJO to Processed SPaT POJO.
     *
     * @param rawKey - Void type because ODE topics have no specified key
     * @param rawSpat - The raw POJO
     * @return A key value pair: the key an {@link RsuIntersectionKey} containing the RSU IP address and Intersection ID
     *         and the value is the GeoJSON FeatureCollection POJO
     */
    @Override
    public KeyValue<RsuIntersectionKey, ProcessedSpat> transform(Void rawKey, DeserializedRawSpat rawSpat) {
        try {
            if (!rawSpat.isValidationFailure()) {
                OdeMessageFrameData rawValue = new OdeMessageFrameData();
                rawValue.setMetadata(rawSpat.getOdeSpatMessageFrameData().getMetadata());
                OdeMessageFrameMetadata spatMetadata = rawValue.getMetadata();

                rawValue.setPayload(rawSpat.getOdeSpatMessageFrameData().getPayload());
                SPATMessageFrame spatMessageFrame = (SPATMessageFrame) rawValue.getPayload().getData();

                ProcessedSpat processedSpat =
                        createProcessedSpat(spatMessageFrame.getValue(), spatMetadata, rawSpat.getValidatorResults());

                // Set the schema version
                processedSpat.setSchemaVersion(ProcessedSchemaVersions.PROCESSED_SPAT_SCHEMA_VERSION);

                var key = new RsuIntersectionKey();
                key.setRsuId(spatMetadata.getOriginIp());
                key.setIntersectionReferenceID(spatMessageFrame.getValue().getIntersections().get(0).getId());
                return KeyValue.pair(key, processedSpat);
            } else {
                ProcessedSpat processedSpat =
                        createFailureProcessedSpat(rawSpat.getValidatorResults(), rawSpat.getFailedMessage());
                var key = new RsuIntersectionKey();
                key.setRsuId("ERROR");

                return KeyValue.pair(key, processedSpat);
            }
        } catch (Exception e) {
            String errMsg =
                    String.format("Exception converting ODE SPaT to Processed SPaT! Message: %s", e.getMessage());
            logger.error(errMsg, e);
            // KafkaStreams knows to remove null responses before allowing further steps from occurring
            var key = new RsuIntersectionKey();
            key.setRsuId("ERROR");
            return KeyValue.pair(key, null);
        }
    }

    @Override
    public void close() {
        // Nothing to do here
    }

    public ProcessedSpat createProcessedSpat(SPAT spat, OdeMessageFrameMetadata metadata,
            JsonValidatorResult validationMessages) {
        // Create an IntersectionState from the SPAT for easier readability
        IntersectionState intersectionState = spat.getIntersections().get(0);

        // Build the ProcessedSpat object representing the intersection state
        ProcessedSpat processedSpat = new ProcessedSpat();
        processedSpat.setOdeReceivedAt(metadata.getOdeReceivedAt()); // ISO 8601: 2022-11-11T16:36:10.529530Z
        processedSpat.setOriginIp(metadata.getOriginIp());
        processedSpat.setAsn1(metadata.getAsn1());
        processedSpat.setName(intersectionState.getName() != null ? intersectionState.getName().getValue() : null);
        IntersectionReferenceID intersectionReferenceID = intersectionState.getId();
        ProcessedIntersectionReferenceID processedIntersectionReferenceID = new ProcessedIntersectionReferenceID();
        processedIntersectionReferenceID.setId(
                intersectionReferenceID.getId() != null ? (int) intersectionReferenceID.getId().getValue() : null);
        processedIntersectionReferenceID.setRegion(
                intersectionReferenceID.getRegion() != null ? (int) intersectionReferenceID.getRegion().getValue()
                        : null);
        processedSpat.setIntersectionReferenceID(processedIntersectionReferenceID);

        // Handle validation messages for the J2735 and CTI-4501 SPaT conformance validation
        List<ProcessedValidationMessage> processedSpatValidationMessages = new ArrayList<ProcessedValidationMessage>();
        for (Exception exception : validationMessages.getExceptions()) {
            ProcessedValidationMessage object = new ProcessedValidationMessage();
            object.setMessage(exception.getMessage());
            object.setException(exception.getStackTrace().toString());
            processedSpatValidationMessages.add(object);
        }
        for (ValidationMessage vm : validationMessages.getValidationMessages()) {
            ProcessedValidationMessage object = new ProcessedValidationMessage();
            object.setMessage(vm.getMessage());
            object.setSchemaPath(vm.getSchemaPath());
            object.setJsonPath(vm.getPath());

            processedSpatValidationMessages.add(object);
        }
        processedSpatValidationMessages.addAll(CTI4501Validator.spatValidation(spat));
        processedSpat.setValidationMessages(processedSpatValidationMessages);
        processedSpat.setCti4501Conformant(processedSpat.getValidationMessages().size() == 0);

        processedSpat.setRevision(
                intersectionState.getRevision() != null ? (int) intersectionState.getRevision().getValue() : null);
        ProcessedIntersectionStatusObject processedStatus = new ProcessedIntersectionStatusObject();
        BitstringUtils.processBitstring(processedStatus, intersectionState.getStatus());
        processedSpat.setStatus(processedStatus);
        List<Integer> enabledLanes = new ArrayList<>();
        if (intersectionState.getEnabledLanes() != null) {
            enabledLanes.addAll(
                    intersectionState.getEnabledLanes().stream().map(laneId -> (int) laneId.getValue()).toList());
        }
        processedSpat.setEnabledLanes(enabledLanes);

        // Retrieve all relevant timestamp-based fields to calculate the UTC timestamp
        Long spatMoy = spat.getTimeStamp() != null ? spat.getTimeStamp().getValue() : null;
        Long intersectionMoy = intersectionState.getMoy() != null ? intersectionState.getMoy().getValue() : null;
        Long intersectionDSecond =
                intersectionState.getTimeStamp() != null ? intersectionState.getTimeStamp().getValue() : null;

        // Generate the UTC timestamp based on the moy that isn't null and let the function handle the rest
        // If both are null or intersectionDSecond is null, the function will still use the ODE received timestamp to
        // fill in the blanks
        ZonedDateTime utcTimestamp = intersectionMoy != null
                ? generateUTCTimestamp(intersectionMoy, intersectionDSecond, metadata.getOdeReceivedAt())
                : generateUTCTimestamp(spatMoy, intersectionDSecond, metadata.getOdeReceivedAt());
        processedSpat.setUtcTimeStamp(utcTimestamp);

        List<ProcessedMovementState> processedMovementStateList = new ArrayList<ProcessedMovementState>();
        for (MovementState signalGroupState : intersectionState.getStates()) {
            ProcessedMovementState processedMovementState = new ProcessedMovementState();
            processedMovementState.setMovementName(
                    signalGroupState.getMovementName() != null ? signalGroupState.getMovementName().getValue() : null);
            processedMovementState.setSignalGroup(
                    signalGroupState.getSignalGroup() != null ? (int) signalGroupState.getSignalGroup().getValue()
                            : null);

            List<ProcessedMovementEvent> processedMovementEventList = new ArrayList<ProcessedMovementEvent>();
            if (signalGroupState.getState_time_speed() != null) {
                for (MovementEvent incomingMovementEvent : signalGroupState.getState_time_speed()) {
                    ProcessedMovementEvent processedMovementEvent = new ProcessedMovementEvent();
                    MovementPhaseState phaseState = incomingMovementEvent.getEventState();
                    if (phaseState != null) {
                        ProcessedMovementPhaseState processedMovementPhaseState =
                                ProcessedMovementPhaseState.fromName(phaseState.getName());
                        processedMovementEvent.setEventState(processedMovementPhaseState);
                    }


                    // Calculate the UTC timestamps for the movement event states and account for null values
                    TimingChangeDetails spatTimingDetails = new TimingChangeDetails();
                    Integer startTime = incomingMovementEvent.getTiming().getStartTime() != null
                            ? (int) incomingMovementEvent.getTiming().getStartTime().getValue()
                            : null;
                    Integer minEndTime = incomingMovementEvent.getTiming().getMinEndTime() != null
                            ? (int) incomingMovementEvent.getTiming().getMinEndTime().getValue()
                            : null;
                    Integer maxEndTime = incomingMovementEvent.getTiming().getMaxEndTime() != null
                            ? (int) incomingMovementEvent.getTiming().getMaxEndTime().getValue()
                            : null;
                    Integer likelyTime = incomingMovementEvent.getTiming().getLikelyTime() != null
                            ? (int) incomingMovementEvent.getTiming().getLikelyTime().getValue()
                            : null;
                    Integer nextTime = incomingMovementEvent.getTiming().getNextTime() != null
                            ? (int) incomingMovementEvent.getTiming().getNextTime().getValue()
                            : null;
                    spatTimingDetails.setStartTime(generateOffsetUTCTimestamp(utcTimestamp, startTime));
                    spatTimingDetails.setMinEndTime(generateOffsetUTCTimestamp(utcTimestamp, minEndTime));
                    spatTimingDetails.setMaxEndTime(generateOffsetUTCTimestamp(utcTimestamp, maxEndTime));
                    spatTimingDetails.setLikelyTime(generateOffsetUTCTimestamp(utcTimestamp, likelyTime));
                    spatTimingDetails.setNextTime(generateOffsetUTCTimestamp(utcTimestamp, nextTime));
                    spatTimingDetails.setConfidence(incomingMovementEvent.getTiming().getConfidence() != null
                            ? (int) incomingMovementEvent.getTiming().getConfidence().getValue()
                            : null);
                    processedMovementEvent.setTiming(spatTimingDetails);

                    // Set the speeds if they exist, otherwise set to null
                    processedMovementEvent.setSpeeds(convertAdvisorySpeedList(incomingMovementEvent.getSpeeds()));

                    processedMovementEventList.add(processedMovementEvent);
                }
            }
            processedMovementState.setStateTimeSpeed(processedMovementEventList);
            processedMovementStateList.add(processedMovementState);
        }

        processedSpat.setStates(processedMovementStateList);
        return processedSpat;
    }

    private ProcessedAdvisorySpeedList convertAdvisorySpeedList(AdvisorySpeedList advisorySpeedList) {
        if (advisorySpeedList != null) {
            ProcessedAdvisorySpeedList processedAdvisorySpeedList = new ProcessedAdvisorySpeedList();
            for (AdvisorySpeed advisorySpeed : advisorySpeedList) {
                ProcessedAdvisorySpeed processedAdvisorySpeed = new ProcessedAdvisorySpeed();

                Integer speed = advisorySpeed.getSpeed() != null ? (int) advisorySpeed.getSpeed().getValue() : null;
                processedAdvisorySpeed.setSpeed(speed);

                Integer class_ = advisorySpeed.getClass_() != null ? (int) advisorySpeed.getClass_().getValue() : null;
                processedAdvisorySpeed.setClass_(class_);

                Integer distance =
                        advisorySpeed.getDistance() != null ? (int) advisorySpeed.getDistance().getValue() : null;
                processedAdvisorySpeed.setDistance(distance);

                AdvisorySpeedType advisorySpeedType = advisorySpeed.getType();
                if (advisorySpeedType != null) {
                    processedAdvisorySpeed.setType(ProcessedAdvisorySpeedType.fromName(advisorySpeedType.getName()));
                }

                SpeedConfidence speedConfidence = advisorySpeed.getConfidence();
                if (speedConfidence != null) {
                    processedAdvisorySpeed.setConfidence(ProcessedSpeedConfidence.fromName(speedConfidence.getName()));
                }

                processedAdvisorySpeedList.add(processedAdvisorySpeed);
            }
            return processedAdvisorySpeedList;
        }
        return null;
    }

    public ProcessedSpat createFailureProcessedSpat(JsonValidatorResult validatorResult, String message) {
        ProcessedSpat processedSpat = new ProcessedSpat();
        ProcessedValidationMessage object = new ProcessedValidationMessage();
        List<ProcessedValidationMessage> processedSpatValidationMessages = new ArrayList<ProcessedValidationMessage>();

        ZonedDateTime utcDateTime = ZonedDateTime.now(ZoneOffset.UTC);

        object.setMessage(message);
        object.setException(ExceptionUtils.getStackTrace(validatorResult.getExceptions().get(0)));

        processedSpatValidationMessages.add(object);
        processedSpat.setValidationMessages(processedSpatValidationMessages);
        processedSpat.setUtcTimeStamp(utcDateTime);

        return processedSpat;
    }

    public ZonedDateTime generateUTCTimestamp(Long moy, Long dSecond, String odeTimestamp) { //
        // 2022-10-31T15:40:26.687292Z
        ZonedDateTime date = null;
        try {
            ZonedDateTime odeDate = Instant.parse(odeTimestamp).atZone(ZoneId.of("UTC"));
            int year = odeDate.getYear();
            String dateString;
            long milliseconds;
            if (moy != null && dSecond != null) {
                dateString = String.format("%d-01-01T00:00:00.00Z", year);
                date = Instant.parse(dateString).atZone(ZoneId.of("UTC"));
                date = date.plusMinutes(moy);
                date = date.plus(dSecond, ChronoUnit.MILLIS);
            } else {
                date = odeDate;
                if (dSecond != null) {
                    milliseconds = dSecond; // milliseconds from beginning of minute
                    date = date.withSecond(0);
                    date = date.withNano(0);
                    date = date.plus(milliseconds, ChronoUnit.MILLIS);
                }
            }


        } catch (Exception e) {
            String errMsg = String.format("Failed to generateUTCTimestamp - SpatProcessedJsonConverter. Message: %s",
                    e.getMessage());
            logger.error(errMsg, e);
        }

        return date;
    }

    public ZonedDateTime generateOffsetUTCTimestamp(ZonedDateTime originTimestamp, Integer timeMark) {
        try {
            if (timeMark != null) {
                long millis = Long.valueOf(timeMark) * 100;
                ZonedDateTime date = originTimestamp;
                if (timeMark == 36011 || timeMark == 36001) {

                    // Return UTC time zero if the Zoned Date time is marked as unknown, UTC time zero chosen so that a
                    // null value can represent an empty field in the SPaT. But 36011, can represent an intentionally
                    // unidentified field.
                    return ZonedDateTime.ofInstant(Instant.ofEpochMilli(0), ZoneId.of("UTC"));

                } else {
                    // If we are within 10 minutes of the next hour, and the timeMark is a small number, it probably
                    // means that the time is rolling over.
                    // In this case, add an hour to the UTC timestamp so that it appears in the future instead of in the
                    // past.s
                    if (originTimestamp.getMinute() > 50 && timeMark < 6000) {
                        date = date.plusHours(1);
                    }

                    date = date.withMinute(0);
                    date = date.withSecond(0);
                    date = date.withNano(0);
                    date = date.plus(millis, ChronoUnit.MILLIS);
                    return date;
                }


            } else {
                return null;
            }
        } catch (Exception e) {
            String errMsg = String.format(
                    "Failed to generateOffsetUTCTimestamp - SpatProcessedJsonConverter. Message: %s", e.getMessage());
            logger.error(errMsg, e);
            return null;
        }
    }
}
