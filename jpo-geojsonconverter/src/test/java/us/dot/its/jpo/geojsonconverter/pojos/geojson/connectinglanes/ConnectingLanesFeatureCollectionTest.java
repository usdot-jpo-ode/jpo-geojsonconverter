package us.dot.its.jpo.geojsonconverter.pojos.geojson.connectinglanes;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.Before;
import org.junit.Test;

import us.dot.its.jpo.geojsonconverter.converter.WKTHandler;
import us.dot.its.jpo.geojsonconverter.pojos.geojson.LineString;

public class ConnectingLanesFeatureCollectionTest {
    GeoJsonConnectingLanesFeature featureGeoJson;
    WKTConnectingLanesFeature featureWKT;
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

        featureGeoJson = new GeoJsonConnectingLanesFeature("id", geometry, properties);
        featureWKT = new WKTConnectingLanesFeature("id", wtkStr, properties);
    }

    @Test
    public void testConnectingLanesFeatureCollectionGeoJson() {
        GeoJsonConnectingLanesFeature[] featureList = new GeoJsonConnectingLanesFeature[] { featureGeoJson };
        ConnectingLanesFeatureCollection<GeoJsonConnectingLanesFeature> featureCollection = new ConnectingLanesFeatureCollection<GeoJsonConnectingLanesFeature>(featureList);
        assertNotNull(featureCollection);
    }

    @Test
    public void testConnectingLanesFeatureCollectionWKT() {
        WKTConnectingLanesFeature[] featureList = new WKTConnectingLanesFeature[] { featureWKT };
        ConnectingLanesFeatureCollection<WKTConnectingLanesFeature> featureCollection = new ConnectingLanesFeatureCollection<WKTConnectingLanesFeature>(featureList);
        assertNotNull(featureCollection);
    }

    @Test
    public void testTypeGeoJson() {
        GeoJsonConnectingLanesFeature[] featureList = new GeoJsonConnectingLanesFeature[] { featureGeoJson };
        ConnectingLanesFeatureCollection<GeoJsonConnectingLanesFeature> featureCollection = new ConnectingLanesFeatureCollection<GeoJsonConnectingLanesFeature>(featureList);
        assertEquals("FeatureCollection", featureCollection.getType());
    }

    @Test
    public void testTypeWKT() {
        WKTConnectingLanesFeature[] featureList = new WKTConnectingLanesFeature[] { featureWKT };
        ConnectingLanesFeatureCollection<WKTConnectingLanesFeature> featureCollection = new ConnectingLanesFeatureCollection<WKTConnectingLanesFeature>(featureList);
        assertEquals("FeatureCollection", featureCollection.getType());
    }

    @Test
    public void testToStringGeoJson() {
        String expectedString = "{\"type\":\"FeatureCollection\",\"features\":[{\"type\":\"Feature\",\"id\":\"id\",\"geometry\":{\"type\":\"LineString\",\"coordinates\":[[39.7392,104.9903],[39.739,104.9907]]},\"properties\":{\"signalGroupId\":1,\"ingressLaneId\":2,\"egressLaneId\":3}}]}";
        GeoJsonConnectingLanesFeature[] featureList = new GeoJsonConnectingLanesFeature[] { featureGeoJson };
        ConnectingLanesFeatureCollection<GeoJsonConnectingLanesFeature> featureCollection = new ConnectingLanesFeatureCollection<GeoJsonConnectingLanesFeature>(featureList);
        assertEquals(expectedString, featureCollection.toString());
    }

    @Test
    public void testToStringWKT() {
        String expectedString = "{\"type\":\"FeatureCollection\",\"features\":[{\"type\":\"Feature\",\"id\":\"id\",\"geometry\":\"LINESTRING (39.7392 104.9903, 39.739 104.9907)\",\"properties\":{\"signalGroupId\":1,\"ingressLaneId\":2,\"egressLaneId\":3}}]}";
        WKTConnectingLanesFeature[] featureList = new WKTConnectingLanesFeature[] { featureWKT };
        ConnectingLanesFeatureCollection<WKTConnectingLanesFeature> featureCollection = new ConnectingLanesFeatureCollection<WKTConnectingLanesFeature>(featureList);
        assertEquals(expectedString, featureCollection.toString());
    }
}
