package us.dot.its.jpo.geojsonconverter.pojos.spat;

import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

public class ProcessedSpatTest {
    @Test
    public void testGettersSetters() {
        ProcessedSpat object = new ProcessedSpat();

        object.setMessageType("type");
        String typeResponse = object.getMessageType();
        assertEquals(typeResponse, "type");

        object.setOdeReceivedAt("receivedAt");
        String oderaResponse = object.getOdeReceivedAt();
        assertEquals(oderaResponse, "receivedAt");

        object.setOriginIp("ip");
        String ipResponse = object.getOriginIp();
        assertEquals(ipResponse, "ip");

        object.setName("name");
        String nameResponse = object.getName();
        assertEquals(nameResponse, "name");

        object.setRegion(1);
        Integer regionResponse = object.getRegion();
        assertEquals(regionResponse, 1);

        object.setIntersectionId(1);
        Integer intersectionResponse = object.getIntersectionId();
        assertEquals(intersectionResponse, 1);

        object.setCti4501Conformant(true);
        Boolean ctiResponse = object.getCti4501Conformant();
        assertEquals(ctiResponse, true);

        List<ProcessedSpatValidationMessage> validationMessages = new ArrayList<ProcessedSpatValidationMessage>();
        object.setValidationMessages(validationMessages);
        List<ProcessedSpatValidationMessage> vmResponse = object.getValidationMessages();
        assertEquals(vmResponse, validationMessages);

        object.setRevision(1);
        Integer revisionResponse = object.getRevision();
        assertEquals(revisionResponse, 1);

        IntersectionStatusObject statusObject = new IntersectionStatusObject();
        object.setStatus(statusObject);
        IntersectionStatusObject statusResponse = object.getStatus();
        assertEquals(statusResponse, statusObject);

        object.setUtcTimeStamp("ts");
        String tsResponse = object.getUtcTimeStamp();
        assertEquals(tsResponse, "ts");

        List<Integer> enabledLanes = new ArrayList<Integer>();
        object.setEnabledLanes(enabledLanes);
        List<Integer> laneResponse = object.getEnabledLanes();
        assertEquals(laneResponse, enabledLanes);

        List<MovementState> states = new ArrayList<MovementState>();
        object.setStates(states);
        List<MovementState> stateResponse = object.getStates();
        assertEquals(stateResponse, states);
    }

    @Test
    public void testEquals() {
        ProcessedSpat object = new ProcessedSpat();
        ProcessedSpat otherObject = new ProcessedSpat();
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
        ProcessedSpat object = new ProcessedSpat();

        Integer hash = object.hashCode();
        assertNotNull(hash);
    }
    
    @Test
    public void testToString() {
        ProcessedSpat object = new ProcessedSpat();

        String string = object.toString();
        assertNotNull(string);
    }
}