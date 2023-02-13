package us.dot.its.jpo.geojsonconverter.converter.spat;

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
import us.dot.its.jpo.geojsonconverter.pojos.spat.DeserializedRawSpat;
import us.dot.its.jpo.geojsonconverter.pojos.spat.ProcessedSpat;
import us.dot.its.jpo.geojsonconverter.serialization.JsonSerdes;
import us.dot.its.jpo.geojsonconverter.validator.JsonValidatorResult;
import us.dot.its.jpo.geojsonconverter.validator.SpatJsonValidator;

/**
 * Kafka Streams Topology builder for processing SPaT messages from
 * ODE SPaT JSON -> SPaT GeoJSON
 */
public class SpatTopology {

    private static final Logger logger = LoggerFactory.getLogger(SpatTopology.class);

    public static Topology build(String spatOdeJsonTopic, String spatProcessedJsonTopic,
            SpatJsonValidator spatJsonValidator) {
        StreamsBuilder builder = new StreamsBuilder();

        // Stream for raw SPAT messages
        KStream<Void, Bytes> rawOdeSpatStream =
            builder.stream(
                spatOdeJsonTopic,
                Consumed.with(
                    Serdes.Void(),  // Raw topic has no key
                    Serdes.Bytes()  // Raw JSON bytes
                )
            );

        // Validate the JSON and write validation errors to the log at warn level
        // Passes the raw JSON along unchanged, even if there are validation errors.
        KStream<Void, DeserializedRawSpat> validatedOdeSpatStream = 
            rawOdeSpatStream.mapValues(
                (Void key, Bytes value) -> {
                    DeserializedRawSpat deserializedRawSpat = new DeserializedRawSpat();
                    try {
                        JsonValidatorResult validationResults = spatJsonValidator.validate(value.get());
                        deserializedRawSpat.setOdeSpatOdeSpatData(JsonSerdes.OdeSpat().deserializer().deserialize(spatOdeJsonTopic, value.get()));
                        deserializedRawSpat.setValidatorResults(validationResults);
                        logger.debug(validationResults.describeResults());
                    } catch (Exception e) {
                        logger.error("Error in mapValues:", e);
                    }
                    return deserializedRawSpat;
                }
            );

        // Convert ODE SPaT to GeoJSON
        KStream<RsuIntersectionKey, ProcessedSpat> processedJsonSpatStream =
            validatedOdeSpatStream.transform(
                () -> new SpatProcessedJsonConverter() // change this converter to something else NOT GEOJSON
            );

        
        processedJsonSpatStream.to(
            // Push the joined GeoJSON stream back out to the SPaT GeoJSON topic 
            spatProcessedJsonTopic, 
            Produced.with(
                JsonSerdes.RsuIntersectionKey(),
                JsonSerdes.ProcessedSpat(), 
                new RsuIdPartitioner<RsuIntersectionKey, ProcessedSpat>())  // Partition by RSU ID
        );
        
        return builder.build();
    }
}
