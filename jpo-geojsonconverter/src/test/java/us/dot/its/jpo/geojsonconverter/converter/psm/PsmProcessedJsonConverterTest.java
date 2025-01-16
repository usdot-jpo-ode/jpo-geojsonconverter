package us.dot.its.jpo.geojsonconverter.converter.psm;

import static org.junit.Assert.assertNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.processor.ProcessorContext;
import org.junit.Before;
import org.junit.Test;

import com.networknt.schema.ValidationMessage;

import us.dot.its.jpo.geojsonconverter.partitioner.RsuTypeIdKey;
import us.dot.its.jpo.geojsonconverter.pojos.geojson.Point;
import us.dot.its.jpo.geojsonconverter.pojos.geojson.psm.DeserializedRawPsm;
import us.dot.its.jpo.geojsonconverter.pojos.geojson.psm.ProcessedPsm;
import us.dot.its.jpo.geojsonconverter.serialization.deserializers.JsonDeserializer;
import us.dot.its.jpo.geojsonconverter.validator.JsonValidatorResult;
import us.dot.its.jpo.ode.model.OdePsmData;
import us.dot.its.jpo.ode.plugin.j2735.J2735PersonalDeviceUserType;

public class PsmProcessedJsonConverterTest {
    PsmProcessedJsonConverter psmProcessedJsonConverter;
    OdePsmData odePsmPojo;
    private String odePsmJsonString;

    @Before
    public void setup() throws IOException {
        odePsmJsonString = new String(Files.readAllBytes(Paths.get("src/test/resources/json/valid.psm.json")));
        try (JsonDeserializer<OdePsmData> odePsmDeserializer = new JsonDeserializer<>(OdePsmData.class)) {
            odePsmPojo = odePsmDeserializer.deserialize("test-topic", odePsmJsonString.getBytes());
        }
        psmProcessedJsonConverter = new PsmProcessedJsonConverter();
    }

    @Test
    public void testConstructor() {
        assertNotNull(psmProcessedJsonConverter);
    }

    @Test
    public void testInit() {
        ProcessorContext mockContext = mock(ProcessorContext.class);
        psmProcessedJsonConverter.init(mockContext);
        assertNotNull(psmProcessedJsonConverter);
    }

    @Test
    public void testTransform() {
        JsonValidatorResult validatorResults = new JsonValidatorResult();
        DeserializedRawPsm deserializedRawPsm = new DeserializedRawPsm();
        deserializedRawPsm.setOdePsmData(odePsmPojo);
        deserializedRawPsm.setValidatorResults(validatorResults);

        KeyValue<RsuTypeIdKey, ProcessedPsm<Point>> processedPsm =
                psmProcessedJsonConverter.transform(null, deserializedRawPsm);
        assertNotNull(processedPsm.key);
        assertEquals(RsuTypeIdKey.builder().pedestrianType(J2735PersonalDeviceUserType.aPEDESTRIAN).rsuId("172.23.0.1")
                .psmId("24779D7E").build(), processedPsm.key);
        assertNotNull(processedPsm.value);
        assertEquals("24779D7E", processedPsm.value.getProperties().getId());
    }

    @Test
    public void testTransformException() {
        JsonValidatorResult validatorResults = new JsonValidatorResult();
        Exception exception = new Exception("test_exception");
        validatorResults.addException(exception);

        DeserializedRawPsm deserializedRawPsm = new DeserializedRawPsm();
        deserializedRawPsm.setOdePsmData(odePsmPojo);
        deserializedRawPsm.setValidatorResults(validatorResults);

        KeyValue<RsuTypeIdKey, ProcessedPsm<Point>> processedPsm = psmProcessedJsonConverter.transform(null, null);
        assertNotNull(processedPsm.key);
        assertEquals(new RsuTypeIdKey(null, null, "ERROR"), processedPsm.key);
        assertNull(processedPsm.value);
    }

    @Test
    public void testTransformFailure() {
        JsonValidatorResult validatorResults = new JsonValidatorResult();
        Exception exception = new Exception("test_exception");
        validatorResults.addException(exception);
        List<ValidationMessage> validationMessages = new ArrayList<>();
        validatorResults.addValidationMessages(validationMessages);

        DeserializedRawPsm deserializedRawPsm = new DeserializedRawPsm();
        deserializedRawPsm.setValidationFailure(true);
        deserializedRawPsm.setValidatorResults(validatorResults);
        deserializedRawPsm.setFailedMessage("{");

        KeyValue<RsuTypeIdKey, ProcessedPsm<Point>> processedPsm =
                psmProcessedJsonConverter.transform(null, deserializedRawPsm);
        assertNotNull(processedPsm.key);
        assertNotNull(processedPsm.value);
        assertEquals("{", processedPsm.value.getProperties().getValidationMessages().get(0).getMessage());
    }

    @Test
    public void testClose() {
        // Should do nothing, but required override
        psmProcessedJsonConverter.close();
        assertNotNull(psmProcessedJsonConverter);
    }
}
