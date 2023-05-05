package us.dot.its.jpo.geojsonconverter.pojos.geojson.map;

import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.Before;
import org.junit.Test;

import us.dot.its.jpo.geojsonconverter.converter.WKTHandler;
import us.dot.its.jpo.geojsonconverter.pojos.geojson.LineString;

public class MapFeatureTest {
    MapProperties properties;
    LineString geometry;
    String wktGeometry;

    @Before
    public void setup() {
        properties = new MapProperties();
        properties.setLaneId(2);
        properties.setEgressApproach(1);
        properties.setIngressApproach(0);
        properties.setIngressPath(false);
        properties.setEgressPath(true);

        double[][] coordinates = new double[][] { { 39.7392, 104.9903 }, { 39.7390, 104.9907 } };
        geometry = new LineString(coordinates);
        wktGeometry = WKTHandler.coordinates2WKTLineString(coordinates);
    }

    @Test
    public void testGeoJsonMapFeatureConstructor() {
        GeoJsonMapFeature feature = new GeoJsonMapFeature(properties.getLaneId(), geometry, properties);
        assertNotNull(feature);
    }

    @Test
    public void testWKTMapFeatureConstructor() {
        WKTMapFeature feature = new WKTMapFeature(properties.getLaneId(), wktGeometry, properties);
        assertNotNull(feature);
    }

    @Test
    public void testType() {
        GeoJsonMapFeature feature = new GeoJsonMapFeature(properties.getLaneId(), geometry, properties);
        assertEquals("Feature", feature.getType());

        WKTMapFeature wktFeature = new WKTMapFeature(properties.getLaneId(), wktGeometry, properties);
        assertEquals("Feature", wktFeature.getType());
    }

    @Test
    public void testGeoJsonToString() {
        String expectedString = "{\"type\":\"Feature\",\"id\":2,\"geometry\":{\"type\":\"LineString\",\"coordinates\":[[39.7392,104.9903],[39.739,104.9907]]},\"properties\":{\"laneId\":2,\"egressApproach\":1,\"ingressApproach\":0,\"ingressPath\":false,\"egressPath\":true}}";
        GeoJsonMapFeature feature = new GeoJsonMapFeature(properties.getLaneId(), geometry, properties);
        assertEquals(expectedString, feature.toString());
    }

    @Test
    public void testWKTToString() {
        String expectedString = "{\"type\":\"Feature\",\"id\":2,\"geometry\":\"LINESTRING (39.7392 104.9903, 39.739 104.9907)\",\"properties\":{\"laneId\":2,\"egressApproach\":1,\"ingressApproach\":0,\"ingressPath\":false,\"egressPath\":true}}";
        WKTMapFeature wktFeature = new WKTMapFeature(properties.getLaneId(), wktGeometry, properties);
        assertEquals(expectedString, wktFeature.toString());
    }

    @Test
    public void testGetId() {
        GeoJsonMapFeature feature = new GeoJsonMapFeature(properties.getLaneId(), geometry, properties);
        assertEquals(2, feature.getId());

        WKTMapFeature wktFeature = new WKTMapFeature(properties.getLaneId(), wktGeometry, properties);
        assertEquals(2, wktFeature.getId());
    }

    @Test
    public void testGeoJsonGetGeometry() {
        GeoJsonMapFeature feature = new GeoJsonMapFeature(properties.getLaneId(), geometry, properties);
        assertEquals(39.7392, feature.getGeometry().getCoordinates()[0][0]);
    }

    @Test
    public void testWKTGetGeometry() {
        WKTMapFeature wktFeature = new WKTMapFeature(properties.getLaneId(), wktGeometry, properties);
        assertEquals("LINESTRING (39.7392 104.9903, 39.739 104.9907)", wktFeature.getGeometry());
    }

    @Test
    public void testGetProperties() {
        GeoJsonMapFeature feature = new GeoJsonMapFeature(properties.getLaneId(), geometry, properties);
        assertEquals(2, feature.getProperties().getLaneId());

        WKTMapFeature wktFeature = new WKTMapFeature(properties.getLaneId(), wktGeometry, properties);
        assertEquals(2, wktFeature.getProperties().getLaneId());
    }

}
