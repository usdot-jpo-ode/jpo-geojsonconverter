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
        properties.setEgressApproach(1);
        properties.setIngressApproach(0);
        properties.setIngressPath(false);
        properties.setEgressPath(true);

        double[][] coordinates = new double[][] { { 39.7392, 104.9903 }, { 39.7390, 104.9907 } };
        LineString geometry = new LineString(coordinates);

        feature = new GeoJSONMapFeature(2, geometry, properties);
    }

    @Test
    public void testMapFeatureCollection() {
        MapFeature[] featureList = new MapFeature[] { feature };
        MapFeatureCollection featureCollection = new MapFeatureCollection(featureList);
        assertNotNull(featureCollection);
    }

    @Test
    public void testType() {
        MapFeature[] featureList = new MapFeature[] { feature };
        MapFeatureCollection featureCollection = new MapFeatureCollection(featureList);
        assertEquals("FeatureCollection", featureCollection.getType());
    }

    @Test
    public void testToString() {
        String expectedString = "{\"type\":\"FeatureCollection\",\"features\":[{\"type\":\"Feature\",\"id\":2,\"geometry\":{\"type\":\"LineString\",\"coordinates\":[[39.7392,104.9903],[39.739,104.9907]]},\"properties\":{\"laneId\":2,\"egressApproach\":1,\"ingressApproach\":0,\"ingressPath\":false,\"egressPath\":true}}]}";
        MapFeature[] featureList = new MapFeature[] { feature };
        MapFeatureCollection featureCollection = new MapFeatureCollection(featureList);
        assertEquals(expectedString, featureCollection.toString());
    }
}
