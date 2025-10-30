package us.dot.its.jpo.geojsonconverter.converter.ssm;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.kstream.KeyValueMapper;
import us.dot.its.jpo.asn.j2735.r2024.SignalStatusMessage.SignalStatusMessageMessageFrame;
import us.dot.its.jpo.geojsonconverter.partitioner.RsuIntersectionKey;
import us.dot.its.jpo.geojsonconverter.pojos.common.DeserializedRawMessageFrame;
import us.dot.its.jpo.geojsonconverter.pojos.ssm.ProcessedSsm;
import us.dot.its.jpo.geojsonconverter.utils.ProcessedSchemaVersions;
import us.dot.its.jpo.ode.model.OdeMessageFrameData;
import us.dot.its.jpo.ode.model.OdeMessageFrameMetadata;
import us.dot.its.jpo.ode.model.OdeMessageFramePayload;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;

/**
 * KeyValueMapper transforms one SSM to one or more ProcessedSsms, for use in flatMap in the Topology.
 * Implement this as a KeyValueMapper, because the Transformer interface is deprecated.
 * The transformation is not stateful, so this doesn't need to be a processor.
 */
@Slf4j
public class SsmTransformer
        implements KeyValueMapper<Void, DeserializedRawMessageFrame, KeyValue<RsuIntersectionKey, ProcessedSsm>> {

    private final SsmConverter converter;

    public SsmTransformer(SsmConverter converter) {
        this.converter = converter;
    }

    @Override
    public KeyValue<RsuIntersectionKey, ProcessedSsm> apply(Void rawKey, DeserializedRawMessageFrame rawSsm) {

        try {
            if (!rawSsm.isValidationFailure()) {
                OdeMessageFrameData rawValue = rawSsm.getOdeMessageFrameData();
                OdeMessageFrameMetadata metadata = rawValue.getMetadata();
                OdeMessageFramePayload payload = rawValue.getPayload();
                SignalStatusMessageMessageFrame ssmMessageFrame = (SignalStatusMessageMessageFrame)payload.getData();
                KeyValue<RsuIntersectionKey, ProcessedSsm> processedSsm
                        = createProcessedSsm(ssmMessageFrame, metadata);
                converter.jsonValidation(processedSsm.value, rawSsm.getValidationResults());
                return processedSsm;
            } else {
                var processed = new ProcessedSsm();
                ZonedDateTime utcDateTime = ZonedDateTime.now(ZoneOffset.UTC);
                processed.setOdeReceivedAt(utcDateTime);
                converter.jsonValidation(processed, rawSsm.getValidationResults());
                RsuIntersectionKey key = new RsuIntersectionKey();
                key.setRsuId("ERROR");
                return KeyValue.pair(key, processed);
            }
        } catch (Exception e) {
            log.error("Error converting Ssm to Processed Ssm", e);
        }
        var key = new RsuIntersectionKey();
        key.setRsuId("ERROR");
        return KeyValue.pair(key, null);
    }

    private KeyValue<RsuIntersectionKey, ProcessedSsm> createProcessedSsm(SignalStatusMessageMessageFrame ssm,
                 OdeMessageFrameMetadata metadata) {

        final ZonedDateTime odeReceivedAt = Instant.parse(metadata.getOdeReceivedAt()).atZone(ZoneId.of("UTC"));

        // Process message frame
        ProcessedSsm processedSsm =  converter.processSsm(ssm, odeReceivedAt);

        // Add metadata
        processedSsm.setOdeReceivedAt(odeReceivedAt);
        processedSsm.setAsn1(metadata.getAsn1());
        processedSsm.setOriginIp(metadata.getOriginIp());
        processedSsm.setSchemaVersion(ProcessedSchemaVersions.PROCESSED_SSM_SCHEMA_VERSION);

        var key = new RsuIntersectionKey();
        key.setRsuId(metadata.getOriginIp());
        Integer intersectionId = processedSsm.getIntersectionId();
        if (intersectionId != null) {
            key.setIntersectionId(intersectionId);
        }
        Integer region = processedSsm.getRegion();
        if (region != null) {
            key.setRegion(region);
        }

        return new KeyValue<>(key, processedSsm);
    }
}
