package us.dot.its.jpo.geojsonconverter.converter.map;

import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.mock;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.processor.ProcessorContext;
import org.junit.Before;
import org.junit.Test;

import com.networknt.schema.ValidationMessage;
import us.dot.its.jpo.asn.j2735.r2024.Common.MinuteOfTheYear;
import us.dot.its.jpo.geojsonconverter.partitioner.RsuIntersectionKey;
import us.dot.its.jpo.geojsonconverter.pojos.geojson.LineString;
import us.dot.its.jpo.geojsonconverter.pojos.geojson.map.DeserializedRawMap;
import us.dot.its.jpo.geojsonconverter.pojos.geojson.map.ProcessedMap;
import us.dot.its.jpo.geojsonconverter.serialization.deserializers.JsonDeserializer;
import us.dot.its.jpo.geojsonconverter.validator.JsonValidatorResult;
import us.dot.its.jpo.ode.model.OdeMessageFrameData;

@Slf4j
public class MapProcessedJsonConverterTest {
    MapProcessedJsonConverter mapProcessedJsonConverter;
    OdeMessageFrameData mapMF;
    DeserializedRawMap rawMap;

    @Before
    public void setup() throws IOException {
        String odeMapJsonString = new String(Files.readAllBytes(Paths.get("src/test/resources/json/valid.map.json")));

        try (JsonDeserializer<OdeMessageFrameData> odeMapDeserializer =
                new JsonDeserializer<>(OdeMessageFrameData.class)) {
            mapMF = odeMapDeserializer.deserialize("test-topic", odeMapJsonString.getBytes());
        }

        JsonValidatorResult validatorResults = new JsonValidatorResult();
        Exception exception = new Exception("test_exception");
        validatorResults.addException(exception);
        List<ValidationMessage> validationMessages = new ArrayList<>();
        validatorResults.addValidationMessages(validationMessages);

        rawMap = new DeserializedRawMap();
        rawMap.setOdeMapMessageFrameData(mapMF);
        rawMap.setValidatorResults(validatorResults);
        mapProcessedJsonConverter = new MapProcessedJsonConverter();
    }

    @Test
    public void testConstructor() {
        assertNotNull(mapProcessedJsonConverter);
    }

    @Test
    public void testInit() {
        ProcessorContext mockContext = mock(ProcessorContext.class);
        mapProcessedJsonConverter.init(mockContext);
        assertNotNull(mapProcessedJsonConverter);
    }

    @Test
    public void testTransform() {
        KeyValue<RsuIntersectionKey, ProcessedMap<LineString>> mapFeatureCollection =
                mapProcessedJsonConverter.transform(null, rawMap);
        log.info("mapFeatureCollection: {}", mapFeatureCollection);
        assertNotNull(mapFeatureCollection.key);
        assertEquals("172.18.0.1", mapFeatureCollection.key.getRsuId());
        assertEquals(12112, mapFeatureCollection.key.getIntersectionId());
        assertNotNull(mapFeatureCollection.value);
        assertEquals(27, mapFeatureCollection.value.getMapFeatureCollection().getFeatures().length);
    }

    @Test
    public void testTransformValidationFailure() {
        rawMap.setValidationFailure(true);
        rawMap.setFailedMessage("Failed to transform");
        KeyValue<RsuIntersectionKey, ProcessedMap<LineString>> mapFeatureCollection =
                mapProcessedJsonConverter.transform(null, rawMap);
        assertNotNull(mapFeatureCollection.key);
        assertEquals("ERROR", mapFeatureCollection.key.getRsuId());
        assertNotNull(mapFeatureCollection.value);
        assertEquals(1, mapFeatureCollection.value.getProperties().getValidationMessages().size());
    }

    @Test
    public void testTransformException() {
        KeyValue<RsuIntersectionKey, ProcessedMap<LineString>> mapFeatureCollection =
                mapProcessedJsonConverter.transform(null, null);
        assertNotNull(mapFeatureCollection.key);
        assertEquals("ERROR", mapFeatureCollection.key.getRsuId());
        assertNull(mapFeatureCollection.value);
    }

    @Test
    public void testGenerateUTCTimestampMOY() {
        ZonedDateTime odeReceivedAt = Instant.parse("2022-01-01T00:00:00Z").atZone(ZoneId.of("UTC"));
        MinuteOfTheYear moy = new MinuteOfTheYear(500000);
        ZonedDateTime moyTime = mapProcessedJsonConverter.generateUTCTimestamp(moy, odeReceivedAt);

        assertNotNull(moyTime);
        assertEquals("DECEMBER", moyTime.getMonth().toString());
    }

    @Test
    public void testClose() {
        // Should do nothing, but required override
        mapProcessedJsonConverter.close();
        assertNotNull(mapProcessedJsonConverter);
    }
}
