package us.dot.its.jpo.geojsonconverter.pojos.geojson.connectinglanes;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.Before;
import org.junit.Test;

import us.dot.its.jpo.geojsonconverter.converter.WKTHandler;
import us.dot.its.jpo.geojsonconverter.pojos.geojson.LineString;

public class ConnectingLanesFeatureCollectionTest {
    ConnectingLanesFeature<LineString> featureGeoJson;
    ConnectingLanesFeature<String> featureWKT;
    LineString geometry;

    @Before
    public void setup() {
        ConnectingLanesProperties properties = new ConnectingLanesProperties();
        properties.setSignalGroupId(1);
        properties.setIngressLaneId(2);
        properties.setEgressLaneId(3);

        double[][] coordinates = new double[][] { { 39.7392, 104.9903 }, { 39.7390, 104.9907 } };
        String wtkStr = WKTHandler.coordinates2WKTLineString(coordinates);
        geometry = new LineString(coordinates);

        featureGeoJson = new ConnectingLanesFeature<LineString>("id", geometry, properties);
        featureWKT = new ConnectingLanesFeature<String>("id", wtkStr, properties);
    }

    @Test
    public void testConnectingLanesFeatureCollectionGeoJson() {
        @SuppressWarnings("unchecked") ConnectingLanesFeature<LineString>[] featureList = new ConnectingLanesFeature[] { featureGeoJson };
        ConnectingLanesFeatureCollection<LineString> featureCollection = new ConnectingLanesFeatureCollection<LineString>(featureList);
        assertNotNull(featureCollection);
    }

    @Test
    public void testConnectingLanesFeatureCollectionWKT() {
        @SuppressWarnings("unchecked") ConnectingLanesFeature<String>[] featureList = new ConnectingLanesFeature[] { featureWKT };
        ConnectingLanesFeatureCollection<String> featureCollection = new ConnectingLanesFeatureCollection<String>(featureList);
        assertNotNull(featureCollection);
    }

    @Test
    public void testTypeGeoJson() {
        @SuppressWarnings("unchecked") ConnectingLanesFeature<LineString>[] featureList = new ConnectingLanesFeature[] { featureGeoJson };
        ConnectingLanesFeatureCollection<LineString> featureCollection = new ConnectingLanesFeatureCollection<LineString>(featureList);
        assertEquals("FeatureCollection", featureCollection.getType());
    }

    @Test
    public void testTypeWKT() {
        @SuppressWarnings("unchecked") ConnectingLanesFeature<String>[] featureList = new ConnectingLanesFeature[] { featureWKT };
        ConnectingLanesFeatureCollection<String> featureCollection = new ConnectingLanesFeatureCollection<String>(featureList);
        assertEquals("FeatureCollection", featureCollection.getType());
    }

    @Test
    public void testToStringGeoJson() {
        String expectedString = "{\"type\":\"FeatureCollection\",\"features\":[{\"type\":\"Feature\",\"id\":\"id\",\"geometry\":{\"type\":\"LineString\",\"coordinates\":[[39.7392,104.9903],[39.739,104.9907]]},\"properties\":{\"signalGroupId\":1,\"ingressLaneId\":2,\"egressLaneId\":3}}]}";
        @SuppressWarnings("unchecked") ConnectingLanesFeature<LineString>[] featureList = new ConnectingLanesFeature[] { featureGeoJson };
        ConnectingLanesFeatureCollection<LineString> featureCollection = new ConnectingLanesFeatureCollection<LineString>(featureList);
        assertEquals(expectedString, featureCollection.toString());
    }

    @Test
    public void testToStringWKT() {
        String expectedString = "{\"type\":\"FeatureCollection\",\"features\":[{\"type\":\"Feature\",\"id\":\"id\",\"geometry\":\"LINESTRING (39.7392 104.9903, 39.739 104.9907)\",\"properties\":{\"signalGroupId\":1,\"ingressLaneId\":2,\"egressLaneId\":3}}]}";
        @SuppressWarnings("unchecked") ConnectingLanesFeature<String>[] featureList = new ConnectingLanesFeature[] { featureWKT };
        ConnectingLanesFeatureCollection<String> featureCollection = new ConnectingLanesFeatureCollection<String>(featureList);
        assertEquals(expectedString, featureCollection.toString());
    }
}
