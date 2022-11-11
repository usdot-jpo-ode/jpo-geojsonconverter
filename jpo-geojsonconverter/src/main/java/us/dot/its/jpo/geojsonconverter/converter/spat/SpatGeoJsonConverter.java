package us.dot.its.jpo.geojsonconverter.converter.spat;

import us.dot.its.jpo.geojsonconverter.pojos.geojson.spat.*;
import us.dot.its.jpo.ode.model.*;
import us.dot.its.jpo.ode.plugin.j2735.J2735IntersectionState;
import us.dot.its.jpo.ode.plugin.j2735.J2735MovementEvent;
import us.dot.its.jpo.ode.plugin.j2735.J2735MovementState;

import java.util.ArrayList;
import java.util.List;

import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.kstream.Transformer;
import org.apache.kafka.streams.processor.ProcessorContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SpatGeoJsonConverter implements Transformer<Void, OdeSpatData, KeyValue<String, SpatFeatureCollection>> {
    private static final Logger logger = LoggerFactory.getLogger(SpatGeoJsonConverter.class);

    @Override
    public void init(ProcessorContext arg0) { }

    /**
     * Transform an ODE SPaT POJO to SPaT GeoJSON POJO.
     * 
     * @param rawKey   - Void type because ODE topics have no specified key
     * @param rawValue - The raw POJO
     * @return A key value pair: the key is the RSU IP concatenated with the intersection ID and the value is the GeoJSON FeatureCollection POJO
     */
    @Override
    public KeyValue<String, SpatFeatureCollection> transform(Void rawKey, OdeSpatData rawValue) {
        try {
            OdeSpatMetadata spatMetadata = (OdeSpatMetadata)rawValue.getMetadata();
            OdeSpatPayload spatPayload = (OdeSpatPayload)rawValue.getPayload();
            J2735IntersectionState intersectionState = spatPayload.getSpat().getIntersectionStateList().getIntersectionStatelist().get(0);

			SpatFeatureCollection spatFeatureCollection = createFeatureCollection(intersectionState, spatMetadata);
            
            String id = spatMetadata.getOriginIp() + ":" + intersectionState.getId().getId();
            logger.info("Successfully created SPaT GeoJSON from " + id);
            return KeyValue.pair(id, spatFeatureCollection);
        } catch (Exception e) {
            String errMsg = String.format("Exception converting ODE SPaT to GeoJSON! Message: %s", e.getMessage());
            logger.error(errMsg, e);
            // KafkaStreams knows to remove null responses before allowing further steps from occurring
            return KeyValue.pair("ERROR", null);
        }
    }

    @Override
    public void close() {
        // Nothing to do here
    }

    public SpatFeatureCollection createFeatureCollection(J2735IntersectionState intersectionState, OdeSpatMetadata metadata) {
        Integer timestamp = intersectionState.getTimeStamp();

        List<SpatFeature> spatFeatures = new ArrayList<>();
        for (int i = 0; i < intersectionState.getStates().getMovementList().size(); i++) {
            // Create SPaT properties
            SpatProperties spatProps = new SpatProperties();
            spatProps.setIp(metadata.getOriginIp());
            spatProps.setOdeReceivedAt(metadata.getOdeReceivedAt());
            spatProps.setTimestamp(timestamp);

			J2735MovementState signalGroupState = intersectionState.getStates().getMovementList().get(i);
            spatProps.setSignalGroupId(signalGroupState.getSignalGroup());

            List<SpatMovementEvent> movementEventList = new ArrayList<>();
            if (signalGroupState.getState_time_speed() != null) {
                for (int x = 0; x < signalGroupState.getState_time_speed().getMovementEventList().size(); x++) {
                    J2735MovementEvent movementEvent = signalGroupState.getState_time_speed().getMovementEventList().get(x);
					SpatMovementEvent spatMovementEvent = new SpatMovementEvent();
                    spatMovementEvent.setEventState(movementEvent.getEventState().toString());
                    spatMovementEvent.setStartTime(movementEvent.getTiming().getStartTime());
                    spatMovementEvent.setMinEndTime(movementEvent.getTiming().getMinEndTime());
                    spatMovementEvent.setMaxEndTime(movementEvent.getTiming().getMaxEndTime());
                    movementEventList.add(spatMovementEvent);
				}
            }
            spatProps.setMovementEvents(movementEventList.toArray(new SpatMovementEvent[0]));

            // Create SPaT feature and add it to the feature list
            // Leave geometry as null for later topology join
            spatFeatures.add(new SpatFeature(spatProps.getSignalGroupId(), null, spatProps));
        }

        return new SpatFeatureCollection(spatFeatures.toArray(new SpatFeature[0]));
    }
}
