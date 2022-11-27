package us.dot.its.jpo.geojsonconverter.pojos.spat;

import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.Test;

import us.dot.its.jpo.ode.plugin.j2735.J2735BitString;
import us.dot.its.jpo.ode.plugin.j2735.J2735IntersectionStatusObject;

public class IntersectionStatusObjectTest {
    @Test
    public void testGettersSetters() {
        IntersectionStatusObject object = new IntersectionStatusObject();
        Boolean response;

        object.setManualControlIsEnabled(true);
        response = object.getManualControlIsEnabled();
        assertEquals(response, true);

        object.setStopTimeIsActivated(true);
        response = object.getStopTimeIsActivated();
        assertEquals(response, true);

        object.setFailureFlash(true);
        response = object.getFailureFlash();
        assertEquals(response, true);

        object.setPreemptIsActive(true);
        response = object.getPreemptIsActive();
        assertEquals(response, true);
        
        object.setSignalPriorityIsActive(true);
        response = object.getSignalPriorityIsActive();
        assertEquals(response, true);

        object.setFixedTimeOperation(true);
        response = object.getFixedTimeOperation();
        assertEquals(response, true);

        object.setTrafficDependentOperation(true);
        response = object.getTrafficDependentOperation();
        assertEquals(response, true);

        object.setStandbyOperation(true);
        response = object.getStandbyOperation();
        assertEquals(response, true);

        object.setOff(true);
        response = object.getOff();
        assertEquals(response, true);

        object.setRecentMAPmessageUpdate(true);
        response = object.getRecentMAPmessageUpdate();
        assertEquals(response, true);

        object.setRecentChangeInMAPassignedLanesIDsUsed(true);
        response = object.getRecentChangeInMAPassignedLanesIDsUsed();
        assertEquals(response, true);

        object.setNoValidMAPisAvailableAtThisTime(true);
        response = object.getNoValidMAPisAvailableAtThisTime();
        assertEquals(response, true);

        object.setNoValidSPATisAvailableAtThisTime(true);
        response = object.getNoValidSPATisAvailableAtThisTime();
        assertEquals(response, true);
    }

    @Test
    public void testSetStatus() {
        IntersectionStatusObject object = new IntersectionStatusObject();
        
        // Set all status bits to true
        var status = new J2735BitString();
        for (var enumValue : J2735IntersectionStatusObject.values()) {
            status.put(enumValue.toString(), true);
        }
        
        object.setStatus(status);
        
        assertEquals(object.getManualControlIsEnabled(), true);
        assertEquals(object.getStopTimeIsActivated(), true);
        assertEquals(object.getFailureFlash(), true);
        assertEquals(object.getPreemptIsActive(), true);
        assertEquals(object.getSignalPriorityIsActive(), true);
        assertEquals(object.getFixedTimeOperation(), true);
        assertEquals(object.getTrafficDependentOperation(), true);
        assertEquals(object.getStandbyOperation(), true);
        assertEquals(object.getFailureMode(), true);
        assertEquals(object.getOff(), true);
        assertEquals(object.getRecentMAPmessageUpdate(), true);
        assertEquals(object.getRecentChangeInMAPassignedLanesIDsUsed(), true);
        assertEquals(object.getNoValidMAPisAvailableAtThisTime(), true);
        assertEquals(object.getNoValidSPATisAvailableAtThisTime(), true);

        // Test that setting a null status resets all properties to false
        object.setStatus(null);
        assertEquals(object.getManualControlIsEnabled(), false);
        assertEquals(object.getStopTimeIsActivated(), false);
        assertEquals(object.getFailureFlash(), false);
        assertEquals(object.getPreemptIsActive(), false);
        assertEquals(object.getSignalPriorityIsActive(), false);
        assertEquals(object.getFixedTimeOperation(), false);
        assertEquals(object.getTrafficDependentOperation(), false);
        assertEquals(object.getStandbyOperation(), false);
        assertEquals(object.getFailureMode(), false);
        assertEquals(object.getOff(), false);
        assertEquals(object.getRecentMAPmessageUpdate(), false);
        assertEquals(object.getRecentChangeInMAPassignedLanesIDsUsed(), false);
        assertEquals(object.getNoValidMAPisAvailableAtThisTime(), false);
        assertEquals(object.getNoValidSPATisAvailableAtThisTime(), false);
    }

    
    @Test
    public void testEquals() {
        IntersectionStatusObject object = new IntersectionStatusObject();
        IntersectionStatusObject otherObject = new IntersectionStatusObject();
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
        IntersectionStatusObject object = new IntersectionStatusObject();

        Integer hash = object.hashCode();
        assertNotNull(hash);
    }

    @Test
    public void testToString() {
        IntersectionStatusObject object = new IntersectionStatusObject();

        String string = object.toString();
        assertNotNull(string);
    }
}