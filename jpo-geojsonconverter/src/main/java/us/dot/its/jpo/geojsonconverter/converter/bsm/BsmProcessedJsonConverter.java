package us.dot.its.jpo.geojsonconverter.converter.bsm;

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
import us.dot.its.jpo.geojsonconverter.partitioner.RsuLogKey;
import us.dot.its.jpo.geojsonconverter.pojos.ProcessedValidationMessage;
import us.dot.its.jpo.geojsonconverter.pojos.geojson.bsm.BsmProperties;
import us.dot.its.jpo.geojsonconverter.pojos.geojson.bsm.DeserializedRawBsm;
import us.dot.its.jpo.geojsonconverter.pojos.geojson.bsm.ProcessedBsm;
import us.dot.its.jpo.geojsonconverter.validator.JsonValidatorResult;

import us.dot.its.jpo.ode.model.OdeBsmData;
import us.dot.its.jpo.ode.model.OdeBsmMetadata;
import us.dot.its.jpo.ode.model.OdeBsmPayload;
import us.dot.its.jpo.ode.plugin.j2735.J2735BsmCoreData;

public class BsmProcessedJsonConverter implements Transformer<Void, DeserializedRawBsm, KeyValue<RsuLogKey, ProcessedBsm<Point>>> {
    private static final Logger logger = LoggerFactory.getLogger(BsmProcessedJsonConverter.class);

    @Override
    public void init(ProcessorContext arg0) { }

    /**
     * Transform an ODE BSM POJO to Processed BSM POJO.
     * 
     * @param rawKey - Void type because ODE topics have no specified key
     * @param rawBsm - The raw POJO
     * @return A key value pair: the key a RsuLogKey containing the RSU IP address or the BSM log file name
     */
    @Override
    public KeyValue<RsuLogKey, ProcessedBsm<Point>> transform(Void rawKey, DeserializedRawBsm rawBsm) {
        try {
            if (!rawBsm.getValidationFailure()) {
                OdeBsmData rawValue = new OdeBsmData();
                rawValue.setMetadata(rawBsm.getOdeBsmData().getMetadata());
                OdeBsmMetadata bsmMetadata = (OdeBsmMetadata)rawValue.getMetadata();

                rawValue.setPayload(rawBsm.getOdeBsmData().getPayload());
                OdeBsmPayload bsmPayload = (OdeBsmPayload)rawValue.getPayload();

                ProcessedBsm<Point> processedBsm = createProcessedBsm(bsmMetadata, bsmPayload, rawBsm.getValidatorResults());

                // Set the schema version
                processedBsm.getProperties().setSchemaVersion(bsmMetadata.getSchemaVersion());
                RsuLogKey key = new RsuLogKey();
                key.setRsuId(bsmMetadata.getOriginIp());
                key.setLogId(bsmMetadata.getLogFileName());
                key.setBsmId(bsmPayload.getBsm().getCoreData().getId());

                return KeyValue.pair(key, processedBsm);
            } else {
                ProcessedBsm<Point> processedBsm = createFailureProcessedBsm(rawBsm.getValidatorResults(), rawBsm.getFailedMessage());
                RsuLogKey key = new RsuLogKey();
                key.setBsmId("ERROR");
                return KeyValue.pair(key, processedBsm);
            }
        } catch (Exception e) {
            String errMsg = String.format("Exception converting ODE BSM to Processed BSM! Message: %s", e.getMessage());
            logger.error(errMsg, e);
            // KafkaStreams knows to remove null responses before allowing further steps from occurring
            RsuLogKey key = new RsuLogKey();
            key.setBsmId("ERROR");
            return KeyValue.pair(key, null);
        }
    }

    @Override
    public void close() {
        // Nothing to do here
    }

    @SuppressWarnings("unchecked")
    public ProcessedBsm<Point> createProcessedBsm(OdeBsmMetadata metadata, OdeBsmPayload payload, JsonValidatorResult validationMessages) {

        ProcessedBsm<Point> processedBsm = createProcessedBsmGeometryAndProperties(payload);
        processedBsm.getProperties().setOdeReceivedAt(metadata.getOdeReceivedAt()); // ISO 8601: 2022-11-11T16:36:10.529530Z

        if (metadata.getOriginIp() != null && !metadata.getOriginIp().isEmpty())
            processedBsm.getProperties().setOriginIp(metadata.getOriginIp());
        if (metadata.getLogFileName() != null && !metadata.getLogFileName().isEmpty())
            processedBsm.getProperties().setLogName(metadata.getLogFileName());

        List<ProcessedValidationMessage> processedBsmValidationMessages = new ArrayList<ProcessedValidationMessage>();
        for (Exception exception : validationMessages.getExceptions()) {
            ProcessedValidationMessage object = new ProcessedValidationMessage();
            object.setMessage(exception.getMessage());
            object.setException(exception.getStackTrace().toString());
            processedBsmValidationMessages.add(object);
        }
        for (ValidationMessage vm : validationMessages.getValidationMessages()){
            ProcessedValidationMessage object = new ProcessedValidationMessage();
            object.setMessage(vm.getMessage());
            object.setSchemaPath(vm.getSchemaPath());
            object.setJsonPath(vm.getPath());

            processedBsmValidationMessages.add(object);
        }

        ZonedDateTime odeDate = Instant.parse(metadata.getOdeReceivedAt()).atZone(ZoneId.of("UTC"));

        processedBsm.getProperties().setValidationMessages(processedBsmValidationMessages);
        processedBsm.getProperties().setTimeStamp(generateOffsetUTCTimestamp(odeDate, payload.getBsm().getCoreData().getSecMark()));

        return processedBsm;
    }

    public ProcessedBsm<Point> createFailureProcessedBsm(JsonValidatorResult validatorResult, String message) {
        ProcessedBsm<Point> processedBsm = new ProcessedBsm<Point>(null, null, new BsmProperties());
        ProcessedValidationMessage object = new ProcessedValidationMessage();
        List<ProcessedValidationMessage> processedBsmValidationMessages = new ArrayList<ProcessedValidationMessage>();

        ZonedDateTime utcDateTime = ZonedDateTime.now(ZoneOffset.UTC);
        
        object.setMessage(message);
        object.setException(ExceptionUtils.getStackTrace(validatorResult.getExceptions().get(0)));

        processedBsmValidationMessages.add(object);
        processedBsm.getProperties().setValidationMessages(processedBsmValidationMessages);
        processedBsm.getProperties().setTimeStamp(utcDateTime);

        return processedBsm;
    }

    private ProcessedBsm<Point> createProcessedBsmGeometryAndProperties(OdeBsmPayload payload) {
        J2735BsmCoreData coreData = payload.getBsm().getCoreData();

        // Create the Geometry Point
        double bsmLong = coreData.getPosition().getLongitude().doubleValue();
        double bsmLat = coreData.getPosition().getLatitude().doubleValue();
        Point bsmPoint = new Point(bsmLong, bsmLat);

        // Create the BSM Properties
        BsmProperties bsmProps = new BsmProperties();
        bsmProps.setAccelSet(coreData.getAccelSet());
        bsmProps.setAccuracy(coreData.getAccuracy());
        bsmProps.setAngle(coreData.getAngle());
        bsmProps.setBrakes(coreData.getBrakes());
        bsmProps.setHeading(coreData.getHeading());
        bsmProps.setId(coreData.getId());
        bsmProps.setMsgCnt(coreData.getMsgCnt());
        bsmProps.setSecMark(coreData.getSecMark());
        bsmProps.setSize(coreData.getSize());
        bsmProps.setTransmission(coreData.getTransmission());

        return new ProcessedBsm<Point>(null, bsmPoint, bsmProps);
    }

    public ZonedDateTime generateOffsetUTCTimestamp(ZonedDateTime odeReceivedAt, Integer secMark){
        try {
            if (secMark != null){
                int millis = (int)(secMark % 1000);
                int seconds = (int)(secMark / 1000);
                ZonedDateTime date = odeReceivedAt;
                if(secMark == 65535){

                    // Return UTC time zero if the Zoned Date time is marked as unknown, UTC time zero chosen so that a null value can represent an empty field in the BSM. But 65535, can represent an intentionally unidentified field.
                    return ZonedDateTime.ofInstant(Instant.ofEpochMilli(0), ZoneId.of("UTC"));
                    
                }else{
                    // If we are within 10 seconds of the next minute, and the timeMark is a large number, it probably means that the time rolled over before reception.
                    // In this case, subtract a minute from the odeReceivedAt so that the true time represents the minute in the past. 
                    if(odeReceivedAt.getSecond() < 10 && secMark > 50000){
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
            String errMsg = String.format("Failed to generateOffsetUTCTimestamp - BSMProcessedJsonConverter. Message: %s", e.getMessage());
            logger.error(errMsg, e);
            return null;
        }               
    }
}
