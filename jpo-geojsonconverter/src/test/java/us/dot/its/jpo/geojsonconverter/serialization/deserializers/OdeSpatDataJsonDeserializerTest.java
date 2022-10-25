package us.dot.its.jpo.geojsonconverter.serialization.deserializers;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.Test;

import us.dot.its.jpo.ode.model.OdeSpatData;
import us.dot.its.jpo.ode.model.OdeSpatMetadata;
import us.dot.its.jpo.ode.model.OdeSpatPayload;

public class OdeSpatDataJsonDeserializerTest {
    String odeSpatJsonString = "{\"metadata\":{\"logFileName\":\"\",\"recordType\":\"spatTx\",\"securityResultCode\":\"success\",\"receivedMessageDetails\":{\"locationData\":null,\"rxSource\":\"NA\"},\"encodings\":null,\"payloadType\":\"us.dot.its.jpo.ode.model.OdeSpatPayload\",\"serialId\":{\"streamId\":\"7b68f216-4a47-4e7e-9376-59c1f8832a8e\",\"bundleSize\":1,\"bundleId\":0,\"recordId\":0,\"serialNumber\":0},\"odeReceivedAt\":\"2022-10-21T17:39:53.700315Z\",\"schemaVersion\":6,\"maxDurationTime\":0,\"recordGeneratedAt\":\"\",\"recordGeneratedBy\":null,\"sanitized\":false,\"odePacketID\":\"\",\"odeTimStartDateTime\":\"\",\"spatSource\":\"V2X\",\"originIp\":\"172.19.0.1\",\"isCertPresent\":false},\"payload\":{\"data\":{\"timeStamp\":null,\"name\":null,\"intersectionStateList\":{\"intersectionStatelist\":[{\"name\":null,\"id\":{\"region\":null,\"id\":12110},\"revision\":0,\"status\":\"MANUALCONTROLISENABLED\",\"moy\":null,\"timeStamp\":28744,\"enabledLanes\":null,\"states\":{\"movementList\":[{\"movementName\":null,\"signalGroup\":1,\"state_time_speed\":{\"movementEventList\":[{\"eventState\":\"STOP_AND_REMAIN\",\"timing\":{\"startTime\":null,\"minEndTime\":2281,\"maxEndTime\":2281,\"likelyTime\":null,\"confidence\":null,\"nextTime\":null},\"speeds\":null}]},\"maneuverAssistList\":null},{\"movementName\":null,\"signalGroup\":2,\"state_time_speed\":{\"movementEventList\":[{\"eventState\":\"STOP_AND_REMAIN\",\"timing\":{\"startTime\":null,\"minEndTime\":1851,\"maxEndTime\":1851,\"likelyTime\":null,\"confidence\":null,\"nextTime\":null},\"speeds\":null}]},\"maneuverAssistList\":null},{\"movementName\":null,\"signalGroup\":3,\"state_time_speed\":{\"movementEventList\":[{\"eventState\":\"STOP_AND_REMAIN\",\"timing\":{\"startTime\":null,\"minEndTime\":2281,\"maxEndTime\":2281,\"likelyTime\":null,\"confidence\":null,\"nextTime\":null},\"speeds\":null}]},\"maneuverAssistList\":null},{\"movementName\":null,\"signalGroup\":4,\"state_time_speed\":{\"movementEventList\":[{\"eventState\":\"STOP_AND_REMAIN\",\"timing\":{\"startTime\":null,\"minEndTime\":1581,\"maxEndTime\":1581,\"likelyTime\":null,\"confidence\":null,\"nextTime\":null},\"speeds\":null}]},\"maneuverAssistList\":null},{\"movementName\":null,\"signalGroup\":5,\"state_time_speed\":{\"movementEventList\":[{\"eventState\":\"STOP_AND_REMAIN\",\"timing\":{\"startTime\":null,\"minEndTime\":1851,\"maxEndTime\":1851,\"likelyTime\":null,\"confidence\":null,\"nextTime\":null},\"speeds\":null}]},\"maneuverAssistList\":null},{\"movementName\":null,\"signalGroup\":6,\"state_time_speed\":{\"movementEventList\":[{\"eventState\":\"STOP_AND_REMAIN\",\"timing\":{\"startTime\":null,\"minEndTime\":2001,\"maxEndTime\":2001,\"likelyTime\":null,\"confidence\":null,\"nextTime\":null},\"speeds\":null}]},\"maneuverAssistList\":null},{\"movementName\":null,\"signalGroup\":7,\"state_time_speed\":{\"movementEventList\":[{\"eventState\":\"PROTECTED_MOVEMENT_ALLOWED\",\"timing\":{\"startTime\":null,\"minEndTime\":1480,\"maxEndTime\":1601,\"likelyTime\":null,\"confidence\":null,\"nextTime\":null},\"speeds\":null}]},\"maneuverAssistList\":null},{\"movementName\":null,\"signalGroup\":8,\"state_time_speed\":{\"movementEventList\":[{\"eventState\":\"STOP_AND_REMAIN\",\"timing\":{\"startTime\":null,\"minEndTime\":1651,\"maxEndTime\":1651,\"likelyTime\":null,\"confidence\":null,\"nextTime\":null},\"speeds\":null}]},\"maneuverAssistList\":null}]},\"maneuverAssistList\":null}]}},\"dataType\":\"us.dot.its.jpo.ode.plugin.j2735.J2735SPAT\"}}";

    @Test
    public void testConstructor() {
        OdeSpatDataJsonDeserializer odeSpatDeserializer = new OdeSpatDataJsonDeserializer();
        assertNotNull(odeSpatDeserializer);
    }

    @Test
    public void testDeserializeMetadata() {
        try (OdeSpatDataJsonDeserializer odeSpatDeserializer = new OdeSpatDataJsonDeserializer()) {
            OdeSpatMetadata odeSpatMetadata = (OdeSpatMetadata)odeSpatDeserializer.deserialize("test-topic", odeSpatJsonString.getBytes()).getMetadata();
            assertNotNull(odeSpatMetadata);
            assertEquals("2022-10-21T17:39:53.700315Z", odeSpatMetadata.getOdeReceivedAt());
        }
    }

    @Test
    public void testDeserializePayload() {
        try (OdeSpatDataJsonDeserializer odeSpatDeserializer = new OdeSpatDataJsonDeserializer()) {
            OdeSpatPayload odeSpatPayload = (OdeSpatPayload)odeSpatDeserializer.deserialize("test-topic", odeSpatJsonString.getBytes()).getPayload();
            assertNotNull(odeSpatPayload);
            assertEquals(12110, odeSpatPayload.getSpat().getIntersectionStateList().getIntersectionStatelist().get(0).getId().getId());
        }
    }

    @Test
    public void testDeserializeNull() {
        try (OdeSpatDataJsonDeserializer odeSpatDeserializer = new OdeSpatDataJsonDeserializer()) {
            OdeSpatData odeSpatData = odeSpatDeserializer.deserialize("test-topic", null);
            assertNull(odeSpatData);
        }
    }

    @Test
    public void testDeserializeError() {
        String malformedJson = "{\"test\":";
        try (OdeSpatDataJsonDeserializer odeSpatDeserializer = new OdeSpatDataJsonDeserializer()) {
            RuntimeException thrown = assertThrows(RuntimeException.class, () -> odeSpatDeserializer.deserialize("test-topic", malformedJson.getBytes()));
            assertTrue(thrown.getMessage().contains("Exception deserializing for topic test-topic"));
        }
    }
}
