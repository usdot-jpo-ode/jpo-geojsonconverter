package us.dot.its.jpo.geojsonconverter.converter.spat;

import us.dot.its.jpo.geojsonconverter.pojos.spat.*;
import us.dot.its.jpo.geojsonconverter.validator.JsonValidatorResult;
import us.dot.its.jpo.ode.model.*;
import us.dot.its.jpo.ode.plugin.j2735.J2735IntersectionState;
import us.dot.its.jpo.ode.plugin.j2735.J2735IntersectionStatusObject;
import us.dot.its.jpo.ode.plugin.j2735.J2735MovementEvent;
import us.dot.its.jpo.ode.plugin.j2735.J2735MovementState;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.kstream.Transformer;
import org.apache.kafka.streams.processor.ProcessorContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.networknt.schema.ValidationMessage;

public class SpatProcessedJsonConverter implements Transformer<Void, DeserializedRawSpat, KeyValue<String, ProcessedSpat>> {
    private static final Logger logger = LoggerFactory.getLogger(SpatProcessedJsonConverter.class);

    @Override
    public void init(ProcessorContext arg0) { }

    /**
     * Transform an ODE SPaT POJO to Processed SPaT POJO.
     * 
     * @param rawKey   - Void type because ODE topics have no specified key
     * @param rawValue - The raw POJO
     * @return A key value pair: the key is the RSU IP concatenated with the intersection ID and the value is the GeoJSON FeatureCollection POJO
     */
    @Override
    public KeyValue<String, ProcessedSpat> transform(Void rawKey, DeserializedRawSpat rawSpat) {
        try {
            OdeSpatData rawValue = new OdeSpatData();
            rawValue.setMetadata(rawSpat.getOdeSpatOdeSpatData().getMetadata());
            rawValue.setPayload(rawSpat.getOdeSpatOdeSpatData().getPayload());
            OdeSpatMetadata spatMetadata = (OdeSpatMetadata)rawValue.getMetadata();
            OdeSpatPayload spatPayload = (OdeSpatPayload)rawValue.getPayload();
            J2735IntersectionState intersectionState = spatPayload.getSpat().getIntersectionStateList().getIntersectionStatelist().get(0);

			ProcessedSpat ProcessedSpat = createProcessedSpat(intersectionState, spatMetadata, rawSpat.getValidatorResults());

            String id = spatMetadata.getOriginIp() + ":" + intersectionState.getId().getId();
            logger.info("Successfully created Processed SPaT from " + id);
            return KeyValue.pair(id, ProcessedSpat);
        } catch (Exception e) {
            String errMsg = String.format("Exception converting ODE SPaT to Processed SPaT! Message: %s", e.getMessage());
            logger.error(errMsg, e);
            // KafkaStreams knows to remove null responses before allowing further steps from occurring
            return KeyValue.pair("ERROR", null);
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
        if (intersectionState.getId().getRegion() != null) {
            processedSpat.setRegion(intersectionState.getId().getRegion());
        } else {
            processedSpat.setRegion(0);
        }
        processedSpat.setIntersectionId(intersectionState.getId().getId());
        processedSpat.setCti4501Conformant(validationMessages.isValid());

        List<ProcessedSpatValidationMessage> processedSpatValidationMessages = new ArrayList<ProcessedSpatValidationMessage>();
        for (Exception exception : validationMessages.getExceptions()){
            ProcessedSpatValidationMessage object = new ProcessedSpatValidationMessage();
            object.setMessage("An exception was thrown.");
            object.setException(exception.getMessage());
            processedSpatValidationMessages.add(object);
        }
        for (ValidationMessage vm : validationMessages.getValidationMessages()){
            ProcessedSpatValidationMessage object = new ProcessedSpatValidationMessage();
            object.setMessage(vm.getMessage());
            object.setSchemaPath(vm.getSchemaPath());
            object.setJsonPath(vm.getPath());

            processedSpatValidationMessages.add(object);
        }
        processedSpat.setValidationMessages(processedSpatValidationMessages);

        processedSpat.setRevision(intersectionState.getRevision());

        J2735IntersectionStatusObject status = intersectionState.getStatus();
        IntersectionStatusObject intersectionStatus = new IntersectionStatusObject();
        intersectionStatus.setStatus(status);
        processedSpat.setStatus(intersectionStatus);

        Integer moyTimestamp = intersectionState.getMoy(); // Minute of the year, elapsed minutes since January in UTC time
        Integer dSecond = intersectionState.getTimeStamp(); // milliseconds within the current minute
        String utcTimestamp = generateUTCTimestamp(moyTimestamp, dSecond, metadata.getOdeReceivedAt());
        processedSpat.setUtcTimeStamp(utcTimestamp);
        if (intersectionState.getEnabledLanes()!=null) {
            processedSpat.setEnabledLanes(intersectionState.getEnabledLanes().getEnabledLaneList());
        } else {
            logger.warn("ODE Message did not contain enabled lanes object");
        }

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
                    if (intersectionState.getEnabledLanes()!=null) {
                        spatTimingDetails.setConfidence(incomingMovementEvent.getTiming().getConfidence());
                    } else {
                        spatTimingDetails.setConfidence(0);
                    }
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

    public String generateUTCTimestamp(Integer moy, Integer dSecond, String odeTimestamp){ //2022-10-31T15:40:26.687292Z
        ZonedDateTime date;
        String formatted = null;
        try {
            ZonedDateTime odeDate = Instant.parse(odeTimestamp).atZone(ZoneId.of("UTC"));
            int year = odeDate.getYear();
            String dateString;
            long milliseconds;
            if (moy != null){
                milliseconds = moy*60*1000+dSecond; // milliseconds from beginning of year
                dateString = String.format("%d-01-01T00:00:00.00Z", year);
                date = Instant.parse(dateString).plusMillis(milliseconds).atZone(ZoneId.of("UTC"));
            } else {
                date = odeDate;
                milliseconds = dSecond; // milliseconds from beginning of minute
                date = date.withSecond(0);
                date = date.withNano(0);
                date = date.plus(milliseconds, ChronoUnit.MILLIS);
            }
                        
            formatted = date.format(DateTimeFormatter.ISO_INSTANT);
        } catch (Exception e) {
            logger.error("Failed to generateUTCTimestamp - SpatProcessedJsonConverter", e);
        }
        
        return formatted;
    }

    public ZonedDateTime generateOffsetUTCTimestamp(String originTimestamp, Integer timeMark){
        try {
            if (timeMark != null){
                long millis = timeMark*100;
                ZonedDateTime date = Instant.parse(originTimestamp).atZone(ZoneId.of("UTC"));
                date = date.withMinute(0);
                date = date.withSecond(0);
                date = date.withNano(0);
                date = date.plus(millis, ChronoUnit.MILLIS);
                return date;
            } else {
                return null;
            }
        } catch (Exception e) {
            logger.error("Failed to generateOffsetUTCTimestamp - SpatProcessedJsonConverter", e);
            return null;
        }               
    }
}
