package us.dot.its.jpo.geojsonconverter.pojos.geojson.map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;

import us.dot.its.jpo.ode.plugin.j2735.J2735IntersectionReferenceID;

public class MapConnectionTest {

    @Test
    public void testLane() {
        Integer expectedLane = 1;
        MapConnection mapConnection = new MapConnection();
        mapConnection.setLane(expectedLane);
        assertEquals(expectedLane, mapConnection.getLane());
    }

    @Test
    public void testRemoteIntersection() {
        J2735IntersectionReferenceID expectedRemoteIntersection = new J2735IntersectionReferenceID();
        MapConnection mapConnection = new MapConnection();
        mapConnection.setRemoteIntersection(expectedRemoteIntersection);
        assertEquals(expectedRemoteIntersection, mapConnection.getRemoteIntersection());
    }
    
    @Test
    public void testSignalGroup() {
        Integer expectedSignalGroup = 1;
        MapConnection mapConnection = new MapConnection();
        mapConnection.setSignalGroup(expectedSignalGroup);
        assertEquals(expectedSignalGroup, mapConnection.getSignalGroup());
    }

    @Test
    public void testUserClass() {
        Integer expectedUserClass = 1;
        MapConnection mapConnection = new MapConnection();
        mapConnection.setUserClass(expectedUserClass);
        assertEquals(expectedUserClass, mapConnection.getUserClass());
    }

    @Test
    public void testConnectionID() {
        Integer expectedConnectionID = 1;
        MapConnection mapConnection = new MapConnection();
        mapConnection.setConnectionID(expectedConnectionID);
        assertEquals(expectedConnectionID, mapConnection.getConnectionID());
    }

    @Test
    public void testHashCode() {
        MapConnection mapConnection = new MapConnection();
        Integer hash = mapConnection.hashCode();
        assertNotNull(hash);
    }

    @Test
    public void testEquals() {
        MapConnection object = new MapConnection();
        MapConnection otherObject = new MapConnection();
        otherObject.setConnectionID(1);

        boolean equals = object.equals(object);
        assertEquals(equals, true);
        
        boolean otherEquals = object.equals(otherObject);
        assertEquals(otherEquals, false);

        String string = "string";
        boolean notEquals = otherObject.equals(string);
        assertEquals(notEquals, false);
    }
    
    @Test
    public void testToString() {
        MapConnection mapConnection = new MapConnection();
        String string = mapConnection.toString();
        assertNotNull(string);
    }
}
