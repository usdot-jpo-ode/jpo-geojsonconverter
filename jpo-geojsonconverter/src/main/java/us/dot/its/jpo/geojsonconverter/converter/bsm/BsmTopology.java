package us.dot.its.jpo.geojsonconverter.converter.bsm;

import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.common.utils.Bytes;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.Topology;
import org.apache.kafka.streams.kstream.Consumed;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.Produced;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import us.dot.its.jpo.geojsonconverter.partitioner.RsuLogKey;
import us.dot.its.jpo.geojsonconverter.partitioner.RsuLogKeyPartitioner;
import us.dot.its.jpo.geojsonconverter.pojos.geojson.Point;
import us.dot.its.jpo.geojsonconverter.pojos.geojson.bsm.DeserializedRawBsm;
import us.dot.its.jpo.geojsonconverter.pojos.geojson.bsm.ProcessedBsmCollection;
import us.dot.its.jpo.geojsonconverter.serialization.JsonSerdes;
import us.dot.its.jpo.geojsonconverter.validator.BsmJsonValidator;
import us.dot.its.jpo.geojsonconverter.validator.JsonValidatorResult;

/**
 * Kafka Streams Topology builder for processing BSM messages from
 * ODE BSM JSON -> BSM GeoJSON
 */
public class BsmTopology {

    private static final Logger logger = LoggerFactory.getLogger(BsmTopology.class);

    public static Topology build(String bsmOdeJsonTopic, String bsmProcessedJsonTopic, BsmJsonValidator bsmJsonValidator) {
        StreamsBuilder builder = new StreamsBuilder();

        // Stream for raw BSM messages
        KStream<Void, Bytes> rawOdeBsmStream =
            builder.stream(
                bsmOdeJsonTopic,
                Consumed.with(
                    Serdes.Void(),  // Raw topic has no key
                    Serdes.Bytes()  // Raw JSON bytes
                )
            );

        // Validate the JSON and write validation errors to the log at warn level
        // Passes the raw JSON along unchanged, even if there are validation errors.
        KStream<Void, DeserializedRawBsm> validatedOdeBsmStream = 
            rawOdeBsmStream.mapValues(
                (Void key, Bytes value) -> {
                    DeserializedRawBsm deserializedRawBsm = new DeserializedRawBsm();
                    try {
                        JsonValidatorResult validationResults = bsmJsonValidator.validate(value.get());
                        deserializedRawBsm.setOdeBsmData(JsonSerdes.OdeBsm().deserializer().deserialize(bsmOdeJsonTopic, value.get()));
                        deserializedRawBsm.setValidatorResults(validationResults);
                        logger.debug(validationResults.describeResults());
                    } catch (Exception e) {
                        JsonValidatorResult validatorResult = new JsonValidatorResult();

                        validatorResult.addException(e);
                        deserializedRawBsm.setValidationFailure(true);
                        deserializedRawBsm.setValidatorResults(validatorResult);
                        deserializedRawBsm.setFailedMessage(e.getMessage());

                        logger.error("Error in bsmValidation:", e);
                    }
                    return deserializedRawBsm;
                }
            );

        // Convert ODE BSM to GeoJSON
        KStream<RsuLogKey, ProcessedBsmCollection<Point>> processedJsonBsmStream =
            validatedOdeBsmStream.transform(
                () -> new BsmProcessedJsonConverter()
            );

            processedJsonBsmStream.to(
            // Push the joined GeoJSON stream back out to the BSM GeoJSON topic 
            bsmProcessedJsonTopic, 
            Produced.with(
                JsonSerdes.RsuLogKey(),
                JsonSerdes.ProcessedBsm(),
                new RsuLogKeyPartitioner<RsuLogKey, ProcessedBsmCollection<Point>>())
        );
        
        return builder.build();
    }
}
