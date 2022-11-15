package us.dot.its.jpo.geojsonconverter.pojos.spat;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.Test;

import us.dot.its.jpo.ode.plugin.j2735.J2735MovementPhaseState;

public class MovementEventTest {
    @Test
    public void testGettersSetters() {
        MovementEvent object = new MovementEvent();

        J2735MovementPhaseState phaseState = J2735MovementPhaseState.UNAVAILABLE;
        object.setEventState(phaseState);
        J2735MovementPhaseState stateResponse = object.getEventState();
        assertEquals(stateResponse, phaseState);

        TimingChangeDetails timing = new TimingChangeDetails();
        object.setTiming(timing);
        TimingChangeDetails timingResponse = object.getTiming();
        assertEquals(timingResponse, timing);

        object.setSpeeds(10.01);
        Double speedResponse = object.getSpeeds();
        assertEquals(speedResponse, 10.01);
    }
}
