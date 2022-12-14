package us.dot.its.jpo.geojsonconverter.pojos.geojson;

import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.Test;


public class PointTest {
    @Test
    public void testNonListConstructor() {
        Point geometry = new Point(39.7392, 104.9903);
        assertNotNull(geometry);
    }

    @Test
    public void testListConstructor() {
        double[] coordinates = new double[] { 39.7392, 104.9903 };
        Point geometry = new Point(coordinates);
        assertNotNull(geometry);
    }

    @Test
    public void testCoordinates() {
        double[] coordinates = new double[] { 39.7392, 104.9903 };
        Point geometry = new Point(coordinates);
        assertEquals(39.7392, geometry.getCoordinates()[0]);
    }

    @Test
    public void testBbox() {
        double[] coordinates = new double[] { 39.7392, 104.9903 };
        Point geometry = new Point(coordinates);
        assertNull(geometry.getBbox());
    }

    @Test
    public void testGeoJSONType() {
        double[] coordinates = new double[] { 39.7392, 104.9903 };
        Point geometry = new Point(coordinates);
        assertEquals("Point", geometry.getGeoJSONType());
    }

    @Test
    public void testHashCode() {
        double[] coordinates = new double[] { 39.7392, 104.9903 };
        Point geometry = new Point(coordinates);

        Integer hash = geometry.hashCode();
        assertNotNull(hash);
    }
    
    @Test
    public void testEquals() {
        double[] coordinates = new double[] { 39.7392, 104.9903 };
        double[] otherCoordinates = new double[] { 1, 1 };

        Point object = new Point(coordinates);
        Point otherObject = new Point(otherCoordinates);

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
        double[] coordinates = new double[] { 39.7392, 104.9903 };
        Point geometry = new Point(coordinates);

        String string = geometry.toString();
        assertNotNull(string);
    }
}
