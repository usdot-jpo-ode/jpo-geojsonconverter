package us.dot.its.jpo.geojsonconverter.pojos.geojson;

import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.Test;

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

    @Test
    public void testHashCode() {
        double[][] coordinates = new double[][] { { 39.7392, 104.9903 }, { 39.7390, 104.9907 } };
        LineString geometry = new LineString(coordinates);

        Integer hash = geometry.hashCode();
        assertNotNull(hash);
    }

    @Test
    public void testEquals() {
        double[][] coordinates = new double[][] { { 39.7392, 104.9903 }, { 39.7390, 104.9907 } };
        double[][] otherCoordinates = new double[][] { { 1, 1 }, { 2, 2 } };

        LineString object = new LineString(coordinates);
        LineString otherObject = new LineString(otherCoordinates);

        boolean equals = object.equals(object);
        assertEquals(true, equals);
        
        boolean otherEquals = object.equals(otherObject);
        assertEquals(false, otherEquals);

        String string = "string";
        boolean notEquals = otherObject.equals(string);
        assertEquals(false, notEquals);
    }
    
    @Test
    public void testToString() {
        double[][] coordinates = new double[][] { { 39.7392, 104.9903 }, { 39.7390, 104.9907 } };
        LineString geometry = new LineString(coordinates);

        String string = geometry.toString();
        assertNotNull(string);
    }
}
