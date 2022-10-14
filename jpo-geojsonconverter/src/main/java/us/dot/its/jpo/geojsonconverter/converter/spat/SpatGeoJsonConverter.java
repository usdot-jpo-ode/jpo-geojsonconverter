package us.dot.its.jpo.geojsonconverter.converter.spat;

import us.dot.its.jpo.geojsonconverter.geojson.Point;
import us.dot.its.jpo.geojsonconverter.geojson.spat.*;
import us.dot.its.jpo.ode.model.*;

import java.util.ArrayList;
import java.util.List;
import java.math.BigDecimal;

import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.kstream.Transformer;
import org.apache.kafka.streams.processor.ProcessorContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SpatGeoJsonConverter implements Transformer<Void, OdeSpatData, KeyValue<Void, SpatFeatureCollection>> {
    private static final Logger logger = LoggerFactory.getLogger(SpatGeoJsonConverter.class);

    @Override
    public void init(ProcessorContext arg0) {}

    /**
     * Transform an ODE SPaT POJO to SPaT GeoJSON POJO.
     * 
     * @param rawKey   - Void type because ODE topics have no specified key
     * @param rawValue - The raw POJO
     * @return A key value pair: the key will always be Void and the value is the GeoJSON FeatureCollection POJO
     */
    @Override
    public KeyValue<Void, SpatFeatureCollection> transform(Void rawKey, OdeSpatData rawValue) {
        try {
            OdeSpatMetadata spatMetadata = (OdeSpatMetadata)rawValue.getMetadata();
            OdeSpatPayload spatPayload = (OdeSpatPayload)rawValue.getPayload();

			SpatFeatureCollection spatFeatureCollection = null;
            
            logger.info("Successfully created SPaT GeoJSON from " + spatMetadata.getOriginIp());
            return KeyValue.pair(null, spatFeatureCollection);
        } catch (Exception e) {
            String errMsg = String.format("Exception converting ODE SPaT to GeoJSON! Message: %s", e.getMessage());
            logger.error(errMsg, e);
            // KafkaStreams knows to remove null responses before allowing further steps from occurring
            return KeyValue.pair(null, null);
        }
    }

    @Override
    public void close() {
        // Nothing to do here
    }
}
