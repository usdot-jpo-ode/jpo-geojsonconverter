package us.dot.its.jpo.geojsonconverter.converter.spat;

import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.mock;

import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.processor.ProcessorContext;
import org.junit.Before;
import org.junit.Test;

import us.dot.its.jpo.geojsonconverter.pojos.spat.ProcessedSpat;
import us.dot.its.jpo.geojsonconverter.serialization.deserializers.OdeSpatDataJsonDeserializer;
import us.dot.its.jpo.ode.model.OdeSpatData;

public class SpatProcessedJsonConverterTest {
    SpatProcessedJsonConverter spatProcessedJsonConverter;
    OdeSpatData odeSpatPojo;

    @Before
    public void setup() {
        String odeSpatJsonString = "{\"metadata\":{\"logFileName\":\"\",\"recordType\":\"spatTx\",\"securityResultCode\":\"success\",\"receivedMessageDetails\":{\"locationData\":null,\"rxSource\":\"NA\"},\"encodings\":null,\"payloadType\":\"us.dot.its.jpo.ode.model.OdeSpatPayload\",\"serialId\":{\"streamId\":\"7b68f216-4a47-4e7e-9376-59c1f8832a8e\",\"bundleSize\":1,\"bundleId\":0,\"recordId\":0,\"serialNumber\":0},\"odeReceivedAt\":\"2022-10-21T17:39:53.700315Z\",\"schemaVersion\":6,\"maxDurationTime\":0,\"recordGeneratedAt\":\"\",\"recordGeneratedBy\":null,\"sanitized\":false,\"odePacketID\":\"\",\"odeTimStartDateTime\":\"\",\"spatSource\":\"V2X\",\"originIp\":\"172.19.0.1\",\"isCertPresent\":false},\"payload\":{\"data\":{\"timeStamp\":null,\"name\":null,\"intersectionStateList\":{\"intersectionStatelist\":[{\"name\":null,\"id\":{\"region\":null,\"id\":12110},\"revision\":0,\"status\":\"MANUALCONTROLISENABLED\",\"moy\":null,\"timeStamp\":28744,\"enabledLanes\":null,\"states\":{\"movementList\":[{\"movementName\":null,\"signalGroup\":1,\"state_time_speed\":{\"movementEventList\":[{\"eventState\":\"STOP_AND_REMAIN\",\"timing\":{\"startTime\":null,\"minEndTime\":2281,\"maxEndTime\":2281,\"likelyTime\":null,\"confidence\":null,\"nextTime\":null},\"speeds\":null}]},\"maneuverAssistList\":null},{\"movementName\":null,\"signalGroup\":2,\"state_time_speed\":{\"movementEventList\":[{\"eventState\":\"STOP_AND_REMAIN\",\"timing\":{\"startTime\":null,\"minEndTime\":1851,\"maxEndTime\":1851,\"likelyTime\":null,\"confidence\":null,\"nextTime\":null},\"speeds\":null}]},\"maneuverAssistList\":null},{\"movementName\":null,\"signalGroup\":3,\"state_time_speed\":{\"movementEventList\":[{\"eventState\":\"STOP_AND_REMAIN\",\"timing\":{\"startTime\":null,\"minEndTime\":2281,\"maxEndTime\":2281,\"likelyTime\":null,\"confidence\":null,\"nextTime\":null},\"speeds\":null}]},\"maneuverAssistList\":null},{\"movementName\":null,\"signalGroup\":4,\"state_time_speed\":{\"movementEventList\":[{\"eventState\":\"STOP_AND_REMAIN\",\"timing\":{\"startTime\":null,\"minEndTime\":1581,\"maxEndTime\":1581,\"likelyTime\":null,\"confidence\":null,\"nextTime\":null},\"speeds\":null}]},\"maneuverAssistList\":null},{\"movementName\":null,\"signalGroup\":5,\"state_time_speed\":{\"movementEventList\":[{\"eventState\":\"STOP_AND_REMAIN\",\"timing\":{\"startTime\":null,\"minEndTime\":1851,\"maxEndTime\":1851,\"likelyTime\":null,\"confidence\":null,\"nextTime\":null},\"speeds\":null}]},\"maneuverAssistList\":null},{\"movementName\":null,\"signalGroup\":6,\"state_time_speed\":{\"movementEventList\":[{\"eventState\":\"STOP_AND_REMAIN\",\"timing\":{\"startTime\":null,\"minEndTime\":2001,\"maxEndTime\":2001,\"likelyTime\":null,\"confidence\":null,\"nextTime\":null},\"speeds\":null}]},\"maneuverAssistList\":null},{\"movementName\":null,\"signalGroup\":7,\"state_time_speed\":{\"movementEventList\":[{\"eventState\":\"PROTECTED_MOVEMENT_ALLOWED\",\"timing\":{\"startTime\":null,\"minEndTime\":1480,\"maxEndTime\":1601,\"likelyTime\":null,\"confidence\":null,\"nextTime\":null},\"speeds\":null}]},\"maneuverAssistList\":null},{\"movementName\":null,\"signalGroup\":8,\"state_time_speed\":{\"movementEventList\":[{\"eventState\":\"STOP_AND_REMAIN\",\"timing\":{\"startTime\":null,\"minEndTime\":1651,\"maxEndTime\":1651,\"likelyTime\":null,\"confidence\":null,\"nextTime\":null},\"speeds\":null}]},\"maneuverAssistList\":null}]},\"maneuverAssistList\":null}]}},\"dataType\":\"us.dot.its.jpo.ode.plugin.j2735.J2735SPAT\"}}";
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
        KeyValue<String, ProcessedSpat> processedSpat = spatProcessedJsonConverter.transform(null, odeSpatPojo);
        assertNotNull(processedSpat.key);
        assertEquals("172.19.0.1:12110", processedSpat.key);
        assertNotNull(processedSpat.value);
        assertEquals(8, processedSpat.value.getStates().size());
    }

    @Test
    public void testTransformException() {
        KeyValue<String, ProcessedSpat> processedSpat = spatProcessedJsonConverter.transform(null, null);
        assertNotNull(processedSpat.key);
        assertEquals("ERROR", processedSpat.key);
        assertNull(processedSpat.value);
    }

    @Test
    public void testClose() {
        // Should do nothing, but required override
        spatProcessedJsonConverter.close();
        assertNotNull(spatProcessedJsonConverter);
    }

    
}
