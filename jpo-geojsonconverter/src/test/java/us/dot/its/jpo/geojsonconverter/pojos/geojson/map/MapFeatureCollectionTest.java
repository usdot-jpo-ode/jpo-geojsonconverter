package us.dot.its.jpo.geojsonconverter.pojos.geojson.map;

import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.Before;
import org.junit.Test;

import us.dot.its.jpo.geojsonconverter.pojos.geojson.LineString;

public class MapFeatureCollectionTest {
    MapFeature feature;

    @Before
    public void setup() {
        MapProperties properties = new MapProperties();
        properties.setLaneId(2);
        properties.setIp("10.0.0.1");
        properties.setOdeReceivedAt("2022-01-01T00:00:00");
        properties.setEgressApproach(1);
        properties.setIngressApproach(0);
        properties.setIngressPath(false);
        properties.setEgressPath(true);
        Integer[] cLanes = new Integer[] { 3, 12 };
        properties.setConnectedLanes(cLanes);

        double[][] coordinates = new double[][] { { 39.7392, 104.9903 }, { 39.7390, 104.9907 } };
        LineString geometry = new LineString(coordinates);

        feature = new MapFeature(2, geometry, properties);
    }

    @Test
    public void testMapFeatureCollection() {
        MapFeature[] featureList = new MapFeature[] { feature };
        MapFeatureCollection featureCollection = new MapFeatureCollection(featureList);
        assertNotNull(featureCollection);
    }

    @Test
    public void testGeoJSONType() {
        MapFeature[] featureList = new MapFeature[] { feature };
        MapFeatureCollection featureCollection = new MapFeatureCollection(featureList);
        assertEquals("FeatureCollection", featureCollection.getGeoJSONType());
    }

    @Test
    public void testType() {
        MapFeature[] featureList = new MapFeature[] { feature };
        MapFeatureCollection featureCollection = new MapFeatureCollection(featureList);
        assertEquals("FeatureCollection", featureCollection.getType());
        featureCollection.setType("Test");
        assertEquals("FeatureCollection", featureCollection.getType());
    }

    @Test
    public void testToString() {
        String expectedString = "{\"type\":\"FeatureCollection\",\"features\":[{\"type\":\"Feature\",\"id\":2,\"geometry\":{\"type\":\"LineString\",\"coordinates\":[[39.7392,104.9903],[39.739,104.9907]]},\"properties\":{\"lane_id\":2,\"ip\":\"10.0.0.1\",\"ode_received_at\":\"2022-01-01T00:00:00\",\"egress_approach\":1,\"ingress_approach\":0,\"ingress_path\":false,\"egress_path\":true,\"connected_lanes\":[3,12]}}]}";
        MapFeature[] featureList = new MapFeature[] { feature };
        MapFeatureCollection featureCollection = new MapFeatureCollection(featureList);
        assertEquals(expectedString, featureCollection.toString());
    }
}
