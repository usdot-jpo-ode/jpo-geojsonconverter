package us.dot.its.jpo.geojsonconverter.converter.psm;

import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.common.utils.Bytes;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.Topology;
import org.apache.kafka.streams.kstream.Consumed;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.Produced;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import us.dot.its.jpo.geojsonconverter.partitioner.RsuPsmIdKey;
import us.dot.its.jpo.geojsonconverter.partitioner.RsuPsmIdPartitioner;
import us.dot.its.jpo.geojsonconverter.pojos.geojson.Point;
import us.dot.its.jpo.geojsonconverter.pojos.geojson.psm.DeserializedRawPsm;
import us.dot.its.jpo.geojsonconverter.pojos.geojson.psm.ProcessedPsm;
import us.dot.its.jpo.geojsonconverter.serialization.JsonSerdes;
import us.dot.its.jpo.geojsonconverter.validator.PsmJsonValidator;
import us.dot.its.jpo.geojsonconverter.validator.JsonValidatorResult;

/**
 * Kafka Streams Topology builder for processing PSM messages from ODE PSM JSON -> PSM GeoJSON
 */
public class PsmTopology {

    private static final Logger logger = LoggerFactory.getLogger(PsmTopology.class);

    public static Topology build(String psmOdeJsonTopic, String psmProcessedJsonTopic,
            PsmJsonValidator psmJsonValidator) {
        StreamsBuilder builder = new StreamsBuilder();

        // Stream for raw PSM messages
        KStream<Void, Bytes> rawOdePsmStream =
                builder.stream(psmOdeJsonTopic, Consumed.with(Serdes.Void(), Serdes.Bytes()));

        // Validate the JSON and write validation errors to the log at warn level
        // Passes the raw JSON along unchanged, even if there are validation errors.
        KStream<Void, DeserializedRawPsm> validatedOdePsmStream = rawOdePsmStream.mapValues((Void key, Bytes value) -> {
            DeserializedRawPsm deserializedRawPsm = new DeserializedRawPsm();
            try {
                JsonValidatorResult validationResults = psmJsonValidator.validate(value.get());
                deserializedRawPsm
                        .setOdePsmData(JsonSerdes.OdePsm().deserializer().deserialize(psmOdeJsonTopic, value.get()));
                deserializedRawPsm.setValidatorResults(validationResults);
                logger.debug(validationResults.describeResults());
            } catch (Exception e) {
                JsonValidatorResult validatorResult = new JsonValidatorResult();

                validatorResult.addException(e);
                deserializedRawPsm.setValidationFailure(true);
                deserializedRawPsm.setValidatorResults(validatorResult);
                deserializedRawPsm.setFailedMessage(e.getMessage());

                logger.error("Error in psmValidation:", e);
            }
            return deserializedRawPsm;
        });

        // Convert ODE PSM to GeoJSON
        KStream<RsuPsmIdKey, ProcessedPsm<Point>> processedJsonPsmStream =
                validatedOdePsmStream.transform(() -> new PsmProcessedJsonConverter());

        processedJsonPsmStream.to(
                // Push the joined GeoJSON stream back out to the PSM GeoJSON topic
                psmProcessedJsonTopic, Produced.with(JsonSerdes.RsuTypeIdKey(), JsonSerdes.ProcessedPsm(),
                        new RsuPsmIdPartitioner<RsuPsmIdKey, ProcessedPsm<Point>>()));

        return builder.build();
    }
}
