package us.dot.its.jpo.geojsonconverter.converter.srm;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.kstream.KeyValueMapper;
import us.dot.its.jpo.asn.j2735.r2024.SignalRequestMessage.SignalRequestMessageMessageFrame;
import us.dot.its.jpo.geojsonconverter.partitioner.RsuVehicleIdKey;
import us.dot.its.jpo.geojsonconverter.pojos.common.DeserializedRawMessageFrame;
import us.dot.its.jpo.geojsonconverter.pojos.geojson.srm.ProcessedSrm;
import us.dot.its.jpo.geojsonconverter.pojos.geojson.srm.SrmProperties;
import us.dot.its.jpo.geojsonconverter.utils.ProcessedSchemaVersions;
import us.dot.its.jpo.ode.model.OdeMessageFrameData;
import us.dot.its.jpo.ode.model.OdeMessageFrameMetadata;
import us.dot.its.jpo.ode.model.OdeMessageFramePayload;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;

/**
 * KeyValueMapper that converts a keyless stream of SRM MessageFrames to a Key-Value Pair.
 * <dl>
 *     <dh>Key</dh>
 *     <dd>RsuVehicleIdKey - The key contains the RSU ID and Vehicle ID. Note we don't use
 *     IntersectionId/Region in the key, because in the most general case a ProcessedSrm7 can have
 *     multiple requests with different IntersectionIDs</dd>
 *     <dh>Value</dh>
 *     <dd>A {@link ProcessedSrm}</dd>
 * </dl>>
 */
@Slf4j
public class SrmTransformer
    implements KeyValueMapper<Void, DeserializedRawMessageFrame, KeyValue<RsuVehicleIdKey, ProcessedSrm>> {

    private final SrmConverter converter;

    public SrmTransformer(SrmConverter converter) {
        this.converter = converter;
    }

    @Override
    public KeyValue<RsuVehicleIdKey, ProcessedSrm> apply(Void rawKey, DeserializedRawMessageFrame rawSrm) {
        try {
            if (!rawSrm.isValidationFailure()) {
                OdeMessageFrameData rawValue = rawSrm.getOdeMessageFrameData();
                OdeMessageFrameMetadata metadata = rawValue.getMetadata();
                OdeMessageFramePayload payload = rawValue.getPayload();
                SignalRequestMessageMessageFrame ssmMessageFrame = (SignalRequestMessageMessageFrame)payload.getData();
                KeyValue<RsuVehicleIdKey, ProcessedSrm> processedSrm = createProcessedSrm(ssmMessageFrame, metadata);
                converter.jsonValidation(processedSrm.value.getProperties(), rawSrm.getValidationResults());
                return processedSrm;
            } else {
                var processed = new ProcessedSrm(null, new SrmProperties());
                ZonedDateTime utcDateTime = ZonedDateTime.now(ZoneOffset.UTC);
                processed.getProperties().setOdeReceivedAt(utcDateTime);
                converter.jsonValidation(processed.getProperties(), rawSrm.getValidationResults());
                processed.getProperties().addValidationMessage(rawSrm.getFailedMessage());
                RsuVehicleIdKey key = new RsuVehicleIdKey();
                key.setRsuId("ERROR");
                return KeyValue.pair(key, processed);
            }
        } catch (Exception e) {
            log.error("Error converting SRM to Processed SRM", e);
        }
        var key = new RsuVehicleIdKey();
        key.setRsuId("ERROR");
        return KeyValue.pair(key, null);
    }

    private KeyValue<RsuVehicleIdKey, ProcessedSrm> createProcessedSrm(
            SignalRequestMessageMessageFrame srm, OdeMessageFrameMetadata metadata) {
        final ZonedDateTime odeReceivedAt = Instant.parse(metadata.getOdeReceivedAt()).atZone(ZoneId.of("UTC"));

        // Process message frame
        ProcessedSrm processedSrm =  converter.processSrm(srm, odeReceivedAt);

        // Add metadata
        SrmProperties properties = processedSrm.getProperties();
        properties.setOdeReceivedAt(odeReceivedAt);
        properties.setAsn1(metadata.getAsn1());
        final String originIp = metadata.getOriginIp();
        properties.setOriginIp(originIp);
        properties.setSchemaVersion(ProcessedSchemaVersions.PROCESSED_SSM_SCHEMA_VERSION);

        // Note we don't use IntersectionId/Region in the key, because in the most general case the ProcessedSrm have
        // multiple requests with different IntersectionIDs
        RsuVehicleIdKey key = new RsuVehicleIdKey(properties.getOriginIp(), properties.getVehicleID());

       return new KeyValue<>(key, processedSrm);
    }
}
