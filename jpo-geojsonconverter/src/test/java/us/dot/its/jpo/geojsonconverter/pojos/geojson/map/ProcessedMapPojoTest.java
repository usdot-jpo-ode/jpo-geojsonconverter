package us.dot.its.jpo.geojsonconverter.pojos.geojson.map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;
import org.junit.Before;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;

import us.dot.its.jpo.geojsonconverter.pojos.geojson.LineString;

public class ProcessedMapPojoTest {
    MapFeature feature;

    @Before
    public void setup() {
        MapProperties properties = new MapProperties();
        properties.setLaneId(2);
        properties.setOriginIp("10.0.0.1");
        ZonedDateTime testDate = Instant.parse("2022-01-01T00:00:00Z").atZone(ZoneId.of("UTC"));
        properties.setOdeReceivedAt(testDate);
        properties.setEgressApproach(1);
        properties.setIngressApproach(0);
        properties.setIngressPath(false);
        properties.setEgressPath(true);

        double[][] coordinates = new double[][] { { 39.7392, 104.9903 }, { 39.7390, 104.9907 } };
        LineString geometry = new LineString(coordinates);

        feature = new MapFeature(2, geometry, properties);
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
        boolean equals = object.equals(object);
        assertEquals(equals, true);
        
        boolean otherEquals = object.equals(otherObject);
        assertEquals(otherEquals, true);

        String string = "string";
        boolean notEquals = otherObject.equals(string);
        assertEquals(notEquals, false);
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
