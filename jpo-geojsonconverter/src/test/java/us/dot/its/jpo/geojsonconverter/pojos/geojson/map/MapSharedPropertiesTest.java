package us.dot.its.jpo.geojsonconverter.pojos.geojson.map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import us.dot.its.jpo.geojsonconverter.pojos.ProcessedValidationMessage;
import us.dot.its.jpo.ode.model.OdeMapMetadata.MapSource;
import us.dot.its.jpo.ode.plugin.j2735.J2735RegulatorySpeedLimit;
import us.dot.its.jpo.ode.plugin.j2735.OdePosition3D;

public class MapSharedPropertiesTest {
    @Test
    public void testMessageType() {
        String expectedMessageType = "type";
        MapSharedProperties mapSharedProperties = new MapSharedProperties();
        mapSharedProperties.setMessageType(expectedMessageType);
        assertEquals(expectedMessageType, mapSharedProperties.getMessageType());
    }

    @Test
    public void testOdeReceivedAt() {
        ZonedDateTime expectedOdeReceivedAt = Instant.parse("2022-01-01T00:00:00Z").atZone(ZoneId.of("UTC"));
        MapSharedProperties mapSharedProperties = new MapSharedProperties();
        mapSharedProperties.setOdeReceivedAt(expectedOdeReceivedAt);
        assertEquals(expectedOdeReceivedAt, mapSharedProperties.getOdeReceivedAt());
    }

    @Test
    public void testOriginIp() {
        String expectedIp = "10.0.0.1";
        MapSharedProperties mapSharedProperties = new MapSharedProperties();
        mapSharedProperties.setOriginIp(expectedIp);
        assertEquals(expectedIp, mapSharedProperties.getOriginIp());
    }

    @Test
    public void testIntersectionName() {
        String intersectionName = "name";
        MapSharedProperties mapSharedProperties = new MapSharedProperties();
        mapSharedProperties.setIntersectionName(intersectionName);
        assertEquals(intersectionName, mapSharedProperties.getIntersectionName());
    }

    @Test
    public void testRegion() {
        Integer region = 1;
        MapSharedProperties mapSharedProperties = new MapSharedProperties();
        mapSharedProperties.setRegion(region);
        assertEquals(region, mapSharedProperties.getRegion());
    }

    @Test
    public void testIntersectionId() {
        Integer intersectionId = 1;
        MapSharedProperties mapSharedProperties = new MapSharedProperties();
        mapSharedProperties.setIntersectionId(intersectionId);
        assertEquals(intersectionId, mapSharedProperties.getIntersectionId());
    }

    @Test
    public void testMsgIssueRevision() {
        Integer msgIssueRevision = 1;
        MapSharedProperties mapSharedProperties = new MapSharedProperties();
        mapSharedProperties.setMsgIssueRevision(msgIssueRevision);
        assertEquals(msgIssueRevision, mapSharedProperties.getMsgIssueRevision());
    }

    @Test
    public void testRevision() {
        Integer revision = 1;
        MapSharedProperties mapSharedProperties = new MapSharedProperties();
        mapSharedProperties.setRevision(revision);
        assertEquals(revision, mapSharedProperties.getRevision());
    }

    @Test
    public void testRefPoint() {
        OdePosition3D refPoint = new OdePosition3D();
        MapSharedProperties mapSharedProperties = new MapSharedProperties();
        mapSharedProperties.setRefPoint(refPoint);
        assertEquals(refPoint, mapSharedProperties.getRefPoint());
    }

    @Test
    public void testCti4501Conformant() {
        Boolean cti4501Conformant = true;
        MapSharedProperties mapSharedProperties = new MapSharedProperties();
        mapSharedProperties.setCti4501Conformant(cti4501Conformant);
        assertEquals(cti4501Conformant, mapSharedProperties.getCti4501Conformant());
    }

    @Test
    public void testValidationMessages() {
        List<ProcessedValidationMessage> validationMessages = new ArrayList<ProcessedValidationMessage>();
        MapSharedProperties mapSharedProperties = new MapSharedProperties();
        mapSharedProperties.setValidationMessages(validationMessages);
        assertEquals(validationMessages, mapSharedProperties.getValidationMessages());
    }

    @Test
    public void testLaneWidth() {
        Integer laneWidth = 1;
        MapSharedProperties mapSharedProperties = new MapSharedProperties();
        mapSharedProperties.setLaneWidth(laneWidth);
        assertEquals(laneWidth, mapSharedProperties.getLaneWidth());
    }

    @Test
    public void testSpeedLimits() {
        List<J2735RegulatorySpeedLimit> speedLimits = new ArrayList<J2735RegulatorySpeedLimit>();
        MapSharedProperties mapSharedProperties = new MapSharedProperties();
        mapSharedProperties.setSpeedLimits(speedLimits);
        assertEquals(speedLimits, mapSharedProperties.getSpeedLimits());
    }

    @Test
    public void testMapSource() {
        MapSource mapSourceRsu = MapSource.RSU;
        MapSharedProperties mapSharedProperties = new MapSharedProperties();
        mapSharedProperties.setMapSource(mapSourceRsu);
        assertEquals(mapSourceRsu, mapSharedProperties.getMapSource());
    }

    @Test
    public void testTimestamp() {
        ZonedDateTime timestamp = Instant.parse("2022-01-01T00:00:00Z").atZone(ZoneId.of("UTC"));
        MapSharedProperties mapSharedProperties = new MapSharedProperties();
        mapSharedProperties.setTimeStamp(timestamp);
        assertEquals(timestamp, mapSharedProperties.getTimeStamp());
    }

    @Test
    public void testEquals() {
        MapSharedProperties object = new MapSharedProperties();
        MapSharedProperties otherObject = new MapSharedProperties();
        otherObject.setCti4501Conformant(false);

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
        MapSharedProperties mapSharedProperties = new MapSharedProperties();
        Integer hash = mapSharedProperties.hashCode();
        assertNotNull(hash);
    }
    
    @Test
    public void testToString() {
        MapSharedProperties mapSharedProperties = new MapSharedProperties();
        String string = mapSharedProperties.toString();
        assertNotNull(string);
    }
}
