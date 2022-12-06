package us.dot.its.jpo.geojsonconverter.pojos.geojson.connectinglanes;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;

public class ConnectingLanesPropertiesTest {
    @Test
    public void testSignalGroupId() {
        Integer expectedSignalGroupId = 1;
        ConnectingLanesProperties connectingLanesProperties = new ConnectingLanesProperties();
        connectingLanesProperties.setSignalGroupId(expectedSignalGroupId);
        assertEquals(expectedSignalGroupId, connectingLanesProperties.getSignalGroupId());
    }

    @Test
    public void testIngressLaneId() {
        Integer expectedIngressLaneId = 1;
        ConnectingLanesProperties connectingLanesProperties = new ConnectingLanesProperties();
        connectingLanesProperties.setIngressLaneId(expectedIngressLaneId);
        assertEquals(expectedIngressLaneId, connectingLanesProperties.getIngressLaneId());
    }

    @Test
    public void testEgressLaneId() {
        Integer expectedEgressLaneId = 1;
        ConnectingLanesProperties connectingLanesProperties = new ConnectingLanesProperties();
        connectingLanesProperties.setEgressLaneId(expectedEgressLaneId);
        assertEquals(expectedEgressLaneId, connectingLanesProperties.getEgressLaneId());
    }

    @Test
    public void testEquals() {
        ConnectingLanesProperties object = new ConnectingLanesProperties();
        ConnectingLanesProperties otherObject = new ConnectingLanesProperties();
        boolean equals = object.equals(object);
        assertEquals(equals, true);
        
        boolean otherEquals = object.equals(otherObject);
        assertEquals(otherEquals, true);

        String string = "string";
        boolean notEquals = otherObject.equals(string);
        assertEquals(notEquals, false);
    }
    
    @Test
    public void testHashCode() {
        ConnectingLanesProperties connectingLanesProperties = new ConnectingLanesProperties();
        Integer hash = connectingLanesProperties.hashCode();
        assertNotNull(hash);
    }
    
    @Test
    public void testToString() {
        ConnectingLanesProperties connectingLanesProperties = new ConnectingLanesProperties();
        String string = connectingLanesProperties.toString();
        assertNotNull(string);
    }
}
