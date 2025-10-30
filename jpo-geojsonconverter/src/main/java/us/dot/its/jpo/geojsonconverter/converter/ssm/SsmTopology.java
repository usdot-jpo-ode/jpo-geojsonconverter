package us.dot.its.jpo.geojsonconverter.converter.ssm;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.common.utils.Bytes;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.Topology;
import org.apache.kafka.streams.kstream.Consumed;
import org.apache.kafka.streams.kstream.Produced;
import us.dot.its.jpo.geojsonconverter.partitioner.IntersectionIdPartitioner;
import us.dot.its.jpo.geojsonconverter.partitioner.RsuIntersectionKey;
import us.dot.its.jpo.geojsonconverter.pojos.common.DeserializedRawMessageFrame;
import us.dot.its.jpo.geojsonconverter.pojos.ssm.ProcessedSsm;
import us.dot.its.jpo.geojsonconverter.serialization.JsonSerdes;
import us.dot.its.jpo.geojsonconverter.validator.JsonValidatorResult;
import us.dot.its.jpo.geojsonconverter.validator.SsmJsonValidator;

import static us.dot.its.jpo.geojsonconverter.serialization.JsonSerdes.OdeMessageFrame;
import static us.dot.its.jpo.geojsonconverter.serialization.JsonSerdes.RsuIntersectionKey;

@Slf4j
public class SsmTopology {

    public static Topology build(
            String ssmOdeJsonTopic,
            String ssmProcessedJsonTopic,
            SsmJsonValidator ssmJsonValidator,
            SsmConverter converter) {
        var builder = new StreamsBuilder();

        builder
                .stream(ssmOdeJsonTopic,
                        Consumed.with(Serdes.Void(), Serdes.Bytes()))
                .mapValues((Void key, Bytes value) -> {
                    var rawMessageFrame = new DeserializedRawMessageFrame();
                    try (var serdes = OdeMessageFrame()) {
                        JsonValidatorResult validationResults = ssmJsonValidator.validate(value.get());
                        rawMessageFrame.setOdeMessageFrameData(
                                serdes.deserializer().deserialize(ssmOdeJsonTopic, value.get()));
                        rawMessageFrame.setValidationResults(validationResults);
                        log.debug(validationResults.describeResults());
                    } catch (Exception e) {
                        JsonValidatorResult validatorResult = new JsonValidatorResult();
                        validatorResult.addException(e);
                        rawMessageFrame.setValidationResults(validatorResult);
                        rawMessageFrame.setValidationFailure(true);
                        rawMessageFrame.setFailedMessage(e.getMessage());
                        log.error("Error in SSM validation: ", e);
                    }
                    return rawMessageFrame;
                })
                .map(new SsmTransformer(converter))
                .to(ssmProcessedJsonTopic,
                    Produced.with(
                        RsuIntersectionKey(),
                        JsonSerdes.ProcessedSsm(),
                        new IntersectionIdPartitioner<RsuIntersectionKey, ProcessedSsm>()));

        return builder.build();
    }
}
