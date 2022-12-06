package us.dot.its.jpo.geojsonconverter.pojos.geojson.map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import com.fasterxml.jackson.databind.JsonNode;

import us.dot.its.jpo.geojsonconverter.pojos.ProcessedValidationMessage;
import us.dot.its.jpo.ode.model.OdeMapMetadata.MapSource;
import us.dot.its.jpo.ode.plugin.j2735.J2735BitString;
import us.dot.its.jpo.ode.plugin.j2735.J2735Connection;
import us.dot.its.jpo.ode.plugin.j2735.J2735GNSSstatusNames;
import us.dot.its.jpo.ode.plugin.j2735.J2735RegulatorySpeedLimit;
import us.dot.its.jpo.ode.plugin.j2735.OdePosition3D;
import us.dot.its.jpo.ode.plugin.j2735.builders.BitStringBuilder;
import us.dot.its.jpo.ode.util.JsonUtils;

public class MapPropertiesTest {
    @Test
    public void testNodes() {
        List<MapNode> expectedMapNodeList = new ArrayList<MapNode>();
        MapProperties mapProperties = new MapProperties();
        mapProperties.setNodes(expectedMapNodeList);
        assertEquals(expectedMapNodeList, mapProperties.getNodes());
    }

    @Test
    public void testMessageType() {
        String expectedMessageType = "type";
        MapProperties mapProperties = new MapProperties();
        mapProperties.setMessageType(expectedMessageType);
        assertEquals(expectedMessageType, mapProperties.getMessageType());
    }

    @Test
    public void testOdeReceivedAt() {
        ZonedDateTime expectedOdeReceivedAt = Instant.parse("2022-01-01T00:00:00Z").atZone(ZoneId.of("UTC"));
        MapProperties mapProperties = new MapProperties();
        mapProperties.setOdeReceivedAt(expectedOdeReceivedAt);
        assertEquals(expectedOdeReceivedAt, mapProperties.getOdeReceivedAt());
    }

    @Test
    public void testOriginIp() {
        String expectedIp = "10.0.0.1";
        MapProperties mapProperties = new MapProperties();
        mapProperties.setOriginIp(expectedIp);
        assertEquals(expectedIp, mapProperties.getOriginIp());
    }

    @Test
    public void testIntersectionName() {
        String intersectionName = "name";
        MapProperties mapProperties = new MapProperties();
        mapProperties.setIntersectionName(intersectionName);
        assertEquals(intersectionName, mapProperties.getIntersectionName());
    }

    @Test
    public void testRegion() {
        Integer region = 1;
        MapProperties mapProperties = new MapProperties();
        mapProperties.setRegion(region);
        assertEquals(region, mapProperties.getRegion());
    }

    @Test
    public void testIntersectionId() {
        Integer intersectionId = 1;
        MapProperties mapProperties = new MapProperties();
        mapProperties.setIntersectionId(intersectionId);
        assertEquals(intersectionId, mapProperties.getIntersectionId());
    }

    @Test
    public void testMsgIssueRevision() {
        Integer msgIssueRevision = 1;
        MapProperties mapProperties = new MapProperties();
        mapProperties.setMsgIssueRevision(msgIssueRevision);
        assertEquals(msgIssueRevision, mapProperties.getMsgIssueRevision());
    }

    @Test
    public void testRevision() {
        Integer revision = 1;
        MapProperties mapProperties = new MapProperties();
        mapProperties.setRevision(revision);
        assertEquals(revision, mapProperties.getRevision());
    }

    @Test
    public void testRefPoint() {
        OdePosition3D refPoint = new OdePosition3D();
        MapProperties mapProperties = new MapProperties();
        mapProperties.setRefPoint(refPoint);
        assertEquals(refPoint, mapProperties.getRefPoint());
    }

    @Test
    public void testCti4501Conformant() {
        Boolean cti4501Conformant = true;
        MapProperties mapProperties = new MapProperties();
        mapProperties.setCti4501Conformant(cti4501Conformant);
        assertEquals(cti4501Conformant, mapProperties.getCti4501Conformant());
    }

    @Test
    public void testValidationMessages() {
        List<ProcessedValidationMessage> validationMessages = new ArrayList<ProcessedValidationMessage>();
        MapProperties mapProperties = new MapProperties();
        mapProperties.setValidationMessages(validationMessages);
        assertEquals(validationMessages, mapProperties.getValidationMessages());
    }

    @Test
    public void testLaneWidth() {
        Integer laneWidth = 1;
        MapProperties mapProperties = new MapProperties();
        mapProperties.setLaneWidth(laneWidth);
        assertEquals(laneWidth, mapProperties.getLaneWidth());
    }

    @Test
    public void testSpeedLimits() {
        List<J2735RegulatorySpeedLimit> speedLimits = new ArrayList<J2735RegulatorySpeedLimit>();
        MapProperties mapProperties = new MapProperties();
        mapProperties.setSpeedLimits(speedLimits);
        assertEquals(speedLimits, mapProperties.getSpeedLimits());
    }

    @Test
    public void testMapSource() {
        MapSource mapSourceRsu = MapSource.RSU;
        MapProperties mapProperties = new MapProperties();
        mapProperties.setMapSource(mapSourceRsu);
        assertEquals(mapSourceRsu, mapProperties.getMapSource());
    }

    @Test
    public void testTimestamp() {
        ZonedDateTime timestamp = Instant.parse("2022-01-01T00:00:00Z").atZone(ZoneId.of("UTC"));
        MapProperties mapProperties = new MapProperties();
        mapProperties.setTimeStamp(timestamp);
        assertEquals(timestamp, mapProperties.getTimeStamp());
    }

    @Test
    public void testLaneId() {
        Integer expectedLaneId = 3;
        MapProperties mapProperties = new MapProperties();
        mapProperties.setLaneId(expectedLaneId);
        assertEquals(expectedLaneId, mapProperties.getLaneId());
    }

    @Test
    public void testLaneName() {
        String expectedLaneName = "laneName";
        MapProperties mapProperties = new MapProperties();
        mapProperties.setLaneName(expectedLaneName);
        assertEquals(expectedLaneName, mapProperties.getLaneName());
    }

    @Test
    public void testSharedWidth() {
        JsonNode node = JsonUtils.newNode().put("test", "00000000");
        J2735BitString actualBitString = BitStringBuilder.genericBitString(node,
        J2735GNSSstatusNames.values());
        MapProperties mapProperties = new MapProperties();
        mapProperties.setSharedWith(actualBitString);
        assertEquals(actualBitString, mapProperties.getSharedWith());
    }

    @Test
    public void testEgressApproach() {
        Integer expectedEgressApproach = 1;
        MapProperties mapProperties = new MapProperties();
        mapProperties.setEgressApproach(expectedEgressApproach);
        assertEquals(expectedEgressApproach, mapProperties.getEgressApproach());
    }

    @Test
    public void testIngressApproach() {
        Integer expectedIngressApproach = 1;
        MapProperties mapProperties = new MapProperties();
        mapProperties.setIngressApproach(expectedIngressApproach);
        assertEquals(expectedIngressApproach, mapProperties.getIngressApproach());
    }

    @Test
    public void testIngressPath() {
        Boolean expectedIngressPath = true;
        MapProperties mapProperties = new MapProperties();
        mapProperties.setIngressPath(expectedIngressPath);
        assertEquals(expectedIngressPath, mapProperties.getIngressPath());
    }

    @Test
    public void testEgressPath() {
        Boolean expectedEgressPath = true;
        MapProperties mapProperties = new MapProperties();
        mapProperties.setEgressPath(expectedEgressPath);
        assertEquals(expectedEgressPath, mapProperties.getEgressPath());
    }

    @Test
    public void testManeuvers() {
        JsonNode node = JsonUtils.newNode().put("test", "00000000");
        J2735BitString expectedManeuver = BitStringBuilder.genericBitString(node,
        J2735GNSSstatusNames.values());
        MapProperties mapProperties = new MapProperties();
        mapProperties.setManeuvers(expectedManeuver);
        assertEquals(expectedManeuver, mapProperties.getManeuvers());
    }

    @Test
    public void testConnectsTo() {
        List<J2735Connection> expectedConnectsTo = new ArrayList<J2735Connection>();
        MapProperties mapProperties = new MapProperties();
        mapProperties.setConnectsTo(expectedConnectsTo);
        assertEquals(expectedConnectsTo, mapProperties.getConnectsTo());
    }

    @Test
    public void testEquals() {
        MapProperties object = new MapProperties();
        MapProperties otherObject = new MapProperties();
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
        MapProperties mapProperties = new MapProperties();
        Integer hash = mapProperties.hashCode();
        assertNotNull(hash);
    }
    
    @Test
    public void testToString() {
        MapProperties mapProperties = new MapProperties();
        String string = mapProperties.toString();
        assertNotNull(string);
    }
}
