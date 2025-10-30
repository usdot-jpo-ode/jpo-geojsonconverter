package us.dot.its.jpo.geojsonconverter.converter.srm;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.common.utils.Bytes;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.Topology;
import org.apache.kafka.streams.kstream.Consumed;
import org.apache.kafka.streams.kstream.Produced;
import us.dot.its.jpo.geojsonconverter.partitioner.RsuIdPartitioner;
import us.dot.its.jpo.geojsonconverter.partitioner.RsuVehicleIdKey;
import us.dot.its.jpo.geojsonconverter.pojos.common.DeserializedRawMessageFrame;
import us.dot.its.jpo.geojsonconverter.pojos.geojson.srm.ProcessedSrm;
import us.dot.its.jpo.geojsonconverter.serialization.JsonSerdes;
import us.dot.its.jpo.geojsonconverter.validator.JsonValidatorResult;
import us.dot.its.jpo.geojsonconverter.validator.SrmJsonValidator;

import static us.dot.its.jpo.geojsonconverter.serialization.JsonSerdes.OdeMessageFrame;
import static us.dot.its.jpo.geojsonconverter.serialization.JsonSerdes.RsuVehicleIdKey;

@Slf4j
public class SrmTopology {

    public static Topology build(
            String srmOdeJsonTopic,
            String srmProcessedJsonTopic,
            SrmJsonValidator srmJsonValidator,
            SrmConverter converter) {
        var builder = new StreamsBuilder();

        builder
                .stream(srmOdeJsonTopic, Consumed.with(Serdes.Void(), Serdes.Bytes()))
                .mapValues((Void key, Bytes value) -> {
                    var rawMessageFrame = new DeserializedRawMessageFrame();
                    try (var serdes = OdeMessageFrame()) {
                        JsonValidatorResult validationResults = srmJsonValidator.validate(value.get());
                        rawMessageFrame.setOdeMessageFrameData(
                                serdes.deserializer().deserialize(srmOdeJsonTopic, value.get()));
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
                .map(new SrmTransformer(converter))
                .to(srmProcessedJsonTopic,
                        Produced.with(RsuVehicleIdKey(),
                                JsonSerdes.ProcessedSrm(),
                                new RsuIdPartitioner<RsuVehicleIdKey, ProcessedSrm>()));
        return builder.build();
    }
}
