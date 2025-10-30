package us.dot.its.jpo.geojsonconverter.converter.spat;

import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.mock;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.processor.ProcessorContext;
import org.junit.Before;
import org.junit.Test;

import com.networknt.schema.ValidationMessage;

import us.dot.its.jpo.geojsonconverter.partitioner.RsuIntersectionKey;
import us.dot.its.jpo.geojsonconverter.pojos.spat.DeserializedRawSpat;
import us.dot.its.jpo.geojsonconverter.pojos.spat.ProcessedSpat;
import us.dot.its.jpo.geojsonconverter.serialization.deserializers.JsonDeserializer;
import us.dot.its.jpo.geojsonconverter.validator.JsonValidatorResult;
import us.dot.its.jpo.ode.model.OdeMessageFrameData;

public class SpatProcessedJsonConverterTest {
    SpatProcessedJsonConverter spatProcessedJsonConverter;
    OdeMessageFrameData spatMF;

    @Before
    public void setup() throws IOException {
        String odeSpatJsonString = new String(Files.readAllBytes(Paths.get("src/test/resources/json/valid.spat.json")));
        try (JsonDeserializer<OdeMessageFrameData> odeSpatDeserializer =
                new JsonDeserializer<>(OdeMessageFrameData.class)) {
            spatMF = odeSpatDeserializer.deserialize("test-topic", odeSpatJsonString.getBytes());
        }
        spatProcessedJsonConverter = new SpatProcessedJsonConverter();
    }

    @Test
    public void testConstructor() {
        assertNotNull(spatProcessedJsonConverter);
    }

    @Test
    public void testInit() {
        ProcessorContext mockContext = mock(ProcessorContext.class);
        spatProcessedJsonConverter.init(mockContext);
        assertNotNull(spatProcessedJsonConverter);
    }

    @Test
    public void testTransformValidation() {
        JsonValidatorResult validatorResults = new JsonValidatorResult();
        Exception exception = new Exception("test_exception");
        validatorResults.addException(exception);

        DeserializedRawSpat deserializedRawSpat = new DeserializedRawSpat();
        deserializedRawSpat.setOdeSpatMessageFrameData(spatMF);
        deserializedRawSpat.setValidatorResults(validatorResults);

        KeyValue<RsuIntersectionKey, ProcessedSpat> processedSpat = spatProcessedJsonConverter.transform(null, null);
        assertNotNull(processedSpat.key);
        assertEquals("ERROR", processedSpat.key.getRsuId());
        assertNull(processedSpat.value);
    }

    @Test
    public void testTransformFailure() {
        JsonValidatorResult validatorResults = new JsonValidatorResult();
        Exception exception = new Exception("test_exception");
        validatorResults.addException(exception);
        List<ValidationMessage> validationMessages = new ArrayList<>();
        validatorResults.addValidationMessages(validationMessages);

        DeserializedRawSpat deserializedRawSpat = new DeserializedRawSpat();
        deserializedRawSpat.setValidationFailure(true);
        deserializedRawSpat.setValidatorResults(validatorResults);
        deserializedRawSpat.setFailedMessage("{");

        KeyValue<RsuIntersectionKey, ProcessedSpat> processedSpat =
                spatProcessedJsonConverter.transform(null, deserializedRawSpat);
        assertNotNull(processedSpat.key);
        assertNotNull(processedSpat.value);
        assertEquals("{", processedSpat.value.getValidationMessages().get(0).getMessage());
    }

    @Test
    public void testTransformException() {
        KeyValue<RsuIntersectionKey, ProcessedSpat> processedSpat = spatProcessedJsonConverter.transform(null, null);
        assertNotNull(processedSpat.key);
        assertEquals("ERROR", processedSpat.key.getRsuId());
        assertNull(processedSpat.value);
    }

    @Test
    public void testGenerateUTCTimestampMOY() {
        ZonedDateTime moyTime =
                spatProcessedJsonConverter.generateUTCTimestamp(481801L, 30000L, "2022-01-01T00:00:00Z");

        assertNotNull(moyTime);
        assertEquals("DECEMBER", moyTime.getMonth().toString());
        assertEquals(1, moyTime.getDayOfMonth());
    }

    @Test
    public void testGenerateUTCTimestampWithNullMoy() {
        ZonedDateTime testTime = ZonedDateTime.of(2025, 1, 1, 0, 0, 30, 0, ZoneId.of("UTC"));
        ZonedDateTime spatTime = spatProcessedJsonConverter.generateUTCTimestamp(null, 30000L,
                testTime.format(DateTimeFormatter.ISO_OFFSET_DATE_TIME));

        assertEquals(testTime, spatTime);
    }

    @Test
    public void testGenerateUTCTimestampWithNullDSecond() {
        ZonedDateTime testTime = ZonedDateTime.of(2025, 1, 1, 0, 0, 0, 0, ZoneId.of("UTC"));
        ZonedDateTime spatTime = spatProcessedJsonConverter.generateUTCTimestamp(481801L, null,
                testTime.format(DateTimeFormatter.ISO_OFFSET_DATE_TIME));

        assertEquals(testTime, spatTime);
    }

    @Test
    public void testClose() {
        // Should do nothing, but required override
        spatProcessedJsonConverter.close();
        assertNotNull(spatProcessedJsonConverter);
    }


}
