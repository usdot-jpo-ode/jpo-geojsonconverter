package us.dot.its.jpo.geojsonconverter.converter.spat;

import us.dot.its.jpo.geojsonconverter.pojos.spat.*;

import us.dot.its.jpo.ode.model.*;
import us.dot.its.jpo.ode.plugin.j2735.J2735IntersectionState;
import us.dot.its.jpo.ode.plugin.j2735.J2735IntersectionStatusObject;
import us.dot.its.jpo.ode.plugin.j2735.J2735MovementEvent;
import us.dot.its.jpo.ode.plugin.j2735.J2735MovementState;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Calendar;

import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.kstream.Transformer;
import org.apache.kafka.streams.processor.ProcessorContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SpatProcessedJsonConverter implements Transformer<Void, OdeSpatData, KeyValue<String, ProcessedSpat>> {
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
    public KeyValue<String, ProcessedSpat> transform(Void rawKey, OdeSpatData rawValue) {
        try {
            OdeSpatMetadata spatMetadata = (OdeSpatMetadata)rawValue.getMetadata();
            OdeSpatPayload spatPayload = (OdeSpatPayload)rawValue.getPayload();
            J2735IntersectionState intersectionState = spatPayload.getSpat().getIntersectionStateList().getIntersectionStatelist().get(0);

			ProcessedSpat ProcessedSpat = createProcessedSpat(intersectionState, spatMetadata);

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

    public ProcessedSpat createProcessedSpat(J2735IntersectionState intersectionState, OdeSpatMetadata metadata) {
        ProcessedSpat processedSpat = new ProcessedSpat();
        processedSpat.setOdeReceivedAt(metadata.getOdeReceivedAt()); // ISO 8601: 2022-11-11T16:36:10.529530Z
        processedSpat.setOriginIp(metadata.getOriginIp());
        processedSpat.setName(intersectionState.getName());
        processedSpat.setRegion(intersectionState.getId().getRegion());
        processedSpat.setIntersectionId(intersectionState.getId().getId());

        List<ProcessedSpatValidationMessage> processedSpatValidationMessages = new ArrayList<ProcessedSpatValidationMessage>();
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
                    spatMovementEvent.setEventState(incomingMovementEvent.getEventState().toString());

                    TimingChangeDetails spatTimingDetails = new TimingChangeDetails();
                    spatTimingDetails.setStartTime(generateOffsetUTCTimestamp(utcTimestamp,incomingMovementEvent.getTiming().getStartTime()));
                    spatTimingDetails.setMinEndTime(generateOffsetUTCTimestamp(utcTimestamp,incomingMovementEvent.getTiming().getMinEndTime()));
                    spatTimingDetails.setMaxEndTime(generateOffsetUTCTimestamp(utcTimestamp,incomingMovementEvent.getTiming().getMaxEndTime()));
                    spatTimingDetails.setLikelyTime(generateOffsetUTCTimestamp(utcTimestamp,incomingMovementEvent.getTiming().getLikelyTime()));
                    spatTimingDetails.setConfidence(incomingMovementEvent.getTiming().getConfidence());
                    spatTimingDetails.setNextTime(generateOffsetUTCTimestamp(utcTimestamp,incomingMovementEvent.getTiming().getNextTime()));
                    
                    spatMovementEvent.setTiming(spatTimingDetails);
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
                milliseconds = moy*60*1000+dSecond*10; // milliseconds from beginning of year
                dateString = String.format("%d-01-01T00:00:00.00Z", year);
            } else {
                milliseconds = dSecond; // milliseconds from beginning of minute
                dateString = String.format("%d-%d-%dT%d:%d:00.00Z", year, odeDate.getMonthValue(), odeDate.getDayOfMonth(), odeDate.getHour(), odeDate.getMinute());
            }
                        
            // String boyString = String.format("%d-01-01T00:00:00.00Z", year);
            date = Instant.parse(dateString).plusMillis(milliseconds).atZone(ZoneId.of("UTC"));
            formatted = date.format(DateTimeFormatter.ISO_INSTANT);
        } catch (Exception e) {
            logger.error("Failed to generateUTCTimestamp - SpatProcessedJsonConverter", e);
        }
        
        return formatted;
    }

    public String generateOffsetUTCTimestamp(String originTimestamp, Integer timeMark){
        ZonedDateTime date;
        String formatted = null;

        try {
            if (timeMark != null){
                long seconds = timeMark/10;
                long nanos = (timeMark - seconds*10) * 100000000;
                date = Instant.parse(originTimestamp).atZone(ZoneId.of("UTC"));
                date = date.withMinute(0);
                date = date.withSecond(0);
                date = date.withNano(0);
                date = date.plusSeconds(seconds);
                date = date.plusNanos(nanos);
                formatted = date.format(DateTimeFormatter.ISO_INSTANT);
            }
        } catch (Exception e) {
            logger.error("Failed to generateOffsetUTCTimestamp - SpatProcessedJsonConverter", e);
        }
        
        return formatted;
    }
}
