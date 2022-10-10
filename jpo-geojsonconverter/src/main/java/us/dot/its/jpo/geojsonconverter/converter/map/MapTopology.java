package us.dot.its.jpo.geojsonconverter.converter.map;

import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.Topology;
import org.apache.kafka.streams.kstream.Consumed;
import org.apache.kafka.streams.kstream.Produced;
import org.apache.kafka.streams.kstream.KStream;

import us.dot.its.jpo.geojsonconverter.serialization.JsonSerdes;
import us.dot.its.jpo.geojsonconverter.geojson.map.MapFeatureCollection;
import us.dot.its.jpo.ode.model.OdeMapData;

/**
 * Kafka Streams Topology builder for processing MAP messages from
 * ODE MAP JSON -> MAP GeoJSON
 */
public class MapTopology {

    public static Topology build(String odeJsonTopic, String geoJsonTopic) {
        StreamsBuilder builder = new StreamsBuilder();

        // Create stream from the ODE MAP topic
        KStream<Void, OdeMapData> odeMapStream = 
            builder.stream(
                odeJsonTopic, 
                Consumed.with(
                    Serdes.Void(), // Key serializer: Raw has no key, so use the "Void" serializer
                    JsonSerdes.OdeMap())   // Value serdes for OdeMapData
                );

        // Convert ODE MAP to GeoJSON
        KStream<String, MapFeatureCollection> geoJsonMapStream =
            odeMapStream.transform(
                () -> new MapGeoJsonConverter()
            );
            
        // Push the GeoJSON stream back out to the MAP GeoJSON topic 
        geoJsonMapStream.to(
            geoJsonTopic, 
            Produced.with(
                Serdes.String(), // Key is now the "RSU-IP:Intersection-ID"
                JsonSerdes.MapGeoJson())  // Value serializer for MAP GeoJSON
            );
        
        return builder.build();
    }
}
