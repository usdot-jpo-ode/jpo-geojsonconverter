package us.dot.its.jpo.geojsonconverter.converter.map;

import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.kstream.Transformer;
import org.apache.kafka.streams.processor.ProcessorContext;

import us.dot.its.jpo.geojsonconverter.converter.WKTHandler;
import us.dot.its.jpo.geojsonconverter.partitioner.RsuIntersectionKey;
import us.dot.its.jpo.geojsonconverter.pojos.geojson.map.*;
import us.dot.its.jpo.geojsonconverter.pojos.geojson.connectinglanes.*;

public class MapProcessedWKTConverter implements Transformer<RsuIntersectionKey, ProcessedMap<GeoJsonMapFeature, GeoJsonConnectingLanesFeature>, KeyValue<RsuIntersectionKey, ProcessedMap<WKTMapFeature, WKTConnectingLanesFeature>>> {
    @Override
    public void init(ProcessorContext arg0) {}

    /**
     * Transform an ODE MAP POJO to MAP GeoJSON POJO.
     * 
     * @param rawKey   - Void type because ODE topics have no specified key
     * @param rawValue - The raw POJO
     * @return A key value pair: the key an {@link RsuIntersectionKey} containing the RSU IP address and Intersection ID
     *  and the value is the GeoJSON FeatureCollection POJO
     */
    @Override
    public KeyValue<RsuIntersectionKey, ProcessedMap<WKTMapFeature, WKTConnectingLanesFeature>> transform(RsuIntersectionKey key, ProcessedMap<GeoJsonMapFeature, GeoJsonConnectingLanesFeature> geoJSONMap) {
        return KeyValue.pair(key, WKTHandler.processedMapGeoJSON2WKT(geoJSONMap));
    }

    @Override
    public void close() {
        // Nothing to do here
    }
}
