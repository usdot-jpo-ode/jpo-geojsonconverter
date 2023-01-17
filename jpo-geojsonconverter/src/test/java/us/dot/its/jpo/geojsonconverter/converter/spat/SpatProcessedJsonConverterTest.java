package us.dot.its.jpo.geojsonconverter.converter.spat;

import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.mock;

import java.time.ZonedDateTime;
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
import us.dot.its.jpo.geojsonconverter.serialization.deserializers.OdeSpatDataJsonDeserializer;
import us.dot.its.jpo.geojsonconverter.validator.JsonValidatorResult;
import us.dot.its.jpo.ode.model.OdeSpatData;

public class SpatProcessedJsonConverterTest {
    SpatProcessedJsonConverter spatProcessedJsonConverter;
    OdeSpatData odeSpatPojo;

    @Before
    public void setup() {
        String odeSpatJsonString = "{\"metadata\":{\"logFileName\":\"\",\"recordType\":\"spatTx\",\"securityResultCode\":\"success\",\"receivedMessageDetails\":{\"rxSource\":\"NA\"},\"payloadType\":\"us.dot.its.jpo.ode.model.OdeSpatPayload\",\"serialId\":{\"streamId\":\"9606fc7c-ca74-48e2-a2bb-bffb6edc718b\",\"bundleSize\":1,\"bundleId\":0,\"recordId\":0,\"serialNumber\":0},\"odeReceivedAt\":\"2022-12-19T19:24:08.843894Z\",\"schemaVersion\":6,\"maxDurationTime\":0,\"recordGeneratedAt\":\"\",\"sanitized\":false,\"odePacketID\":\"\",\"odeTimStartDateTime\":\"\",\"spatSource\":\"V2X\",\"originIp\":\"10.11.81.26\",\"isCertPresent\":false},\"payload\":{\"data\":{\"intersectionStateList\":{\"intersectionStatelist\":[{\"id\":{\"id\":12103},\"revision\":0,\"status\":{\"failureFlash\":true,\"noValidSPATisAvailableAtThisTime\":false,\"fixedTimeOperation\":false,\"standbyOperation\":false,\"trafficDependentOperation\":false,\"manualControlIsEnabled\":true,\"off\":false,\"stopTimeIsActivated\":false,\"recentChangeInMAPassignedLanesIDsUsed\":false,\"recentMAPmessageUpdate\":false,\"failureMode\":false,\"noValidMAPisAvailableAtThisTime\":false,\"signalPriorityIsActive\":false,\"preemptIsActive\":false},\"timeStamp\":8687,\"states\":{\"movementList\":[{\"signalGroup\":1,\"state_time_speed\":{\"movementEventList\":[{\"eventState\":\"STOP_AND_REMAIN\",\"timing\":{\"minEndTime\":14497,\"maxEndTime\":14497}}]}},{\"signalGroup\":2,\"state_time_speed\":{\"movementEventList\":[{\"eventState\":\"STOP_AND_REMAIN\",\"timing\":{\"minEndTime\":14957,\"maxEndTime\":14957}}]}},{\"signalGroup\":3,\"state_time_speed\":{\"movementEventList\":[{\"eventState\":\"STOP_AND_REMAIN\",\"timing\":{\"minEndTime\":14486,\"maxEndTime\":14486}}]}},{\"signalGroup\":4,\"state_time_speed\":{\"movementEventList\":[{\"eventState\":\"STOP_AND_REMAIN\",\"timing\":{\"minEndTime\":14737,\"maxEndTime\":14737}}]}},{\"signalGroup\":5,\"state_time_speed\":{\"movementEventList\":[{\"eventState\":\"STOP_AND_REMAIN\",\"timing\":{\"minEndTime\":14957,\"maxEndTime\":14957}}]}},{\"signalGroup\":6,\"state_time_speed\":{\"movementEventList\":[{\"eventState\":\"PROTECTED_MOVEMENT_ALLOWED\",\"timing\":{\"minEndTime\":14556,\"maxEndTime\":14556}}]}},{\"signalGroup\":7,\"state_time_speed\":{\"movementEventList\":[{\"eventState\":\"STOP_AND_REMAIN\",\"timing\":{\"minEndTime\":14617,\"maxEndTime\":14617}}]}},{\"signalGroup\":8,\"state_time_speed\":{\"movementEventList\":[{\"eventState\":\"STOP_AND_REMAIN\",\"timing\":{\"minEndTime\":14486,\"maxEndTime\":14486}}]}}]}}]}},\"dataType\":\"us.dot.its.jpo.ode.plugin.j2735.J2735SPAT\"}}";
        try (OdeSpatDataJsonDeserializer odeSpatDeserializer = new OdeSpatDataJsonDeserializer()) {
            odeSpatPojo = odeSpatDeserializer.deserialize("test-topic", odeSpatJsonString.getBytes());
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
    public void testTransform() {
        JsonValidatorResult validatorResults = new JsonValidatorResult();
        Exception exception = new Exception("test_exception");
        validatorResults.addException(exception);
        List<ValidationMessage> validationMessages = new ArrayList<>();
        validatorResults.addValidationMessages(validationMessages);

        DeserializedRawSpat deserializedRawSpat = new DeserializedRawSpat();
        deserializedRawSpat.setOdeSpatOdeSpatData(odeSpatPojo);
        deserializedRawSpat.setValidatorResults(validatorResults);

        KeyValue<RsuIntersectionKey, ProcessedSpat> processedSpat = spatProcessedJsonConverter.transform(null, deserializedRawSpat);
        assertNotNull(processedSpat.key);
        assertEquals("10.11.81.26", processedSpat.key.getRsuId());
        assertEquals(12103, processedSpat.key.getIntersectionId());
        assertNotNull(processedSpat.value);
        assertEquals(8, processedSpat.value.getStates().size());
        assertEquals("test_exception", processedSpat.value.getValidationMessages().get(0).getMessage());
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
        ZonedDateTime  moyTime = spatProcessedJsonConverter.generateUTCTimestamp(481801, 30000, "2022-01-01T00:00:00Z");

        assertNotNull(moyTime);
        assertEquals("DECEMBER", moyTime.getMonth().toString());
        assertEquals(1, moyTime.getDayOfMonth());
    }

    @Test
    public void testClose() {
        // Should do nothing, but required override
        spatProcessedJsonConverter.close();
        assertNotNull(spatProcessedJsonConverter);
    }

    
}
