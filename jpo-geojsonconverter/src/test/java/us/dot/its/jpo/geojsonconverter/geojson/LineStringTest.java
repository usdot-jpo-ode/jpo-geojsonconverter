package us.dot.its.jpo.geojsonconverter.geojson;

import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.Test;

import us.dot.its.jpo.geojsonconverter.pojos.geojson.LineString;

public class LineStringTest {
    @Test
    public void testConstructor() {
        double[][] coordinates = new double[][] { { 39.7392, 104.9903 }, { 39.7390, 104.9907 } };
        LineString geometry = new LineString(coordinates);
        assertNotNull(geometry);
    }

    @Test
    public void testCoordinates() {
        double[][] coordinates = new double[][] { { 39.7392, 104.9903 }, { 39.7390, 104.9907 } };
        LineString geometry = new LineString(coordinates);
        assertEquals(39.7392, geometry.getCoordinates()[0][0]);
    }

    @Test
    public void testBbox() {
        double[][] coordinates = new double[][] { { 39.7392, 104.9903 }, { 39.7390, 104.9907 } };
        LineString geometry = new LineString(coordinates);
        assertNull(geometry.getBbox());
    }

    @Test
    public void testGeoJSONType() {
        double[][] coordinates = new double[][] { { 39.7392, 104.9903 }, { 39.7390, 104.9907 } };
        LineString geometry = new LineString(coordinates);
        assertEquals("LineString", geometry.getGeoJSONType());
    }
}
