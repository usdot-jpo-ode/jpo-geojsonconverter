package us.dot.its.jpo.geojsonconverter.converter.spat;

import us.dot.its.jpo.geojsonconverter.partitioner.RsuIntersectionKey;
import us.dot.its.jpo.geojsonconverter.pojos.ProcessedValidationMessage;
import us.dot.its.jpo.geojsonconverter.pojos.spat.*;
import us.dot.its.jpo.geojsonconverter.validator.JsonValidatorResult;
import us.dot.its.jpo.ode.model.*;
import us.dot.its.jpo.ode.plugin.j2735.J2735IntersectionState;
import us.dot.its.jpo.ode.plugin.j2735.J2735IntersectionStatusObject;
import us.dot.its.jpo.ode.plugin.j2735.J2735MovementEvent;
import us.dot.its.jpo.ode.plugin.j2735.J2735MovementState;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.kstream.Transformer;
import org.apache.kafka.streams.processor.ProcessorContext;
import org.apache.commons.lang3.exception.ExceptionUtils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.networknt.schema.ValidationMessage;

public class SpatProcessedJsonConverter implements Transformer<Void, DeserializedRawSpat, KeyValue<RsuIntersectionKey, ProcessedSpat>> {
    private static final Logger logger = LoggerFactory.getLogger(SpatProcessedJsonConverter.class);

    @Override
    public void init(ProcessorContext arg0) { }

    /**
     * Transform an ODE SPaT POJO to Processed SPaT POJO.
     * 
     * @param rawKey   - Void type because ODE topics have no specified key
     * @param rawSpat - The raw POJO
     * @return A key value pair: the key an {@link RsuIntersectionKey} containing the RSU IP address and Intersection ID
     *  and the value is the GeoJSON FeatureCollection POJO
     */
    @Override
    public KeyValue<RsuIntersectionKey, ProcessedSpat> transform(Void rawKey, DeserializedRawSpat rawSpat) {
        try {
            if (!rawSpat.getValidationFailure()){
                OdeSpatData rawValue = new OdeSpatData();
                rawValue.setMetadata(rawSpat.getOdeSpatOdeSpatData().getMetadata());
                OdeSpatMetadata spatMetadata = (OdeSpatMetadata)rawValue.getMetadata();

                rawValue.setPayload(rawSpat.getOdeSpatOdeSpatData().getPayload());
                OdeSpatPayload spatPayload = (OdeSpatPayload)rawValue.getPayload();
                J2735IntersectionState intersectionState = spatPayload.getSpat().getIntersectionStateList().getIntersectionStatelist().get(0);

                ProcessedSpat processedSpat = createProcessedSpat(intersectionState, spatMetadata, rawSpat.getValidatorResults());

                var key = new RsuIntersectionKey();
                key.setRsuId(spatMetadata.getOriginIp());
                key.setIntersectionReferenceID(intersectionState.getId());
                return KeyValue.pair(key, processedSpat);
            } else {
                ProcessedSpat processedSpat = createFailureProcessedSpat(rawSpat.getValidatorResults(), rawSpat.getFailedMessage());
                var key = new RsuIntersectionKey();
                key.setRsuId("ERROR");

                return KeyValue.pair(key, processedSpat);
            }
        } catch (Exception e) {
            String errMsg = String.format("Exception converting ODE SPaT to Processed SPaT! Message: %s", e.getMessage());
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

    public ProcessedSpat createProcessedSpat(J2735IntersectionState intersectionState, OdeSpatMetadata metadata, JsonValidatorResult validationMessages) {
        ProcessedSpat processedSpat = new ProcessedSpat();
        processedSpat.setOdeReceivedAt(metadata.getOdeReceivedAt()); // ISO 8601: 2022-11-11T16:36:10.529530Z
        processedSpat.setOriginIp(metadata.getOriginIp());
        processedSpat.setName(intersectionState.getName());
        processedSpat.setRegion(intersectionState.getId().getRegion());
        processedSpat.setIntersectionId(intersectionState.getId().getId());
        processedSpat.setCti4501Conformant(validationMessages.isValid());

        List<ProcessedValidationMessage> processedSpatValidationMessages = new ArrayList<ProcessedValidationMessage>();
        for (Exception exception : validationMessages.getExceptions()){
            ProcessedValidationMessage object = new ProcessedValidationMessage();
            object.setMessage(exception.getMessage());
            object.setException(exception.getStackTrace().toString());
            processedSpatValidationMessages.add(object);
        }
        for (ValidationMessage vm : validationMessages.getValidationMessages()){
            ProcessedValidationMessage object = new ProcessedValidationMessage();
            object.setMessage(vm.getMessage());
            object.setSchemaPath(vm.getSchemaPath());
            object.setJsonPath(vm.getPath());

            processedSpatValidationMessages.add(object);
        }
        processedSpat.setValidationMessages(processedSpatValidationMessages);

        processedSpat.setRevision(intersectionState.getRevision());

        // Iterate through status hash map to pull booleans out
        IntersectionStatusObject intersectionStatus = new IntersectionStatusObject();
        for (Map.Entry<String, Boolean> set : intersectionState.getStatus().entrySet()) {
            if (set.getValue()) {
                intersectionStatus.setStatus(J2735IntersectionStatusObject.valueOf(set.getKey()));
            }
        }
        processedSpat.setStatus(intersectionStatus);

        Integer moyTimestamp = intersectionState.getMoy(); // Minute of the year, elapsed minutes since January in UTC time
        Integer dSecond = intersectionState.getTimeStamp(); // milliseconds within the current minute
        ZonedDateTime utcTimestamp = generateUTCTimestamp(moyTimestamp, dSecond, metadata.getOdeReceivedAt());
        processedSpat.setUtcTimeStamp(utcTimestamp);
        processedSpat.setEnabledLanes(intersectionState.getEnabledLanes() != null ? intersectionState.getEnabledLanes().getEnabledLaneList() : null);

        List<MovementState> movementStateList = new ArrayList<MovementState>();
        for (J2735MovementState signalGroupState : intersectionState.getStates().getMovementList()) {           
            MovementState movementState = new MovementState();
            movementState.setMovementName(signalGroupState.getMovementName());
            movementState.setSignalGroup(signalGroupState.getSignalGroup());
            
            List<MovementEvent> movementEventList = new ArrayList<MovementEvent>();
            if (signalGroupState.getState_time_speed() != null) {
                for (J2735MovementEvent incomingMovementEvent : signalGroupState.getState_time_speed().getMovementEventList()) {
					MovementEvent spatMovementEvent = new MovementEvent();
                    spatMovementEvent.setEventState(incomingMovementEvent.getEventState());

                    TimingChangeDetails spatTimingDetails = new TimingChangeDetails();
                    spatTimingDetails.setStartTime(
                        generateOffsetUTCTimestamp(utcTimestamp,incomingMovementEvent.getTiming().getStartTime()));
                    spatTimingDetails.setMinEndTime(
                        generateOffsetUTCTimestamp(utcTimestamp,incomingMovementEvent.getTiming().getMinEndTime()));
                    spatTimingDetails.setMaxEndTime(
                        generateOffsetUTCTimestamp(utcTimestamp,incomingMovementEvent.getTiming().getMaxEndTime()));
                    spatTimingDetails.setLikelyTime(
                        generateOffsetUTCTimestamp(utcTimestamp,incomingMovementEvent.getTiming().getLikelyTime()));
                    spatTimingDetails.setConfidence(incomingMovementEvent.getTiming().getConfidence());

                    spatTimingDetails.setNextTime(generateOffsetUTCTimestamp(utcTimestamp,incomingMovementEvent.getTiming().getNextTime()));
                    spatMovementEvent.setTiming(spatTimingDetails);
                    spatMovementEvent.setSpeeds(incomingMovementEvent.getSpeeds());

                    movementEventList.add(spatMovementEvent);
				}
            }
            movementState.setStateTimeSpeed(movementEventList);
            movementStateList.add(movementState);
        }

        processedSpat.setStates(movementStateList);
        return processedSpat;
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


    public ZonedDateTime generateUTCTimestamp(Integer moy, Integer dSecond, String odeTimestamp){ //2022-10-31T15:40:26.687292Z
        ZonedDateTime date = null;
        try {
            ZonedDateTime odeDate = Instant.parse(odeTimestamp).atZone(ZoneId.of("UTC"));
            int year = odeDate.getYear();
            String dateString;
            long milliseconds;
            if (moy != null){
                long minutes = moy;
                milliseconds = (long) dSecond; // milliseconds in current minute
                dateString = String.format("%d-01-01T00:00:00.00Z", year);
                date = Instant.parse(dateString).atZone(ZoneId.of("UTC"));
                date = date.plusMinutes(minutes);
                date = date.plus(milliseconds,ChronoUnit.MILLIS);
            } else {
                date = odeDate;
                milliseconds = dSecond; // milliseconds from beginning of minute
                date = date.withSecond(0);
                date = date.withNano(0);
                date = date.plus(milliseconds, ChronoUnit.MILLIS);
            }
                        
        } catch (Exception e) {
            String errMsg = String.format("Failed to generateUTCTimestamp - SpatProcessedJsonConverter. Message: %s", e.getMessage());
            logger.error(errMsg, e);
        }
        
        return date;
    }

    public ZonedDateTime generateOffsetUTCTimestamp(ZonedDateTime originTimestamp, Integer timeMark){
        try {
            if (timeMark != null){
                long millis = Long.valueOf(timeMark)*100;
                ZonedDateTime date = originTimestamp;
                if(timeMark == 36011 || timeMark == 36001){

                    // Return UTC time zero if the Zoned Date time is marked as unknown, UTC time zero chosen so that a null value can represent an empty field in the SPaT. But 36011, can represent an intentionally unidentified field.
                    return ZonedDateTime.ofInstant(Instant.ofEpochMilli(0), ZoneId.of("UTC"));
                    
                }else{
                    // If we are within 10 minutes of the next hour, and the timeMark is a small number, it probably means that the time is rolling over.
                    // In this case, add an hour to the UTC timestamp so that it appears in the future instead of in the past.s
                    if(originTimestamp.getMinute() > 50 && timeMark < 6000){
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
            String errMsg = String.format("Failed to generateOffsetUTCTimestamp - SpatProcessedJsonConverter. Message: %s", e.getMessage());
            logger.error(errMsg, e);
            return null;
        }               
    }
}
