package us.dot.its.jpo.geojsonconverter.geojson.spat;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.Test;

public class SpatPropertiesTest {
    @Test
    public void testSignalGroupId() {
        Integer expectedSignalGroupId = 3;
        SpatProperties spatProperties = new SpatProperties();
        spatProperties.setSignalGroupId(expectedSignalGroupId);
        assertEquals(expectedSignalGroupId, spatProperties.getSignalGroupId());
    }

    @Test
    public void testIp() {
        String expectedIp = "10.0.0.1";
        SpatProperties spatProperties = new SpatProperties();
        spatProperties.setIp(expectedIp);
        assertEquals(expectedIp, spatProperties.getIp());
    }

    @Test
    public void testOdeReceivedAt() {
        String expectedOdeReceivedAt = "testTimestamp";
        SpatProperties spatProperties = new SpatProperties();
        spatProperties.setOdeReceivedAt(expectedOdeReceivedAt);
        assertEquals(expectedOdeReceivedAt, spatProperties.getOdeReceivedAt());
    }

    @Test
    public void testTimestamp() {
        Integer expectedTimestamp = 30200;
        SpatProperties spatProperties = new SpatProperties();
        spatProperties.setTimestamp(expectedTimestamp);
        assertEquals(expectedTimestamp, spatProperties.getTimestamp());
    }

    @Test
    public void testMovementEvents() {
        SpatMovementEvent movementEvent = new SpatMovementEvent();
        movementEvent.setEventState("TEST_STATE");
        movementEvent.setStartTime(4000);
        movementEvent.setMinEndTime(12000);
        movementEvent.setMaxEndTime(15000);
        SpatMovementEvent[] movementEventList = new SpatMovementEvent[] { movementEvent };

        SpatProperties spatProperties = new SpatProperties();
        spatProperties.setMovementEvents(movementEventList);
        assertNotNull(spatProperties.getMovementEvents());
        assertEquals(movementEvent.getEventState(), spatProperties.getMovementEvents()[0].getEventState());
    }
}
