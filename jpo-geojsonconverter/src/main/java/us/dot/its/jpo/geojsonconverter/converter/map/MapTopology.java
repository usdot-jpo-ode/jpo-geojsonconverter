package us.dot.its.jpo.geojsonconverter.converter.map;

import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.common.utils.Bytes;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.Topology;
import org.apache.kafka.streams.kstream.Consumed;
import org.apache.kafka.streams.kstream.Produced;
import org.apache.kafka.streams.kstream.KStream;

import us.dot.its.jpo.geojsonconverter.serialization.JsonSerdes;
import us.dot.its.jpo.geojsonconverter.validator.MapJsonValidator;
import us.dot.its.jpo.geojsonconverter.geojson.map.MapFeatureCollection;
import us.dot.its.jpo.ode.model.OdeMapData;

/**
 * Kafka Streams Topology builder for processing MAP messages from
 * ODE MAP JSON -> MAP GeoJSON
 */
public class MapTopology {

    public static Topology build(String mapOdeJsonTopic, String mapGeoJsonTopic, MapJsonValidator mapJsonValidator) {
        StreamsBuilder builder = new StreamsBuilder();

        // Create stream from the ODE MAP topic
        KStream<Void, Bytes> rawOdeMapStream = 
            builder.stream(
                mapOdeJsonTopic, 
                Consumed.with(
                    Serdes.Void(), // Key serializer: Raw has no key, so use the "Void" serializer
                    Serdes.Bytes())   // Raw JSON bytes
                );

        // Validate the JSON and log validation errors
        

        // Deserialize the raw JSON bytes to an OdeMapData object
        KStream<Void, OdeMapData> odeMapStream =
            rawOdeMapStream.mapValues((Bytes value) -> {
                if (value == null) return null;
                return JsonSerdes.OdeMap().deserializer().deserialize(mapOdeJsonTopic, value.get());
            });

        // Convert ODE MAP to GeoJSON
        KStream<String, MapFeatureCollection> geoJsonMapStream =
            odeMapStream.transform(
                () -> new MapGeoJsonConverter()
            );
            
        // Push the GeoJSON stream back out to the MAP GeoJSON topic 
        geoJsonMapStream.to(
            mapGeoJsonTopic, 
            Produced.with(
                Serdes.String(), // Key is now the "RSU-IP:Intersection-ID"
                JsonSerdes.MapGeoJson())  // Value serializer for MAP GeoJSON
            );
        
        return builder.build();
    }
}
