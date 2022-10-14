package us.dot.its.jpo.geojsonconverter.converter.spat;

import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.Topology;
import org.apache.kafka.streams.kstream.Consumed;
import org.apache.kafka.streams.kstream.Produced;
import org.apache.kafka.streams.kstream.KStream;

import us.dot.its.jpo.geojsonconverter.serialization.JsonSerdes;
import us.dot.its.jpo.geojsonconverter.geojson.spat.SpatFeatureCollection;
import us.dot.its.jpo.ode.model.OdeSpatData;

/**
 * Kafka Streams Topology builder for processing SPaT messages from
 * ODE SPaT JSON -> SPaT GeoJSON
 */
public class SpatTopology {

    public static Topology build(String odeJsonTopic, String geoJsonTopic) {
        StreamsBuilder builder = new StreamsBuilder();

        // Create stream from the ODE SPaT topic
        KStream<Void, OdeSpatData> odeSpatStream = 
            builder.stream(
                odeJsonTopic, 
                Consumed.with(
                    Serdes.Void(), // Key serializer: Raw has no key, so use the "Void" serializer
                    JsonSerdes.OdeSpat())   // Value serdes for OdeSpatData
                );

        // // Convert ODE SPaT to GeoJSON
        // KStream<String, SpatFeatureCollection> geoJsonSpatStream =
        //     odeSpatStream.transform(
        //         () -> new SpatGeoJsonConverter()
        //     );
            
        // // Push the GeoJSON stream back out to the SPaT GeoJSON topic 
        // geoJsonSpatStream.to(
        //     geoJsonTopic, 
        //     Produced.with(
        //         Serdes.Void(), // Key serializer: SPaT GeoJSON has no key, so use the "Void" serializer
        //         JsonSerdes.SpatGeoJson())  // Value serializer for SPaT GeoJSON
        //     );
        
        return builder.build();
    }
}
