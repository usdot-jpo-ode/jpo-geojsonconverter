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

        status = J2735IntersectionStatusObject.manualControlIsEnabled;
        object.setStatus(status);
        response = object.getManualControlIsEnabled();
        assertEquals(response, true);

        status = J2735IntersectionStatusObject.stopTimeIsActivated;
        object.setStatus(status);
        response = object.getStopTimeIsActivated();
        assertEquals(response, true);

        status = J2735IntersectionStatusObject.failureFlash;
        object.setStatus(status);
        response = object.getFailureFlash();
        assertEquals(response, true);

        status = J2735IntersectionStatusObject.preemptIsActive;
        object.setStatus(status);
        response = object.getPreemptIsActive();
        assertEquals(response, true);

        status = J2735IntersectionStatusObject.signalPriorityIsActive;
        object.setStatus(status);
        response = object.getSignalPriorityIsActive();
        assertEquals(response, true);

        status = J2735IntersectionStatusObject.fixedTimeOperation;
        object.setStatus(status);
        response = object.getFixedTimeOperation();
        assertEquals(response, true);

        status = J2735IntersectionStatusObject.trafficDependentOperation;
        object.setStatus(status);
        response = object.getTrafficDependentOperation();
        assertEquals(response, true);

        status = J2735IntersectionStatusObject.standbyOperation;
        object.setStatus(status);
        response = object.getStandbyOperation();
        assertEquals(response, true);

        status = J2735IntersectionStatusObject.failureMode;
        object.setStatus(status);
        response = object.getFailureMode();
        assertEquals(response, true);

        status = J2735IntersectionStatusObject.off;
        object.setStatus(status);
        response = object.getOff();
        assertEquals(response, true);

        status = J2735IntersectionStatusObject.recentMAPmessageUpdate;
        object.setStatus(status);
        response = object.getRecentMAPmessageUpdate();
        assertEquals(response, true);

        status = J2735IntersectionStatusObject.recentChangeInMAPassignedLanesIDsUsed;
        object.setStatus(status);
        response = object.getRecentChangeInMAPassignedLanesIDsUsed();
        assertEquals(response, true);

        status = J2735IntersectionStatusObject.noValidMAPisAvailableAtThisTime;
        object.setStatus(status);
        response = object.getNoValidMAPisAvailableAtThisTime();
        assertEquals(response, true);

        status = J2735IntersectionStatusObject.noValidSPATisAvailableAtThisTime;
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
        assertEquals(true, equals);
        
        boolean otherEquals = object.equals(otherObject);
        assertEquals(false, otherEquals);

        String string = "string";
        boolean notEquals = otherObject.equals(string);
        assertEquals(false, notEquals);
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