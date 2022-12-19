package us.dot.its.jpo.geojsonconverter.converter.map;

import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.common.utils.Bytes;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.Topology;
import org.apache.kafka.streams.kstream.Consumed;
import org.apache.kafka.streams.kstream.Produced;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.apache.kafka.streams.kstream.KStream;

import us.dot.its.jpo.geojsonconverter.partitioner.RsuIdPartitioner;
import us.dot.its.jpo.geojsonconverter.partitioner.RsuIntersectionKey;
import us.dot.its.jpo.geojsonconverter.pojos.geojson.map.DeserializedRawMap;
import us.dot.its.jpo.geojsonconverter.pojos.geojson.map.ProcessedMap;
import us.dot.its.jpo.geojsonconverter.serialization.JsonSerdes;
import us.dot.its.jpo.geojsonconverter.validator.JsonValidatorResult;
import us.dot.its.jpo.geojsonconverter.validator.MapJsonValidator;

/**
 * Kafka Streams Topology builder for processing MAP messages from
 * ODE MAP JSON -> MAP GeoJSON
 */
public class MapTopology {

    private static final Logger logger = LoggerFactory.getLogger(MapTopology.class);

    public static Topology build(String mapOdeJsonTopic, String processedMapTopic, MapJsonValidator mapJsonValidator) {
        StreamsBuilder builder = new StreamsBuilder();

        // Create stream from the ODE MAP topic
        KStream<Void, Bytes> rawOdeMapStream = 
            builder.stream(
                mapOdeJsonTopic, 
                Consumed.with(
                    Serdes.Void(), // Key serializer: Raw has no key, so use the "Void" serializer
                    Serdes.Bytes())   // Raw JSON bytes
                );

        // Validate the JSON and log validation errors at warn level
        // Passes the raw JSON along unchanged, even if there are validation errors.
        KStream<Void, DeserializedRawMap> validatedOdeMapStream = 
            rawOdeMapStream.mapValues(
                (Void key, Bytes value) -> {
                    DeserializedRawMap deserializedRawMap = new DeserializedRawMap();
                    JsonValidatorResult validationResults = mapJsonValidator.validate(value.get());
                    deserializedRawMap.setOdeMapOdeMapData(JsonSerdes.OdeMap().deserializer().deserialize(mapOdeJsonTopic, value.get()));
                    deserializedRawMap.setValidatorResults(validationResults);
                    if (validationResults.isValid()) {
                        logger.debug(validationResults.describeResults());
                    } else {
                        logger.debug(validationResults.describeResults());
                    }
                    return deserializedRawMap;
                }
            );

        // Convert ODE MAP to GeoJSON
        KStream<RsuIntersectionKey, ProcessedMap> geoJsonMapStream =
            validatedOdeMapStream.transform(
                () -> new MapProcessedJsonConverter()
            );
            
        // Push the GeoJSON stream back out to the MAP GeoJSON topic 
        geoJsonMapStream.to(
            processedMapTopic, 
            Produced.with(
                JsonSerdes.RsuIntersectionKey(), // Key is now an RsuIntersectionKey object
                JsonSerdes.ProcessedMap(),      // Value serializer for MAP GeoJSON
                new RsuIdPartitioner<RsuIntersectionKey, ProcessedMap>())  // Partition by RSU ID
            );
        
        return builder.build();
    }
}
