package us.dot.its.jpo.geojsonconverter.pojos.spat;

import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.Test;

import us.dot.its.jpo.ode.plugin.j2735.J2735AdvisorySpeedList;
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

        J2735AdvisorySpeedList speedList = new J2735AdvisorySpeedList();

        object.setSpeeds(speedList);
        J2735AdvisorySpeedList speedResponse = object.getSpeeds();
        assertEquals(speedResponse, speedList);
    }

    @Test
    public void testEquals() {
        MovementEvent object = new MovementEvent();
        MovementEvent otherObject = new MovementEvent();
        TimingChangeDetails timing = new TimingChangeDetails();
        otherObject.setTiming(timing);

        boolean equals = object.equals(object);
        assertEquals(equals, true);
        
        boolean otherEquals = object.equals(otherObject);
        assertEquals(otherEquals, false);

        String string = "string";
        boolean notEquals = otherObject.equals(string);
        assertEquals(notEquals, false);
    }


    @Test
    public void testHashCode() {
        MovementEvent object = new MovementEvent();

        Integer hash = object.hashCode();
        assertNotNull(hash);
    }

    @Test
    public void testToString() {
        MovementEvent object = new MovementEvent();

        String string = object.toString();
        assertNotNull(string);
    }
}
