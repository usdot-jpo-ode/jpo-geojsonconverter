package us.dot.its.jpo.geojsonconverter.converter.rtcm;


import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.kstream.Transformer;
import org.apache.kafka.streams.processor.ProcessorContext;
import us.dot.its.jpo.asn.j2735.r2024.RTCMcorrections.RTCMcorrectionsMessageFrame;
import us.dot.its.jpo.geojsonconverter.partitioner.RsuStationIdKey;
import us.dot.its.jpo.geojsonconverter.pojos.geojson.rtcm.DeserializedRawRTCM;
import us.dot.its.jpo.geojsonconverter.pojos.geojson.rtcm.ProcessedRTCM;
import us.dot.its.jpo.geojsonconverter.pojos.geojson.rtcm.RTCMProperties;
import us.dot.its.jpo.geojsonconverter.utils.ProcessedSchemaVersions;
import us.dot.its.jpo.ode.model.OdeMessageFrameData;
import us.dot.its.jpo.ode.model.OdeMessageFrameMetadata;
import us.dot.its.jpo.ode.model.OdeMessageFramePayload;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeParseException;

/**
 * Streams transformer. Converts {@link DeserializedRawRTCM}s to {@link ProcessedRTCM}s
 */
@Slf4j
public class RTCMTransformer
    implements Transformer<Void, DeserializedRawRTCM, KeyValue<RsuStationIdKey, ProcessedRTCM>> {

    private final RTCMConverter rtcmConverter;

    public RTCMTransformer(RTCMConverter rtcmConverter) {
        this.rtcmConverter = rtcmConverter;
    }

    @Override
    public void init(ProcessorContext context) {
        // Nothing to initialize
    }

    @Override
    public KeyValue<RsuStationIdKey, ProcessedRTCM> transform(Void rawKey, DeserializedRawRTCM rawRtcm) {
        try {
            if (!rawRtcm.isValidationFailure()) {
                // Extract ODE stuff
                OdeMessageFrameData rawData = rawRtcm.getOdeRTCMMessageFrameData();
                OdeMessageFrameMetadata metadata = rawData.getMetadata();
                OdeMessageFramePayload payload = rawData.getPayload();
                RTCMcorrectionsMessageFrame rtcmMessageFrame =
                        (RTCMcorrectionsMessageFrame) payload.getData();

                // Create ProcessedRTCM
                ProcessedRTCM processed = rtcmConverter.processRTCM(rtcmMessageFrame);

                // Metadata
                processed.getProperties().setSchemaVersion(ProcessedSchemaVersions.PROCESSED_RTCM_SCHEMA_VERSION);
                try {
                    ZonedDateTime odeReceivedAt = Instant.parse(metadata.getOdeReceivedAt()).atZone(ZoneId.of("UTC"));
                    processed.getProperties().setOdeReceivedAt(odeReceivedAt);
                } catch (DateTimeParseException e) {
                    log.error("Error parsing ODE received at {}", metadata.getOdeReceivedAt());
                    processed.getProperties().addValidationMessage("Error parsing ODE received at date/time: " + metadata.getOdeReceivedAt());
                }
                processed.getProperties().setAsn1(metadata.getAsn1());
                if (metadata.getOriginIp() != null && !metadata.getOriginIp().isEmpty()) {
                    processed.getProperties().setOriginIp(metadata.getOriginIp());
                }

                // Validation results
                rtcmConverter.jsonValidation(processed.getProperties(), rawRtcm.getValidatorResults());

                // Key
                var key = new RsuStationIdKey();
                key.setRsuId(metadata.getOriginIp());
                key.setStationId(processed.getProperties().getStationId());

                return KeyValue.pair(key, processed);

            } else {
                var processed = new ProcessedRTCM(null, new RTCMProperties());
                ZonedDateTime utcDateTime = ZonedDateTime.now(ZoneOffset.UTC);
                processed.getProperties().setOdeReceivedAt(utcDateTime);
                rtcmConverter.jsonValidation(processed.getProperties(), rawRtcm.getValidatorResults());
                processed.getProperties().addValidationMessage(rawRtcm.getFailedMessage());
                RsuStationIdKey key = new RsuStationIdKey();
                key.setRsuId("ERROR");
                return KeyValue.pair(key, processed);
            }
        } catch (Exception e) {
            String errMsg = String.format("Exception converting ODE BSM to Processed BSM! Message: %s", e.getMessage());
            log.error(errMsg, e);
            RsuStationIdKey key = new RsuStationIdKey();
            key.setRsuId("ERROR");
            return KeyValue.pair(key, null);
        }
    }

    @Override
    public void close() {
        // Nothing to close
    }
}
