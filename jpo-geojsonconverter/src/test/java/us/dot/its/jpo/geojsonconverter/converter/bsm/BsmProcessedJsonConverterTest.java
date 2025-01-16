package us.dot.its.jpo.geojsonconverter.converter.bsm;

import static org.junit.Assert.assertNull;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.Mockito.mock;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.processor.ProcessorContext;
import org.junit.Before;
import org.junit.Test;

import com.networknt.schema.ValidationMessage;

import us.dot.its.jpo.geojsonconverter.partitioner.RsuLogKey;
import us.dot.its.jpo.geojsonconverter.pojos.geojson.Point;
import us.dot.its.jpo.geojsonconverter.pojos.geojson.bsm.BsmProperties;
import us.dot.its.jpo.geojsonconverter.pojos.geojson.bsm.DeserializedRawBsm;
import us.dot.its.jpo.geojsonconverter.pojos.geojson.bsm.ProcessedBsm;
import us.dot.its.jpo.geojsonconverter.serialization.deserializers.JsonDeserializer;
import us.dot.its.jpo.geojsonconverter.validator.JsonValidatorResult;
import us.dot.its.jpo.ode.model.OdeBsmData;
import us.dot.its.jpo.ode.plugin.j2735.J2735BitString;

public class BsmProcessedJsonConverterTest {
    BsmProcessedJsonConverter bsmProcessedJsonConverter;
    OdeBsmData odeBsmPojo;

    @Before
    public void setup() {
        String odeBsmJsonString = "{\"metadata\":{\"bsmSource\":\"EV\",\"logFileName\":null,\"recordType\":\"bsmTx\",\"securityResultCode\":\"success\",\"receivedMessageDetails\":{\"locationData\":{\"latitude\":\"unavailable\",\"longitude\":\"unavailable\",\"elevation\":\"unavailable\",\"speed\":\"unavailable\",\"heading\":\"unavailable\"},\"rxSource\":\"RSU\"},\"payloadType\":\"us.dot.its.jpo.ode.model.OdeBsmPayload\",\"serialId\":{\"streamId\":\"d3a49df1-983a-4ab9-ab61-dcac5dacaed7\",\"bundleSize\":1,\"bundleId\":0,\"recordId\":0,\"serialNumber\":0},\"odeReceivedAt\":\"2024-08-12T12:32:03.811Z\",\"schemaVersion\":6,\"maxDurationTime\":0,\"recordGeneratedAt\":\"\",\"recordGeneratedBy\":\"OBU\",\"sanitized\":false,\"odePacketID\":\"\",\"odeTimStartDateTime\":\"\",\"originIp\":\"10.11.81.26\"},\"payload\":{\"data\":{\"coreData\":{\"msgCnt\":25,\"id\":\"12A7A951\",\"secMark\":2800,\"position\":{\"latitude\":40.5671913,\"longitude\":-105.0342901,\"elevation\":1505.9},\"accelSet\":{\"accelLat\":2001,\"accelLong\":0.00,\"accelVert\":-127,\"accelYaw\":0.00},\"accuracy\":{\"semiMajor\":5.00,\"semiMinor\":2.00,\"orientation\":0E-10},\"transmission\":\"UNAVAILABLE\",\"speed\":0.00,\"heading\":359.4000,\"brakes\":{\"wheelBrakes\":{\"leftFront\":false,\"rightFront\":false,\"unavailable\":true,\"leftRear\":false,\"rightRear\":false},\"traction\":\"unavailable\",\"abs\":\"unavailable\",\"scs\":\"unavailable\",\"brakeBoost\":\"unavailable\",\"auxBrakes\":\"unavailable\"},\"size\":{\"width\":208,\"length\":586}},\"partII\":[]},\"dataType\":\"us.dot.its.jpo.ode.plugin.j2735.J2735Bsm\"}}";
        try (JsonDeserializer<OdeBsmData> odeBsmDeserializer = new JsonDeserializer<>(OdeBsmData.class)) {
            odeBsmPojo = odeBsmDeserializer.deserialize("test-topic", odeBsmJsonString.getBytes());
        }
        bsmProcessedJsonConverter = new BsmProcessedJsonConverter();
    }

    @Test
    public void testConstructor() {
        assertNotNull(bsmProcessedJsonConverter);
    }

    @Test
    public void testInit() {
        ProcessorContext mockContext = mock(ProcessorContext.class);
        bsmProcessedJsonConverter.init(mockContext);
        assertNotNull(bsmProcessedJsonConverter);
    }

    @Test
    public void testTransform() {
        JsonValidatorResult validatorResults = new JsonValidatorResult();
        DeserializedRawBsm deserializedRawBsm = new DeserializedRawBsm();
        deserializedRawBsm.setOdeBsmData(odeBsmPojo);
        deserializedRawBsm.setValidatorResults(validatorResults);

        KeyValue<RsuLogKey, ProcessedBsm<Point>> processedBsm = bsmProcessedJsonConverter.transform(null, deserializedRawBsm);
        assertNotNull(processedBsm.key);
        assertEquals(new RsuLogKey("10.11.81.26", null, "12A7A951"), processedBsm.key);
        assertNotNull(processedBsm.value);
        var value = processedBsm.value;
        assertNotNull(value);
        BsmProperties props = value.getProperties();
        assertNotNull(props);
        assertEquals("12A7A951", props.getId());
        assertEquals(6, props.getSchemaVersion());
        assertEquals("BSM", props.getMessageType());
        assertEquals("2024-08-12T12:32:03.811Z", props.getOdeReceivedAt());
        assertNotNull(props.getTimeStamp());
        assertEquals("10.11.81.26", props.getOriginIp());
        assertNull(props.getLogName());
        var accelSet = props.getAccelSet();
        assertNotNull(accelSet);
        assertEquals(new BigDecimal(2001), accelSet.getAccelLat());
        assertEquals(0, new BigDecimal(0).compareTo(accelSet.getAccelLong()));
        assertEquals(new BigDecimal(-127), accelSet.getAccelVert());
        assertEquals(0, new BigDecimal(0).compareTo(accelSet.getAccelYaw()));
        var accuracy = props.getAccuracy();
        assertNotNull(accuracy);
        assertEquals(0, new BigDecimal(5).compareTo(accuracy.getSemiMajor()));
        assertEquals(0, new BigDecimal(2).compareTo(accuracy.getSemiMinor()));
        assertEquals(0, new BigDecimal(0).compareTo(accuracy.getOrientation()));
        var brakes = props.getBrakes();
        assertNotNull(brakes);
        assertEquals("unavailable", brakes.getTraction());
        assertEquals("unavailable", brakes.getAbs());
        assertEquals("unavailable", brakes.getScs());
        assertEquals("unavailable", brakes.getBrakeBoost());
        assertEquals("unavailable", brakes.getAuxBrakes());
        J2735BitString wheelBrakes = brakes.getWheelBrakes();
        assertNotNull(wheelBrakes);
        assertFalse(wheelBrakes.get("leftFront"));
        assertFalse(wheelBrakes.get("rightFront"));
        assertTrue(wheelBrakes.get("unavailable"));
        assertFalse(wheelBrakes.get("leftRear"));
        assertFalse(wheelBrakes.get("rightRear"));
        var geometry = value.getGeometry();
        assertNotNull(geometry);
        assertEquals("Point", geometry.getType());
        var coords = geometry.getCoordinates();
        assertNotNull(coords);
        assertEquals(2, coords.length);
        assertEquals(-105.0342901, coords[0]);
        assertEquals(40.5671913, coords[1]);
    }

    @Test
    public void testTransformException() {
        JsonValidatorResult validatorResults = new JsonValidatorResult();
        Exception exception = new Exception("test_exception");
        validatorResults.addException(exception);

        DeserializedRawBsm deserializedRawBsm = new DeserializedRawBsm();
        deserializedRawBsm.setOdeBsmData(odeBsmPojo);
        deserializedRawBsm.setValidatorResults(validatorResults);

        KeyValue<RsuLogKey, ProcessedBsm<Point>> processedBsm = bsmProcessedJsonConverter.transform(null, null);
        assertNotNull(processedBsm.key);
        assertEquals(new RsuLogKey(null, null, "ERROR"), processedBsm.key);
        assertNull(processedBsm.value);
    }

    @Test
    public void testTransformFailure() {
        JsonValidatorResult validatorResults = new JsonValidatorResult();
        Exception exception = new Exception("test_exception");
        validatorResults.addException(exception);
        List<ValidationMessage> validationMessages = new ArrayList<>();
        validatorResults.addValidationMessages(validationMessages);

        DeserializedRawBsm deserializedRawBsm = new DeserializedRawBsm();
        deserializedRawBsm.setValidationFailure(true);
        deserializedRawBsm.setValidatorResults(validatorResults);
        deserializedRawBsm.setFailedMessage("{");

        KeyValue<RsuLogKey, ProcessedBsm<Point>> processedBsm = bsmProcessedJsonConverter.transform(null, deserializedRawBsm);
        assertNotNull(processedBsm.key);
        assertNotNull(processedBsm.value);
        assertEquals("{", processedBsm.value.getProperties().getValidationMessages().get(0).getMessage());
    }

    @Test
    public void testClose() {
        // Should do nothing, but required override
        bsmProcessedJsonConverter.close();
        assertNotNull(bsmProcessedJsonConverter);
    }
}
