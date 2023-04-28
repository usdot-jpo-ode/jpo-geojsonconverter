package us.dot.its.jpo.geojsonconverter.pojos.geojson.map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;
import org.junit.Before;

import us.dot.its.jpo.geojsonconverter.pojos.geojson.LineString;

public class ProcessedMapPojoTest {
    MapFeature feature;

    @Before
    public void setup() {
        MapProperties properties = new MapProperties();
        properties.setLaneId(2);
        properties.setEgressApproach(1);
        properties.setIngressApproach(0);
        properties.setIngressPath(false);
        properties.setEgressPath(true);

        double[][] coordinates = new double[][] { { 39.7392, 104.9903 }, { 39.7390, 104.9907 } };
        LineString geometry = new LineString(coordinates);

        feature = new GeoJSONMapFeature(2, geometry, properties);
    }

    @Test
    public void testMapFeatureCollection() {
        MapFeature[] featureList = new MapFeature[] { feature };
        MapFeatureCollection expectedFeatureCollection = new MapFeatureCollection(featureList);
        ProcessedMap ProcessedMapPojo = new ProcessedMap();
        ProcessedMapPojo.setMapFeatureCollection(expectedFeatureCollection);
        assertEquals(expectedFeatureCollection, ProcessedMapPojo.getMapFeatureCollection());
    }

    @Test
    public void testEquals() {
        ProcessedMap object = new ProcessedMap();
        ProcessedMap otherObject = new ProcessedMap();
        MapSharedProperties props = new MapSharedProperties();
        props.setCti4501Conformant(true);
        otherObject.setProperties(props);

        boolean equals = object.equals(object);
        assertEquals(true, equals);
        
        boolean otherEquals = object.equals(otherObject);
        assertEquals(false, otherEquals);

        String string = "string";
        boolean notEquals = otherObject.equals(string);
        assertEquals(false, notEquals);
    }
    
    @Test
    public void testHashCode() {
        ProcessedMap ProcessedMapPojo = new ProcessedMap();
        Integer hash = ProcessedMapPojo.hashCode();
        assertNotNull(hash);
    }
    
    @Test
    public void testToString() {
        ProcessedMap ProcessedMapPojo = new ProcessedMap();
        String string = ProcessedMapPojo.toString();
        assertNotNull(string);
    }
}
