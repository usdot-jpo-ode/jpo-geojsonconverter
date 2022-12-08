package us.dot.its.jpo.geojsonconverter.pojos.spat;

import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.Test;

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
        Boolean response;
        J2735IntersectionStatusObject status;

        status = J2735IntersectionStatusObject.MANUALCONTROLISENABLED;
        object.setStatus(status);
        response = object.getManualControlIsEnabled();
        assertEquals(response, true);

        status = J2735IntersectionStatusObject.STOPTIMEISACTIVATED;
        object.setStatus(status);
        response = object.getStopTimeIsActivated();
        assertEquals(response, true);

        status = J2735IntersectionStatusObject.FAILUREFLASH;
        object.setStatus(status);
        response = object.getFailureFlash();
        assertEquals(response, true);

        status = J2735IntersectionStatusObject.PREEMPTISACTIVE;
        object.setStatus(status);
        response = object.getPreemptIsActive();
        assertEquals(response, true);

        status = J2735IntersectionStatusObject.SIGNALPRIORITYISACTIVE;
        object.setStatus(status);
        response = object.getSignalPriorityIsActive();
        assertEquals(response, true);

        status = J2735IntersectionStatusObject.FIXEDTIMEOPERATION;
        object.setStatus(status);
        response = object.getFixedTimeOperation();
        assertEquals(response, true);

        status = J2735IntersectionStatusObject.TRAFFICDEPENDENTOPERATION;
        object.setStatus(status);
        response = object.getTrafficDependentOperation();
        assertEquals(response, true);

        status = J2735IntersectionStatusObject.STANDBYOPERATION;
        object.setStatus(status);
        response = object.getStandbyOperation();
        assertEquals(response, true);

        status = J2735IntersectionStatusObject.FAILUREMODE;
        object.setStatus(status);
        response = object.getFailureMode();
        assertEquals(response, true);

        status = J2735IntersectionStatusObject.OFF;
        object.setStatus(status);
        response = object.getOff();
        assertEquals(response, true);

        status = J2735IntersectionStatusObject.RECENTMAPMESSAGEUPDATE;
        object.setStatus(status);
        response = object.getRecentMAPmessageUpdate();
        assertEquals(response, true);

        status = J2735IntersectionStatusObject.RECENTCHANGEINMAPASSIGNEDLANESIDSUSED;
        object.setStatus(status);
        response = object.getRecentChangeInMAPassignedLanesIDsUsed();
        assertEquals(response, true);

        status = J2735IntersectionStatusObject.NOVALIDMAPISAVAILABLEATTHISTIME;
        object.setStatus(status);
        response = object.getNoValidMAPisAvailableAtThisTime();
        assertEquals(response, true);

        status = J2735IntersectionStatusObject.NOVALIDSPATISAVAILABLEATTHISTIME;
        object.setStatus(status);
        response = object.getNoValidSPATisAvailableAtThisTime();
        assertEquals(response, true);
    }

    @Test
    public void testEquals() {
        IntersectionStatusObject object = new IntersectionStatusObject();
        IntersectionStatusObject otherObject = new IntersectionStatusObject();
        otherObject.setFailureFlash(true);

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