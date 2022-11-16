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

import us.dot.its.jpo.geojsonconverter.pojos.spat.DeserializedRawSpat;
import us.dot.its.jpo.geojsonconverter.pojos.spat.ProcessedSpat;
import us.dot.its.jpo.geojsonconverter.pojos.spat.ValidationRawSpat;
import us.dot.its.jpo.geojsonconverter.serialization.JsonSerdes;
import us.dot.its.jpo.geojsonconverter.validator.JsonValidatorResult;
import us.dot.its.jpo.geojsonconverter.validator.SpatJsonValidator;
import us.dot.its.jpo.ode.model.OdeSpatData;

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
        KStream<Void, ValidationRawSpat> validatedOdeSpatStream = 
            rawOdeSpatStream.mapValues(
                (Void key, Bytes value) -> {
                    JsonValidatorResult validationResults = spatJsonValidator.validate(value.get());
                    ValidationRawSpat validationRawSpat = new ValidationRawSpat();
                    validationRawSpat.setOdeSpatBytes(value);
                    validationRawSpat.setValidatorResults(validationResults);
                    if (validationResults.isValid()) {
                        logger.info(validationResults.describeResults());
                    } else {
                        logger.warn(validationResults.describeResults());
                    }
                    return validationRawSpat;
                }
            );

        // Deserialize the raw JSON bytes to SPAT after validation
        KStream<Void, DeserializedRawSpat> odeSpatStream =
                validatedOdeSpatStream.mapValues(
                    (ValidationRawSpat validationRawSpat) -> {
                        DeserializedRawSpat deserializedRawSpat = new DeserializedRawSpat();
                        deserializedRawSpat.setOdeSpatOdeSpatData(JsonSerdes.OdeSpat().deserializer().deserialize(spatOdeJsonTopic, validationRawSpat.getOdeSpatBytes().get()));
                        deserializedRawSpat.setValidatorResults(validationRawSpat.getValidatorResults());
                        return deserializedRawSpat;
                    }
                );

        // Convert ODE SPaT to GeoJSON
        KStream<String, ProcessedSpat> processedJsonSpatStream =
            odeSpatStream.transform(
                () -> new SpatProcessedJsonConverter() // change this converter to something else NOT GEOJSON
            );

        
        processedJsonSpatStream.to(
            // Push the joined GeoJSON stream back out to the SPaT GeoJSON topic 
            spatProcessedJsonTopic, 
            Produced.with(Serdes.String(),
                    JsonSerdes.ProcessedSpat()));
        
        return builder.build();
    }
}
