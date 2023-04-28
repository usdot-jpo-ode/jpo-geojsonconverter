package us.dot.its.jpo.geojsonconverter.pojos.geojson.connectinglanes;

import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.Before;
import org.junit.Test;

import us.dot.its.jpo.geojsonconverter.pojos.geojson.LineString;

public class ConnectingLanesFeatureCollectionTest {
    ConnectingLanesFeature feature;
    LineString geometry;

    @Before
    public void setup() {
        ConnectingLanesProperties properties = new ConnectingLanesProperties();
        properties.setSignalGroupId(1);
        properties.setIngressLaneId(2);
        properties.setEgressLaneId(3);

        double[][] coordinates = new double[][] { { 39.7392, 104.9903 }, { 39.7390, 104.9907 } };
        geometry = new LineString(coordinates);

        feature = new GeoJSONConnectingLanesFeature("id", geometry, properties);
    }

    @Test
    public void testConnectingLanesFeatureCollection() {
        ConnectingLanesFeature[] featureList = new ConnectingLanesFeature[] { feature };
        ConnectingLanesFeatureCollection featureCollection = new ConnectingLanesFeatureCollection(featureList);
        assertNotNull(featureCollection);
    }

    @Test
    public void testType() {
        ConnectingLanesFeature[] featureList = new ConnectingLanesFeature[] { feature };
        ConnectingLanesFeatureCollection featureCollection = new ConnectingLanesFeatureCollection(featureList);
        assertEquals("FeatureCollection", featureCollection.getType());
    }

    @Test
    public void testToString() {
        String expectedString = "{\"type\":\"FeatureCollection\",\"features\":[{\"type\":\"Feature\",\"id\":\"id\",\"geometry\":{\"type\":\"LineString\",\"coordinates\":[[39.7392,104.9903],[39.739,104.9907]]},\"properties\":{\"signalGroupId\":1,\"ingressLaneId\":2,\"egressLaneId\":3}}]}";
        ConnectingLanesFeature[] featureList = new ConnectingLanesFeature[] { feature };
        ConnectingLanesFeatureCollection featureCollection = new ConnectingLanesFeatureCollection(featureList);
        assertEquals(expectedString, featureCollection.toString());
    }
}
