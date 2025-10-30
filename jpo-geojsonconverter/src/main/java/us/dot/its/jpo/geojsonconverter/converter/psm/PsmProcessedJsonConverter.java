package us.dot.its.jpo.geojsonconverter.converter.psm;

import com.networknt.schema.ValidationMessage;
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
import us.dot.its.jpo.asn.j2735.r2024.PersonalSafetyMessage.PersonalDeviceUserType;
import us.dot.its.jpo.geojsonconverter.pojos.geojson.Point;
import us.dot.its.jpo.geojsonconverter.pojos.geojson.psm.DeserializedRawPsm;
import us.dot.its.jpo.geojsonconverter.pojos.geojson.psm.ProcessedPersonalDeviceUserType;
import us.dot.its.jpo.geojsonconverter.pojos.geojson.psm.ProcessedPsm;
import us.dot.its.jpo.geojsonconverter.pojos.geojson.psm.PsmProperties;
import us.dot.its.jpo.asn.j2735.r2024.PersonalSafetyMessage.PersonalSafetyMessage;
import us.dot.its.jpo.asn.j2735.r2024.PersonalSafetyMessage.PersonalSafetyMessageMessageFrame;
import us.dot.its.jpo.geojsonconverter.converter.FieldConversions;
import us.dot.its.jpo.geojsonconverter.partitioner.RsuPsmIdKey;
import us.dot.its.jpo.geojsonconverter.pojos.ProcessedValidationMessage;
import us.dot.its.jpo.geojsonconverter.utils.ProcessedSchemaVersions;
import us.dot.its.jpo.geojsonconverter.validator.JsonValidatorResult;
import us.dot.its.jpo.ode.model.OdeMessageFrameData;
import us.dot.its.jpo.ode.model.OdeMessageFrameMetadata;

public class PsmProcessedJsonConverter
        implements Transformer<Void, DeserializedRawPsm, KeyValue<RsuPsmIdKey, ProcessedPsm<Point>>> {
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
    public KeyValue<RsuPsmIdKey, ProcessedPsm<Point>> transform(Void rawKey, DeserializedRawPsm rawPsm) {
        try {
            if (!rawPsm.isValidationFailure()) {
                OdeMessageFrameData rawValue = new OdeMessageFrameData();
                rawValue.setMetadata(rawPsm.getOdePsmMessageFrameData().getMetadata());
                OdeMessageFrameMetadata psmMetadata = rawValue.getMetadata();

                rawValue.setPayload(rawPsm.getOdePsmMessageFrameData().getPayload());
                PersonalSafetyMessageMessageFrame psmMessageFrame =
                        (PersonalSafetyMessageMessageFrame) rawValue.getPayload().getData();

                ProcessedPsm<Point> processedPsm =
                        createProcessedPsm(psmMetadata, psmMessageFrame, rawPsm.getValidatorResults());

                // Set the schema version
                processedPsm.getProperties().setSchemaVersion(ProcessedSchemaVersions.PROCESSED_PSM_SCHEMA_VERSION);
                RsuPsmIdKey key = new RsuPsmIdKey();
                key.setRsuId(psmMetadata.getOriginIp());
                key.setPsmId(psmMessageFrame.getValue().getId().getValue());

                return KeyValue.pair(key, processedPsm);
            } else {
                ProcessedPsm<Point> processedPsm =
                        createFailureProcessedPsm(rawPsm.getValidatorResults(), rawPsm.getFailedMessage());
                RsuPsmIdKey key = new RsuPsmIdKey();
                key.setPsmId("ERROR");
                return KeyValue.pair(key, processedPsm);
            }
        } catch (Exception e) {
            String errMsg = String.format("Exception converting ODE PSM to Processed PSM! Message: %s", e.getMessage());
            logger.error(errMsg, e);
            // KafkaStreams knows to remove null responses before allowing further steps from occurring
            RsuPsmIdKey key = new RsuPsmIdKey();
            key.setPsmId("ERROR");
            return KeyValue.pair(key, null);
        }
    }

    @Override
    public void close() {
        // Nothing to do here
    }

    public ProcessedPsm<Point> createProcessedPsm(OdeMessageFrameMetadata metadata,
            PersonalSafetyMessageMessageFrame psmMessageFrame, JsonValidatorResult validationMessages) {

        ProcessedPsm<Point> processedPsm = createProcessedPsmGeometryAndProperties(psmMessageFrame);
        // ISO 8601: 2022-11-11T16:36:10.529530Z
        processedPsm.getProperties().setOdeReceivedAt(metadata.getOdeReceivedAt());
        processedPsm.getProperties().setAsn1(metadata.getAsn1());
        if (metadata.getOriginIp() != null && !metadata.getOriginIp().isEmpty())
            processedPsm.getProperties().setOriginIp(metadata.getOriginIp());

        // Handle validation messages
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
        processedPsm.getProperties().setValidationMessages(processedPsmValidationMessages);

        ZonedDateTime odeDate = Instant.parse(metadata.getOdeReceivedAt()).atZone(ZoneId.of("UTC"));
        processedPsm.getProperties().setTimeStamp(
                generateOffsetUTCTimestamp(odeDate, (int) psmMessageFrame.getValue().getSecMark().getValue()));

        return processedPsm;
    }

    private ProcessedPsm<Point> createProcessedPsmGeometryAndProperties(
            PersonalSafetyMessageMessageFrame psmMessageFrame) {
        PersonalSafetyMessage psm = psmMessageFrame.getValue();

        // Create the Geometry Point
        Double psmLong = FieldConversions.convertLong(psm.getPosition().getLong_().getValue());
        Double psmLat = FieldConversions.convertLat(psm.getPosition().getLat().getValue());
        Point psmPoint = new Point(psmLong, psmLat);

        // Create the PSM Properties
        PsmProperties psmProps = new PsmProperties();
        PersonalDeviceUserType personalDeviceUserType = psm.getBasicType();
        if (personalDeviceUserType != null) {
            var processedPersonalDeviceUserType = ProcessedPersonalDeviceUserType.fromName(personalDeviceUserType.getName());
            psmProps.setBasicType(processedPersonalDeviceUserType);
        }
        psmProps.setId(psm.getId().getValue());
        psmProps.setMsgCnt((int) psm.getMsgCnt().getValue());
        psmProps.setSecMark((int) psm.getSecMark().getValue());
        psmProps.setSpeed(FieldConversions.convertSpeed(psm.getSpeed().getValue()));
        psmProps.setHeading(FieldConversions.convertHeading(psm.getHeading().getValue()));

        return new ProcessedPsm<Point>(null, psmPoint, psmProps);
    }

    public ProcessedPsm<Point> createFailureProcessedPsm(JsonValidatorResult validatorResult, String message) {
        ProcessedPsm<Point> processedPsm = new ProcessedPsm<Point>(null, null, new PsmProperties());
        ProcessedValidationMessage object = new ProcessedValidationMessage();
        List<ProcessedValidationMessage> processedPsmValidationMessages = new ArrayList<ProcessedValidationMessage>();

        ZonedDateTime utcDateTime = ZonedDateTime.now(ZoneOffset.UTC);

        object.setMessage(message);
        object.setException(ExceptionUtils.getStackTrace(validatorResult.getExceptions().get(0)));

        processedPsmValidationMessages.add(object);
        processedPsm.getProperties().setValidationMessages(processedPsmValidationMessages);
        processedPsm.getProperties().setTimeStamp(utcDateTime);

        return processedPsm;
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
