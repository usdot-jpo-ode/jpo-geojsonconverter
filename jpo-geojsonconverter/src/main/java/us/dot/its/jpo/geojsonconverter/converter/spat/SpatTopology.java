package us.dot.its.jpo.geojsonconverter.converter.spat;

import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.common.utils.Bytes;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.Topology;
import org.apache.kafka.streams.kstream.Consumed;
import org.apache.kafka.streams.kstream.Joined;
import org.apache.kafka.streams.kstream.Produced;
import org.apache.kafka.streams.state.KeyValueStore;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.KTable;
import org.apache.kafka.streams.kstream.Materialized;

import us.dot.its.jpo.geojsonconverter.serialization.JsonSerdes;
import us.dot.its.jpo.geojsonconverter.geojson.Point;
import us.dot.its.jpo.geojsonconverter.geojson.map.MapFeature;
import us.dot.its.jpo.geojsonconverter.geojson.map.MapFeatureCollection;
import us.dot.its.jpo.geojsonconverter.geojson.spat.SpatFeatureCollection;
import us.dot.its.jpo.ode.model.OdeSpatData;

/**
 * Kafka Streams Topology builder for processing SPaT messages from
 * ODE SPaT JSON -> SPaT GeoJSON
 */
public class SpatTopology {

    public static Topology build(String spatOdeJsonTopic, String spatGeoJsonTopic, String mapGeoJsonTopic) {
        StreamsBuilder builder = new StreamsBuilder();

        // Create stream from the ODE SPaT topic
        KStream<Void, OdeSpatData> odeSpatStream = 
            builder.stream(
                spatOdeJsonTopic, 
                Consumed.with(
                    Serdes.Void(), // Key serializer: Raw has no key, so use the "Void" serializer
                    JsonSerdes.OdeSpat())   // Value serdes for OdeSpatData
                );

        // KTable MapGeoJSON
        KTable<String, MapFeatureCollection> mapFeatureCollectionTable = 
            builder.table(
                mapGeoJsonTopic,
                Consumed.with(Serdes.String(), JsonSerdes.MapGeoJson()),
                Materialized.<String, MapFeatureCollection, KeyValueStore<Bytes, byte[]>> as ("mapgeojson-store")
                    .withKeySerde(Serdes.String()).withValueSerde(JsonSerdes.MapGeoJson())
                );

        // Convert ODE SPaT to GeoJSON
        KStream<String, SpatFeatureCollection> geoJsonSpatStream =
            odeSpatStream.transform(
                () -> new SpatGeoJsonConverter()
            );

        // Join SPaT GeoJSON stream with the MapGeoJSON table
        geoJsonSpatStream.join(mapFeatureCollectionTable, (spatGeoJson, mapGeoJson) -> {
                for (int i = 0; i < spatGeoJson.getFeatures().length; i++) {
                    Integer signalGroupId = spatGeoJson.getFeatures()[i].getProperties().getSignalGroupId();
                    for(MapFeature mapFeature : mapGeoJson.getFeatures()) {  
                        if (mapFeature.getProperties().getLaneId() == signalGroupId) {
                            Point signalPoint = new Point(mapFeature.getGeometry().getCoordinates()[0]);
                            spatGeoJson.getFeatures()[i].setGeometry(signalPoint);
                        }
                    }
                }
                return spatGeoJson;
            }, 
            Joined.<String, SpatFeatureCollection, MapFeatureCollection>as("geojson-joined").withKeySerde(Serdes.String())
                .withValueSerde(JsonSerdes.SpatGeoJson()).withOtherValueSerde(JsonSerdes.MapGeoJson()));

        // Push the GeoJSON stream back out to the SPaT GeoJSON topic 
        geoJsonSpatStream.to(
            spatGeoJsonTopic, 
            Produced.with(
                Serdes.String(), // Key is now the "RSU-IP:Intersection-ID"
                JsonSerdes.SpatGeoJson())  // Value serializer for SPaT GeoJSON
            );
        
        return builder.build();
    }
}
