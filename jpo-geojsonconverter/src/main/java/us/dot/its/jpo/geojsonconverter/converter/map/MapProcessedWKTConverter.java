package us.dot.its.jpo.geojsonconverter.converter.map;

import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.kstream.Transformer;
import org.apache.kafka.streams.processor.ProcessorContext;

import us.dot.its.jpo.geojsonconverter.converter.WKTHandler;
import us.dot.its.jpo.geojsonconverter.partitioner.RsuIntersectionKey;
import us.dot.its.jpo.geojsonconverter.pojos.geojson.LineString;
import us.dot.its.jpo.geojsonconverter.pojos.geojson.map.ProcessedMap;

public class MapProcessedWKTConverter implements Transformer<RsuIntersectionKey, ProcessedMap<LineString>, KeyValue<RsuIntersectionKey, ProcessedMap<String>>> {
    @Override
    public void init(ProcessorContext arg0) {}

    /**
     * Transform a ProcessedMap GeoJSON POJO to ProcessedMap WKT POJO.
     * 
     * @param rawKey   - {@link RsuIntersectionKey} containing the RSU IP address and Intersection ID
     * @param rawValue - GeoJSON FeatureCollection POJO
     * @return A key value pair: the key an {@link RsuIntersectionKey} containing the RSU IP address and Intersection ID
     *  and the value is the WKT FeatureCollection POJO
     */
    @Override
    public KeyValue<RsuIntersectionKey, ProcessedMap<String>> transform(RsuIntersectionKey key, ProcessedMap<LineString> geoJSONMap) {
        ProcessedMap<String> wktProcessedMap = WKTHandler.processedMapGeoJSON2WKT(geoJSONMap);

        // Remove parts of the ProcessedMap that are not needed since WKT is not used by the Conflict Monitor
        wktProcessedMap.getProperties().setValidationMessages(null);
        for (int i = 0; i < wktProcessedMap.getMapFeatureCollection().getFeatures().length; i++) {
            wktProcessedMap.getMapFeatureCollection().getFeatures()[i].getProperties().setNodes(null);
        }

        return KeyValue.pair(key, wktProcessedMap);
    }

    @Override
    public void close() {
        // Nothing to do here
    }
}
