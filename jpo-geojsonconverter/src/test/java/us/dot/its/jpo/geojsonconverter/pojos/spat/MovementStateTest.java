package us.dot.its.jpo.geojsonconverter.pojos.spat;

import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

public class MovementStateTest {
    @Test
    public void testGettersSetters() {
        MovementState object = new MovementState();

        object.setMovementName("name");
        String nameResponse = object.getMovementName();
        assertEquals(nameResponse, "name");

        object.setSignalGroup(1);
        Integer sgResponse = object.getSignalGroup();
        assertEquals(sgResponse, 1);

        List<MovementEvent> movementEvent = new ArrayList<MovementEvent>();
        object.setStateTimeSpeed(movementEvent);
        List<MovementEvent> movementResponse = object.getStateTimeSpeed();
        assertEquals(movementResponse, movementEvent);
    }

    @Test
    public void testEquals() {
        MovementState object = new MovementState();
        MovementState otherObject = new MovementState();
        otherObject.setMovementName("name");

        boolean equals = object.equals(object);
        assertEquals(true, equals);
        
        boolean otherEquals = object.equals(otherObject);
        assertEquals(false, otherEquals);

        String string = "string";
        boolean notEquals = otherObject.equals(string);
        assertEquals(false, notEquals);
    }


    @Test
    public void testHashCode() {
        MovementState object = new MovementState();

        Integer hash = object.hashCode();
        assertNotNull(hash);
    }

    @Test
    public void testToString() {
        MovementState object = new MovementState();

        String string = object.toString();
        assertNotNull(string);
    }
}
