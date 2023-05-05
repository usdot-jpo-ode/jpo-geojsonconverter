package us.dot.its.jpo.geojsonconverter.pojos.geojson.map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.Test;
import org.junit.Before;

import us.dot.its.jpo.geojsonconverter.converter.WKTHandler;
import us.dot.its.jpo.geojsonconverter.pojos.geojson.LineString;
import us.dot.its.jpo.geojsonconverter.pojos.geojson.connectinglanes.GeoJsonConnectingLanesFeature;
import us.dot.its.jpo.geojsonconverter.pojos.geojson.connectinglanes.WKTConnectingLanesFeature;

public class ProcessedMapPojoTest {
    GeoJsonMapFeature feature;
    WKTMapFeature wktFeature;

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

        feature = new GeoJsonMapFeature(2, geometry, properties);
        wktFeature = new WKTMapFeature(2, wktGeometry, properties);
    }

    @Test
    public void testMapFeatureCollectionGeoJson() {
        GeoJsonMapFeature[] featureList = new GeoJsonMapFeature[] { feature };
        MapFeatureCollection<GeoJsonMapFeature> expectedFeatureCollection = new MapFeatureCollection<GeoJsonMapFeature>(featureList);
        ProcessedMap<GeoJsonMapFeature, GeoJsonConnectingLanesFeature> ProcessedMapPojo = new ProcessedMap<GeoJsonMapFeature, GeoJsonConnectingLanesFeature>();
        ProcessedMapPojo.setMapFeatureCollection(expectedFeatureCollection);
        assertEquals(expectedFeatureCollection, ProcessedMapPojo.getMapFeatureCollection());
    }

    @Test
    public void testMapFeatureCollectionWKT() {
        WKTMapFeature[] featureList = new WKTMapFeature[] { wktFeature };
        MapFeatureCollection<WKTMapFeature> expectedFeatureCollection = new MapFeatureCollection<WKTMapFeature>(featureList);
        ProcessedMap<WKTMapFeature, WKTConnectingLanesFeature> ProcessedMapPojo = new ProcessedMap<WKTMapFeature, WKTConnectingLanesFeature>();
        ProcessedMapPojo.setMapFeatureCollection(expectedFeatureCollection);
        assertEquals(expectedFeatureCollection, ProcessedMapPojo.getMapFeatureCollection());
    }

    @Test
    public void testEquals() {
        ProcessedMap<GeoJsonMapFeature, GeoJsonConnectingLanesFeature> object = new ProcessedMap<GeoJsonMapFeature, GeoJsonConnectingLanesFeature>();
        ProcessedMap<GeoJsonMapFeature, GeoJsonConnectingLanesFeature> otherObject = new ProcessedMap<GeoJsonMapFeature, GeoJsonConnectingLanesFeature>();
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
        ProcessedMap<GeoJsonMapFeature, GeoJsonConnectingLanesFeature> ProcessedMapPojo = new ProcessedMap<GeoJsonMapFeature, GeoJsonConnectingLanesFeature>();
        Integer hash = ProcessedMapPojo.hashCode();
        assertNotNull(hash);
    }
    
    @Test
    public void testToString() {
        ProcessedMap<GeoJsonMapFeature, GeoJsonConnectingLanesFeature> ProcessedMapPojo = new ProcessedMap<GeoJsonMapFeature, GeoJsonConnectingLanesFeature>();
        String string = ProcessedMapPojo.toString();
        assertNotNull(string);
    }
}
