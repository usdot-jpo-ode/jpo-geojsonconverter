package us.dot.its.jpo.geojsonconverter.pojos.spat;

import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import us.dot.its.jpo.geojsonconverter.pojos.ProcessedValidationMessage;

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

        List<ProcessedValidationMessage> validationMessages = new ArrayList<ProcessedValidationMessage>();
        object.setValidationMessages(validationMessages);
        List<ProcessedValidationMessage> vmResponse = object.getValidationMessages();
        assertEquals(vmResponse, validationMessages);

        object.setRevision(1);
        Integer revisionResponse = object.getRevision();
        assertEquals(revisionResponse, 1);

        IntersectionStatusObject statusObject = new IntersectionStatusObject();
        object.setStatus(statusObject);
        IntersectionStatusObject statusResponse = object.getStatus();
        assertEquals(statusResponse, statusObject);

        ZonedDateTime dateTime = ZonedDateTime.now();
        object.setUtcTimeStamp(dateTime);
        ZonedDateTime tsResponse = object.getUtcTimeStamp();
        assertEquals(tsResponse, dateTime);

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
        otherObject.setCti4501Conformant(true);

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
