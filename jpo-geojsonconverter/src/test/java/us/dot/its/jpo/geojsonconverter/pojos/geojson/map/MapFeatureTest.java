package us.dot.its.jpo.geojsonconverter.pojos.geojson.map;

import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.Before;
import org.junit.Test;

import us.dot.its.jpo.geojsonconverter.pojos.geojson.LineString;

public class MapFeatureTest {
    MapProperties properties;
    LineString geometry;

    @Before
    public void setup() {
        properties = new MapProperties();
        properties.setLaneId(2);
        properties.setOriginIp("10.0.0.1");
        properties.setOdeReceivedAt("2022-01-01T00:00:00");
        properties.setEgressApproach(1);
        properties.setIngressApproach(0);
        properties.setIngressPath(false);
        properties.setEgressPath(true);
        Integer[] cLanes = new Integer[] { 3, 12 };

        double[][] coordinates = new double[][] { { 39.7392, 104.9903 }, { 39.7390, 104.9907 } };
        geometry = new LineString(coordinates);
    }

    @Test
    public void testMapFeatureConstructor() {
        MapFeature feature = new MapFeature(properties.getLaneId(), geometry, properties);
        assertNotNull(feature);
    }

    @Test
    public void testGeoJSONType() {
        MapFeature feature = new MapFeature(properties.getLaneId(), geometry, properties);
        assertEquals("Feature", feature.getGeoJSONType());
    }

    @Test
    public void testType() {
        MapFeature feature = new MapFeature(properties.getLaneId(), geometry, properties);
        assertEquals("Feature", feature.getType());
    }

    @Test
    public void testToString() {
        String expectedString = "{\"type\":\"Feature\",\"id\":2,\"geometry\":{\"type\":\"LineString\",\"coordinates\":[[39.7392,104.9903],[39.739,104.9907]]},\"properties\":{\"lane_id\":2,\"ip\":\"10.0.0.1\",\"ode_received_at\":\"2022-01-01T00:00:00\",\"egress_approach\":1,\"ingress_approach\":0,\"ingress_path\":false,\"egress_path\":true,\"connected_lanes\":[3,12]}}";
        MapFeature feature = new MapFeature(properties.getLaneId(), geometry, properties);
        assertEquals(expectedString, feature.toString());
    }

    @Test
    public void testGetId() {
        MapFeature feature = new MapFeature(properties.getLaneId(), geometry, properties);
        assertEquals(2, feature.getId());
    }

    @Test
    public void testGetGeometry() {
        MapFeature feature = new MapFeature(properties.getLaneId(), geometry, properties);
        assertEquals(39.7392, feature.getGeometry().getCoordinates()[0][0]);
    }

    @Test
    public void testGetProperties() {
        MapFeature feature = new MapFeature(properties.getLaneId(), geometry, properties);
        assertEquals(2, feature.getProperties().getLaneId());
    }
}
