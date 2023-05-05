package us.dot.its.jpo.geojsonconverter.pojos.geojson.map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.Test;
import org.junit.Before;

import us.dot.its.jpo.geojsonconverter.converter.WKTHandler;
import us.dot.its.jpo.geojsonconverter.pojos.geojson.LineString;

public class ProcessedMapPojoTest {
    MapFeature<LineString> feature;
    MapFeature<String> wktFeature;

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
        String wktGeometry = WKTHandler.coordinates2WKTLineString(coordinates);

        feature = new MapFeature<LineString>(2, geometry, properties);
        wktFeature = new MapFeature<String>(2, wktGeometry, properties);
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testMapFeatureCollectionGeoJson() {
        MapFeature<LineString>[] featureList = new MapFeature[] { feature };
        MapFeatureCollection<LineString> expectedFeatureCollection = new MapFeatureCollection<LineString>(featureList);
        ProcessedMap<LineString> ProcessedMapPojo = new ProcessedMap<LineString>();
        ProcessedMapPojo.setMapFeatureCollection(expectedFeatureCollection);
        assertEquals(expectedFeatureCollection, ProcessedMapPojo.getMapFeatureCollection());
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testMapFeatureCollectionWKT() {
        MapFeature<String>[] featureList = new MapFeature[] { wktFeature };
        MapFeatureCollection<String> expectedFeatureCollection = new MapFeatureCollection<String>(featureList);
        ProcessedMap<String> ProcessedMapPojo = new ProcessedMap<String>();
        ProcessedMapPojo.setMapFeatureCollection(expectedFeatureCollection);
        assertEquals(expectedFeatureCollection, ProcessedMapPojo.getMapFeatureCollection());
    }

    @Test
    public void testEquals() {
        ProcessedMap<LineString> object = new ProcessedMap<LineString>();
        ProcessedMap<LineString> otherObject = new ProcessedMap<LineString>();
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
        ProcessedMap<LineString> ProcessedMapPojo = new ProcessedMap<LineString>();
        Integer hash = ProcessedMapPojo.hashCode();
        assertNotNull(hash);
    }
    
    @Test
    public void testToString() {
        ProcessedMap<LineString> ProcessedMapPojo = new ProcessedMap<LineString>();
        String string = ProcessedMapPojo.toString();
        assertNotNull(string);
    }
}
