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
    String odeSpatJsonString = "{\"metadata\":{\"logFileName\":\"\",\"recordType\":\"spatTx\",\"securityResultCode\":\"success\",\"receivedMessageDetails\":{\"rxSource\":\"NA\"},\"payloadType\":\"us.dot.its.jpo.ode.model.OdeSpatPayload\",\"serialId\":{\"streamId\":\"9606fc7c-ca74-48e2-a2bb-bffb6edc718b\",\"bundleSize\":1,\"bundleId\":0,\"recordId\":0,\"serialNumber\":0},\"odeReceivedAt\":\"2022-12-19T19:24:08.843894Z\",\"schemaVersion\":6,\"maxDurationTime\":0,\"recordGeneratedAt\":\"\",\"sanitized\":false,\"odePacketID\":\"\",\"odeTimStartDateTime\":\"\",\"spatSource\":\"V2X\",\"originIp\":\"10.11.81.26\",\"isCertPresent\":false},\"payload\":{\"data\":{\"intersectionStateList\":{\"intersectionStatelist\":[{\"id\":{\"id\":12103},\"revision\":0,\"status\":{\"failureFlash\":false,\"noValidSPATisAvailableAtThisTime\":false,\"fixedTimeOperation\":false,\"standbyOperation\":false,\"trafficDependentOperation\":false,\"manualControlIsEnabled\":false,\"off\":false,\"stopTimeIsActivated\":false,\"recentChangeInMAPassignedLanesIDsUsed\":false,\"recentMAPmessageUpdate\":false,\"failureMode\":false,\"noValidMAPisAvailableAtThisTime\":false,\"signalPriorityIsActive\":false,\"preemptIsActive\":false},\"timeStamp\":8687,\"states\":{\"movementList\":[{\"signalGroup\":1,\"state_time_speed\":{\"movementEventList\":[{\"eventState\":\"STOP_AND_REMAIN\",\"timing\":{\"minEndTime\":14497,\"maxEndTime\":14497}}]}},{\"signalGroup\":2,\"state_time_speed\":{\"movementEventList\":[{\"eventState\":\"STOP_AND_REMAIN\",\"timing\":{\"minEndTime\":14957,\"maxEndTime\":14957}}]}},{\"signalGroup\":3,\"state_time_speed\":{\"movementEventList\":[{\"eventState\":\"STOP_AND_REMAIN\",\"timing\":{\"minEndTime\":14486,\"maxEndTime\":14486}}]}},{\"signalGroup\":4,\"state_time_speed\":{\"movementEventList\":[{\"eventState\":\"STOP_AND_REMAIN\",\"timing\":{\"minEndTime\":14737,\"maxEndTime\":14737}}]}},{\"signalGroup\":5,\"state_time_speed\":{\"movementEventList\":[{\"eventState\":\"STOP_AND_REMAIN\",\"timing\":{\"minEndTime\":14957,\"maxEndTime\":14957}}]}},{\"signalGroup\":6,\"state_time_speed\":{\"movementEventList\":[{\"eventState\":\"PROTECTED_MOVEMENT_ALLOWED\",\"timing\":{\"minEndTime\":14556,\"maxEndTime\":14556}}]}},{\"signalGroup\":7,\"state_time_speed\":{\"movementEventList\":[{\"eventState\":\"STOP_AND_REMAIN\",\"timing\":{\"minEndTime\":14617,\"maxEndTime\":14617}}]}},{\"signalGroup\":8,\"state_time_speed\":{\"movementEventList\":[{\"eventState\":\"STOP_AND_REMAIN\",\"timing\":{\"minEndTime\":14486,\"maxEndTime\":14486}}]}}]}}]}},\"dataType\":\"us.dot.its.jpo.ode.plugin.j2735.J2735SPAT\"}}";

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
            assertEquals("2022-12-19T19:24:08.843894Z", odeSpatMetadata.getOdeReceivedAt());
        }
    }

    @Test
    public void testDeserializePayload() {
        try (OdeSpatDataJsonDeserializer odeSpatDeserializer = new OdeSpatDataJsonDeserializer()) {
            OdeSpatPayload odeSpatPayload = (OdeSpatPayload)odeSpatDeserializer.deserialize("test-topic", odeSpatJsonString.getBytes()).getPayload();
            assertNotNull(odeSpatPayload);
            assertEquals(12103, odeSpatPayload.getSpat().getIntersectionStateList().getIntersectionStatelist().get(0).getId().getId());
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
