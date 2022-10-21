package us.dot.its.jpo.geojsonconverter.geojson.spat;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.Test;

public class SpatMovementEventTest {
    @Test
    public void testEventState() {
        String expectedEventState = "TEST_STATE";
        SpatMovementEvent spatMovementEvent = new SpatMovementEvent();
        spatMovementEvent.setEventState(expectedEventState);
        assertEquals(expectedEventState, spatMovementEvent.getEventState());
    }

    @Test
    public void testStartTime() {
        Integer expectedStartTime = 4000;
        SpatMovementEvent spatMovementEvent = new SpatMovementEvent();
        spatMovementEvent.setStartTime(expectedStartTime);
        assertEquals(expectedStartTime, spatMovementEvent.getStartTime());
    }

    @Test
    public void testMinEndTime() {
        Integer expectedMinEndTime = 4000;
        SpatMovementEvent spatMovementEvent = new SpatMovementEvent();
        spatMovementEvent.setMinEndTime(expectedMinEndTime);
        assertEquals(expectedMinEndTime, spatMovementEvent.getMinEndTime());
    }

    @Test
    public void testMaxEndTime() {
        Integer expectedMaxEndTime = 4000;
        SpatMovementEvent spatMovementEvent = new SpatMovementEvent();
        spatMovementEvent.setMaxEndTime(expectedMaxEndTime);
        assertEquals(expectedMaxEndTime, spatMovementEvent.getMaxEndTime());
    }
}
