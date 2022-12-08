package us.dot.its.jpo.geojsonconverter.pojos.geojson.map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import com.fasterxml.jackson.databind.JsonNode;

import us.dot.its.jpo.ode.plugin.j2735.J2735BitString;
import us.dot.its.jpo.ode.plugin.j2735.J2735Connection;
import us.dot.its.jpo.ode.plugin.j2735.J2735GNSSstatusNames;
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
        otherObject.setEgressPath(true);
        
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
