package us.dot.its.jpo.geojsonconverter.pojos.geojson.map;

import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.Before;
import org.junit.Test;

import us.dot.its.jpo.geojsonconverter.converter.WKTHandler;
import us.dot.its.jpo.geojsonconverter.pojos.geojson.LineString;

public class MapFeatureCollectionTest {
    MapFeature<LineString> feature;
    MapFeature<String> wktFeature;

    @Before
    public void setup() {
        MapProperties properties = new MapProperties();
        properties.setLaneId(2);
        properties.setEgressApproach(1);
        properties.setIngressApproach(0);
        properties.setIngressPath(false);
        properties.setEgressPath(true);

        double[][] coordinates = new double[][] { { 39.7392, 104.9903 }, { 39.7390, 104.9907 } };
        String wktGeometry = WKTHandler.coordinates2WKTLineString(coordinates);
        LineString geometry = new LineString(coordinates);

        feature = new MapFeature<LineString>(2, geometry, properties);
        wktFeature = new MapFeature<String>(2, wktGeometry, properties);
    }

    @Test
    public void testMapFeatureCollectionGeoJson() {
        @SuppressWarnings("unchecked") MapFeature<LineString>[] featureList = new MapFeature[] { feature };
        MapFeatureCollection<LineString> featureCollection = new MapFeatureCollection<LineString>(featureList);
        assertNotNull(featureCollection);
    }

    @Test
    public void testMapFeatureCollectionWKT() {
        @SuppressWarnings("unchecked") MapFeature<String>[] featureList = new MapFeature[] { wktFeature };
        MapFeatureCollection<String> featureCollection = new MapFeatureCollection<String>(featureList);
        assertNotNull(featureCollection);
    }

    @Test
    public void testTypeGeoJson() {
        @SuppressWarnings("unchecked") MapFeature<LineString>[] featureList = new MapFeature[] { feature };
        MapFeatureCollection<LineString> featureCollection = new MapFeatureCollection<LineString>(featureList);
        assertEquals("FeatureCollection", featureCollection.getType());
    }

    @Test
    public void testTypeWKT() {
        @SuppressWarnings("unchecked") MapFeature<String>[] featureList = new MapFeature[] { wktFeature };
        MapFeatureCollection<String> featureCollection = new MapFeatureCollection<String>(featureList);
        assertEquals("FeatureCollection", featureCollection.getType());
    }

    @Test
    public void testToStringGeoJson() {
        String expectedString = "{\"type\":\"FeatureCollection\",\"features\":[{\"type\":\"Feature\",\"id\":2,\"geometry\":{\"type\":\"LineString\",\"coordinates\":[[39.7392,104.9903],[39.739,104.9907]]},\"properties\":{\"laneId\":2,\"egressApproach\":1,\"ingressApproach\":0,\"ingressPath\":false,\"egressPath\":true}}]}";
        @SuppressWarnings("unchecked") MapFeature<LineString>[] featureList = new MapFeature[] { feature };
        MapFeatureCollection<LineString> featureCollection = new MapFeatureCollection<LineString>(featureList);
        assertEquals(expectedString, featureCollection.toString());
    }

    @Test
    public void testToStringWKT() {
        String expectedString = "{\"type\":\"FeatureCollection\",\"features\":[{\"type\":\"Feature\",\"id\":2,\"geometry\":\"LINESTRING (39.7392 104.9903, 39.739 104.9907)\",\"properties\":{\"laneId\":2,\"egressApproach\":1,\"ingressApproach\":0,\"ingressPath\":false,\"egressPath\":true}}]}";
        @SuppressWarnings("unchecked") MapFeature<String>[] featureList = new MapFeature[] { wktFeature };
        MapFeatureCollection<String> featureCollection = new MapFeatureCollection<String>(featureList);
        assertEquals(expectedString, featureCollection.toString());
    }
}
