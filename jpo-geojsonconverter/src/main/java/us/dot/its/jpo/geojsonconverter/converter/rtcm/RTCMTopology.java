package us.dot.its.jpo.geojsonconverter.converter.rtcm;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.common.utils.Bytes;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.Topology;
import org.apache.kafka.streams.kstream.Consumed;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.Produced;
import us.dot.its.jpo.geojsonconverter.partitioner.RsuStationIdKey;
import us.dot.its.jpo.geojsonconverter.pojos.geojson.rtcm.DeserializedRawRTCM;
import us.dot.its.jpo.geojsonconverter.pojos.geojson.rtcm.ProcessedRTCM;
import us.dot.its.jpo.geojsonconverter.serialization.JsonSerdes;
import us.dot.its.jpo.geojsonconverter.validator.JsonValidatorResult;
import us.dot.its.jpo.geojsonconverter.validator.RTCMJsonValidator;
import us.dot.its.jpo.ode.model.OdeMessageFrameData;

/**
 * Kafka Streams Topology for processing RTCMcorrections messages received from the ODE.
 */
@Slf4j
public class RTCMTopology {

    public static Topology build(final String rtcmOdeJsonTopic,
                                 final String rtcmProcessedJsonTopic,
                                 RTCMJsonValidator rtcmJsonValidator,
                                 RTCMConverter rtcmConverter) {
        var builder = new StreamsBuilder();

        // Stream in raw RTCMcorrections
        KStream<Void, Bytes> rawOdeRTCMStream =
                builder.stream(rtcmOdeJsonTopic,
                        Consumed.with(Serdes.Void(),
                                Serdes.Bytes()));

        // Validate the JSON for J2735 conformance and ODE metadata
        KStream<Void, DeserializedRawRTCM> validatedOdeRTCMStream =
            rawOdeRTCMStream.mapValues((Void key, Bytes value) -> {
                var deserializedRawRTCM = new DeserializedRawRTCM();
                try (var serde = JsonSerdes.OdeMessageFrame()) {
                    JsonValidatorResult validatorResult = rtcmJsonValidator.validate(value.get());
                    OdeMessageFrameData messageFrameData
                            = serde.deserializer().deserialize(rtcmOdeJsonTopic, value.get());
                    deserializedRawRTCM.setOdeRTCMMessageFrameData(messageFrameData);
                    deserializedRawRTCM.setValidatorResults(validatorResult);
                    log.debug(validatorResult.describeResults());
                } catch (Exception e) {
                    var validatorResult = new JsonValidatorResult();
                    validatorResult.addException(e);
                    deserializedRawRTCM.setValidationFailure(true);
                    deserializedRawRTCM.setValidatorResults(validatorResult);
                    deserializedRawRTCM.setFailedMessage(e.getMessage());
                    log.error("Error in RTCM JSON validation", e);
                }
                return deserializedRawRTCM;
            });

        // Convert to ProcessedRTCM
        KStream<RsuStationIdKey, ProcessedRTCM> processedRTCMStream =
                validatedOdeRTCMStream.transform(() -> new RTCMTransformer(rtcmConverter));

        processedRTCMStream.to(rtcmProcessedJsonTopic,
                Produced.with(
                        JsonSerdes.RsuStationIdKey(),
                        JsonSerdes.ProcessedRTCM()
                ));

        return builder.build();
    }
}
