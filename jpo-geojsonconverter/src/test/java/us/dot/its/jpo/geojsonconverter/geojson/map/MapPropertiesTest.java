package us.dot.its.jpo.geojsonconverter.geojson.map;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.Test;

import us.dot.its.jpo.geojsonconverter.pojos.geojson.map.MapProperties;

public class MapPropertiesTest {
    @Test
    public void testLaneId() {
        Integer expectedLaneId = 3;
        MapProperties mapProperties = new MapProperties();
        mapProperties.setLaneId(expectedLaneId);
        assertEquals(expectedLaneId, mapProperties.getLaneId());
    }

    @Test
    public void testIp() {
        String expectedIp = "10.0.0.1";
        MapProperties mapProperties = new MapProperties();
        mapProperties.setIp(expectedIp);
        assertEquals(expectedIp, mapProperties.getIp());
    }

    @Test
    public void testOdeReceivedAt() {
        String expectedOdeReceivedAt = "testTimestamp";
        MapProperties mapProperties = new MapProperties();
        mapProperties.setOdeReceivedAt(expectedOdeReceivedAt);
        assertEquals(expectedOdeReceivedAt, mapProperties.getOdeReceivedAt());
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
    public void testConnectedLanes() {
        Integer[] expectedConnectedLanes = new Integer[] { 3, 12 };
        MapProperties mapProperties = new MapProperties();
        mapProperties.setConnectedLanes(expectedConnectedLanes);
        assertEquals(expectedConnectedLanes.length, mapProperties.getConnectedLanes().length);
        assertEquals(expectedConnectedLanes[0], mapProperties.getConnectedLanes()[0]);
        assertEquals(expectedConnectedLanes[1], mapProperties.getConnectedLanes()[1]);
    }
}
