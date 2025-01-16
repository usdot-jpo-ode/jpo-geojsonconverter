package us.dot.its.jpo.geojsonconverter.converter.psm;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.kstream.Transformer;
import org.apache.kafka.streams.processor.ProcessorContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.networknt.schema.ValidationMessage;

import us.dot.its.jpo.geojsonconverter.pojos.geojson.Point;
import us.dot.its.jpo.geojsonconverter.partitioner.RsuTypeIdKey;
import us.dot.its.jpo.geojsonconverter.pojos.ProcessedValidationMessage;
import us.dot.its.jpo.geojsonconverter.pojos.geojson.psm.*;
import us.dot.its.jpo.geojsonconverter.validator.JsonValidatorResult;

import us.dot.its.jpo.ode.model.OdePsmData;
import us.dot.its.jpo.ode.model.OdePsmMetadata;
import us.dot.its.jpo.ode.model.OdePsmPayload;
import us.dot.its.jpo.ode.plugin.j2735.J2735PSM;

public class PsmProcessedJsonConverter
        implements Transformer<Void, DeserializedRawPsm, KeyValue<RsuTypeIdKey, ProcessedPsm<Point>>> {
    private static final Logger logger = LoggerFactory.getLogger(PsmProcessedJsonConverter.class);

    @Override
    public void init(ProcessorContext arg0) {}

    /**
     * Transform an ODE PSM POJO to Processed PSM POJO.
     * 
     * @param rawKey - Void type because ODE topics have no specified key
     * @param rawPsm - The raw POJO
     * @return A key value pair: the key a RsuTypeIdKey containing the RSU IP address or the PSM log file name
     */
    @Override
    public KeyValue<RsuTypeIdKey, ProcessedPsm<Point>> transform(Void rawKey, DeserializedRawPsm rawPsm) {
        try {
            if (!rawPsm.getValidationFailure()) {
                OdePsmData rawValue = new OdePsmData();
                rawValue.setMetadata(rawPsm.getOdePsmData().getMetadata());
                OdePsmMetadata psmMetadata = (OdePsmMetadata) rawValue.getMetadata();

                rawValue.setPayload(rawPsm.getOdePsmData().getPayload());
                OdePsmPayload psmPayload = (OdePsmPayload) rawValue.getPayload();

                ProcessedPsm<Point> processedPsm =
                        createProcessedPsm(psmMetadata, psmPayload, rawPsm.getValidatorResults());

                // Set the schema version
                processedPsm.setSchemaVersion(1);
                RsuTypeIdKey key = new RsuTypeIdKey();
                key.setRsuId(processedPsm.getOriginIp());
                key.setPedestrianType(psmPayload.getPsm().getBasicType());
                key.setPsmId(psmPayload.getPsm().getId());

                return KeyValue.pair(key, processedPsm);
            } else {
                ProcessedPsm<Point> processedPsm =
                        createFailureProcessedPsm(rawPsm.getValidatorResults(), rawPsm.getFailedMessage());
                RsuTypeIdKey key = new RsuTypeIdKey();
                key.setPsmId("ERROR");
                return KeyValue.pair(key, processedPsm);
            }
        } catch (Exception e) {
            String errMsg = String.format("Exception converting ODE PSM to Processed PSM! Message: %s", e.getMessage());
            logger.error(errMsg, e);
            // KafkaStreams knows to remove null responses before allowing further steps from occurring
            RsuTypeIdKey key = new RsuTypeIdKey();
            key.setPsmId("ERROR");
            return KeyValue.pair(key, null);
        }
    }

    @Override
    public void close() {
        // Nothing to do here
    }

    @SuppressWarnings("unchecked")
    public ProcessedPsm<Point> createProcessedPsm(OdePsmMetadata metadata, OdePsmPayload payload,
            JsonValidatorResult validationMessages) {
        List<PsmFeature<Point>> psmFeatures = new ArrayList<>();
        psmFeatures.add(createPsmFeature(payload));

        ProcessedPsm<Point> processedPsm = new ProcessedPsm<Point>(psmFeatures.toArray(new PsmFeature[0]));
        processedPsm.setOdeReceivedAt(metadata.getOdeReceivedAt()); // ISO 8601: 2022-11-11T16:36:10.529530Z

        if (metadata.getOriginIp() != null && !metadata.getOriginIp().isEmpty())
            processedPsm.setOriginIp(metadata.getOriginIp());

        List<ProcessedValidationMessage> processedPsmValidationMessages = new ArrayList<ProcessedValidationMessage>();
        for (Exception exception : validationMessages.getExceptions()) {
            ProcessedValidationMessage object = new ProcessedValidationMessage();
            object.setMessage(exception.getMessage());
            object.setException(exception.getStackTrace().toString());
            processedPsmValidationMessages.add(object);
        }
        for (ValidationMessage vm : validationMessages.getValidationMessages()) {
            ProcessedValidationMessage object = new ProcessedValidationMessage();
            object.setMessage(vm.getMessage());
            object.setSchemaPath(vm.getSchemaPath());
            object.setJsonPath(vm.getPath());

            processedPsmValidationMessages.add(object);
        }

        ZonedDateTime odeDate = Instant.parse(metadata.getOdeReceivedAt()).atZone(ZoneId.of("UTC"));

        processedPsm.setValidationMessages(processedPsmValidationMessages);
        processedPsm.setTimeStamp(generateOffsetUTCTimestamp(odeDate, payload.getPsm().getSecMark()));

        return processedPsm;
    }

    public ProcessedPsm<Point> createFailureProcessedPsm(JsonValidatorResult validatorResult, String message) {
        ProcessedPsm<Point> processedPsm = new ProcessedPsm<Point>(null);
        ProcessedValidationMessage object = new ProcessedValidationMessage();
        List<ProcessedValidationMessage> processedPsmValidationMessages = new ArrayList<ProcessedValidationMessage>();

        ZonedDateTime utcDateTime = ZonedDateTime.now(ZoneOffset.UTC);

        object.setMessage(message);
        object.setException(ExceptionUtils.getStackTrace(validatorResult.getExceptions().get(0)));

        processedPsmValidationMessages.add(object);
        processedPsm.setValidationMessages(processedPsmValidationMessages);
        processedPsm.setTimeStamp(utcDateTime);

        return processedPsm;
    }

    public PsmFeature<Point> createPsmFeature(OdePsmPayload payload) {
        J2735PSM psm = payload.getPsm();

        // Create the Geometry Point
        double psmLong = psm.getPosition().getLongitude().doubleValue();
        double psmLat = psm.getPosition().getLatitude().doubleValue();
        Point psmPoint = new Point(psmLong, psmLat);

        // Create the PSM Properties using lombok all constructor args
        PsmProperties psmProps = new PsmProperties(psm.getBasicType(), psm.getId(), psm.getMsgCnt(), psm.getSecMark(),
                psm.getSpeed(), psm.getHeading());

        return new PsmFeature<Point>(null, psmPoint, psmProps);
    }

    public ZonedDateTime generateOffsetUTCTimestamp(ZonedDateTime odeReceivedAt, Integer secMark) {
        try {
            if (secMark != null) {
                int millis = (int) (secMark % 1000);
                int seconds = (int) (secMark / 1000);
                ZonedDateTime date = odeReceivedAt;
                if (secMark == 65535) {

                    // Return UTC time zero if the Zoned Date time is marked as unknown, UTC time zero chosen so that a
                    // null value can
                    // represent an empty field in the PSM. But 65535, can represent an intentionally unidentified
                    // field.
                    return ZonedDateTime.ofInstant(Instant.ofEpochMilli(0), ZoneId.of("UTC"));

                } else {
                    // If we are within 10 seconds of the next minute, and the timeMark is a large number, it probably
                    // means that the time
                    // rolled over before reception.
                    // In this case, subtract a minute from the odeReceivedAt so that the true time represents the
                    // minute in the past.
                    if (odeReceivedAt.getSecond() < 10 && secMark > 50000) {
                        date = date.minusMinutes(1);
                    }

                    date = date.withSecond(seconds);
                    date = date.withNano(0);
                    date = date.plus(millis, ChronoUnit.MILLIS);
                    return date;
                }


            } else {
                return null;
            }
        } catch (Exception e) {
            String errMsg = String.format(
                    "Failed to generateOffsetUTCTimestamp - PSMProcessedJsonConverter. Message: %s", e.getMessage());
            logger.error(errMsg, e);
            return null;
        }
    }
}
