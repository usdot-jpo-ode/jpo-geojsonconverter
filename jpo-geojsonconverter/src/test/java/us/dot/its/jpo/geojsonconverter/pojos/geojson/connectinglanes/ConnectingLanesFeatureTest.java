package us.dot.its.jpo.geojsonconverter.pojos.geojson.connectinglanes;

import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.Before;
import org.junit.Test;

import us.dot.its.jpo.geojsonconverter.pojos.geojson.LineString;

public class ConnectingLanesFeatureTest {
    ConnectingLanesProperties properties;
    LineString geometry;

    @Before
    public void setup() {
        properties = new ConnectingLanesProperties();
        properties.setSignalGroupId(1);
        properties.setIngressLaneId(2);
        properties.setEgressLaneId(3);

        double[][] coordinates = new double[][] { { 39.7392, 104.9903 }, { 39.7390, 104.9907 } };
        geometry = new LineString(coordinates);
    }

    @Test
    public void testConnectingLanesFeatureConstructor() {
        ConnectingLanesFeature feature = new ConnectingLanesFeature("id", geometry, properties);
        assertNotNull(feature);
    }

    @Test
    public void testGeoJSONType() {
        ConnectingLanesFeature feature = new ConnectingLanesFeature("id", geometry, properties);
        assertEquals("Feature", feature.getGeoJSONType());
    }

    @Test
    public void testType() {
        ConnectingLanesFeature feature = new ConnectingLanesFeature("id", geometry, properties);
        assertEquals("Feature", feature.getType());
    }

    @Test
    public void testToString() {
        String expectedString = "{\"type\":\"Feature\",\"id\":\"id\",\"geometry\":{\"type\":\"LineString\",\"coordinates\":[[39.7392,104.9903],[39.739,104.9907]]},\"properties\":{\"signalGroupId\":1,\"ingressLaneId\":2,\"egressLaneId\":3}}";
        ConnectingLanesFeature feature = new ConnectingLanesFeature("id", geometry, properties);
        assertEquals(expectedString, feature.toString());
    }

    @Test
    public void testGetId() {
        ConnectingLanesFeature feature = new ConnectingLanesFeature("id", geometry, properties);
        assertEquals("id", feature.getId());
    }

    @Test
    public void testGetGeometry() {
        ConnectingLanesFeature feature = new ConnectingLanesFeature("id", geometry, properties);
        assertEquals(39.7392, feature.getGeometry().getCoordinates()[0][0]);
    }

    @Test
    public void testGetProperties() {
        ConnectingLanesFeature feature = new ConnectingLanesFeature("id", geometry, properties);
        assertEquals(3, feature.getProperties().getEgressLaneId());
    }
}
