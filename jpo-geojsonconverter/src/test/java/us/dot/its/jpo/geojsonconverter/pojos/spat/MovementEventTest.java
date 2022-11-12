package us.dot.its.jpo.geojsonconverter.pojos.spat;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.Test;

public class MovementEventTest {
    @Test
    public void testGettersSetters() {
        MovementEvent object = new MovementEvent();

        object.setEventState("state");
        String stateResponse = object.getEventState();
        assertEquals(stateResponse, "state");

        TimingChangeDetails timing = new TimingChangeDetails();
        object.setTiming(timing);
        TimingChangeDetails timingResponse = object.getTiming();
        assertEquals(timingResponse, timing);

        object.setSpeeds(10.01);
        Double speedResponse = object.getSpeeds();
        assertEquals(speedResponse, 10.01);
    }
}
