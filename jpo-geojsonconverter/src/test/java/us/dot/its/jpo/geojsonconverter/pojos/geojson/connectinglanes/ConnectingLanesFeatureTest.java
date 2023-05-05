package us.dot.its.jpo.geojsonconverter.pojos.geojson.connectinglanes;

import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.Before;
import org.junit.Test;

import us.dot.its.jpo.geojsonconverter.converter.WKTHandler;
import us.dot.its.jpo.geojsonconverter.pojos.geojson.LineString;

public class ConnectingLanesFeatureTest {
    ConnectingLanesProperties properties;
    LineString geometry;
    String wktGeometry;

    @Before
    public void setup() {
        properties = new ConnectingLanesProperties();
        properties.setSignalGroupId(1);
        properties.setIngressLaneId(2);
        properties.setEgressLaneId(3);

        double[][] coordinates = new double[][] { { 39.7392, 104.9903 }, { 39.7390, 104.9907 } };
        geometry = new LineString(coordinates);
        wktGeometry = WKTHandler.coordinates2WKTLineString(coordinates);
    }

    @Test
    public void testGeoJsonConnectingLanesFeatureConstructor() {
        ConnectingLanesFeature<LineString> feature = new ConnectingLanesFeature<LineString>("id", geometry, properties);
        assertNotNull(feature);
    }

    @Test
    public void testWKTConnectingLanesFeatureConstructor() {
        ConnectingLanesFeature<String> feature = new ConnectingLanesFeature<String>("id", wktGeometry, properties);
        assertNotNull(feature);
    }

    @Test
    public void testType() {
        ConnectingLanesFeature<LineString> feature = new ConnectingLanesFeature<LineString>("id", geometry, properties);
        assertEquals("Feature", feature.getType());

        ConnectingLanesFeature<String> wktFeature = new ConnectingLanesFeature<String>("id", wktGeometry, properties);
        assertEquals("Feature", wktFeature.getType());
    }

    @Test
    public void testGeoJsonToString() {
        String expectedString = "{\"type\":\"Feature\",\"id\":\"id\",\"geometry\":{\"type\":\"LineString\",\"coordinates\":[[39.7392,104.9903],[39.739,104.9907]]},\"properties\":{\"signalGroupId\":1,\"ingressLaneId\":2,\"egressLaneId\":3}}";
        ConnectingLanesFeature<LineString> feature = new ConnectingLanesFeature<LineString>("id", geometry, properties);
        assertEquals(expectedString, feature.toString());
    }

    @Test
    public void testWKTToString() {
        String expectedString = "{\"type\":\"Feature\",\"id\":\"id\",\"geometry\":\"LINESTRING (39.7392 104.9903, 39.739 104.9907)\",\"properties\":{\"signalGroupId\":1,\"ingressLaneId\":2,\"egressLaneId\":3}}";
        ConnectingLanesFeature<String> wktFeature = new ConnectingLanesFeature<String>("id", wktGeometry, properties);
        assertEquals(expectedString, wktFeature.toString());
    }

    @Test
    public void testGetId() {
        ConnectingLanesFeature<LineString> feature = new ConnectingLanesFeature<LineString>("id", geometry, properties);
        assertEquals("id", feature.getId());

        ConnectingLanesFeature<String> wktFeature = new ConnectingLanesFeature<String>("id", wktGeometry, properties);
        assertEquals("id", wktFeature.getId());
    }

    @Test
    public void testGeoJsonGetGeometry() {
        ConnectingLanesFeature<LineString> feature = new ConnectingLanesFeature<LineString>("id", geometry, properties);
        assertEquals(39.7392, feature.getGeometry().getCoordinates()[0][0]);
    }

    @Test
    public void testWKTGetGeometry() {
        ConnectingLanesFeature<String> wktFeature = new ConnectingLanesFeature<String>("id", wktGeometry, properties);
        assertEquals("LINESTRING (39.7392 104.9903, 39.739 104.9907)", wktFeature.getGeometry());
    }

    @Test
    public void testGetProperties() {
        ConnectingLanesFeature<LineString> feature = new ConnectingLanesFeature<LineString>("id", geometry, properties);
        assertEquals(3, feature.getProperties().getEgressLaneId());

        ConnectingLanesFeature<String> wktFeature = new ConnectingLanesFeature<String>("id", wktGeometry, properties);
        assertEquals(3, wktFeature.getProperties().getEgressLaneId());
    }
}
