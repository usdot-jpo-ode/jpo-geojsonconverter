package us.dot.its.jpo.geojsonconverter.converter;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.Test;

import us.dot.its.jpo.geojsonconverter.pojos.geojson.LineString;
import us.dot.its.jpo.geojsonconverter.pojos.geojson.connectinglanes.ConnectingLanesFeatureCollection;
import us.dot.its.jpo.geojsonconverter.pojos.geojson.connectinglanes.ConnectingLanesProperties;
import us.dot.its.jpo.geojsonconverter.pojos.geojson.connectinglanes.GeoJsonConnectingLanesFeature;
import us.dot.its.jpo.geojsonconverter.pojos.geojson.connectinglanes.WKTConnectingLanesFeature;
import us.dot.its.jpo.geojsonconverter.pojos.geojson.map.GeoJsonMapFeature;
import us.dot.its.jpo.geojsonconverter.pojos.geojson.map.MapFeatureCollection;
import us.dot.its.jpo.geojsonconverter.pojos.geojson.map.MapProperties;
import us.dot.its.jpo.geojsonconverter.pojos.geojson.map.MapSharedProperties;
import us.dot.its.jpo.geojsonconverter.pojos.geojson.map.ProcessedMap;
import us.dot.its.jpo.geojsonconverter.pojos.geojson.map.WKTMapFeature;

public class WKTHandlerTest {
    @Test
    public void testCoordinates2WKTLineString() {
        double[][] coordinates = new double[][] { { 39.7392, 104.9903 }, { 39.7390, 104.9907 } };
        String wktGeometry = WKTHandler.coordinates2WKTLineString(coordinates);
        assertEquals("LINESTRING (39.7392 104.9903, 39.739 104.9907)", wktGeometry);
    }

    @Test
    public void testProcessedMapGeoJSON2WKTg() {
        // Build the GeoJSON MapFeatureCollection
        MapProperties mapProperties = new MapProperties();
        mapProperties.setLaneId(2);
        mapProperties.setEgressApproach(1);
        mapProperties.setIngressApproach(0);
        mapProperties.setIngressPath(false);
        mapProperties.setEgressPath(true);

        double[][] mapCoordinates = new double[][] { { 39.7257, 100.9903 }, { 39.7390, 104.9907 } };
        LineString mapGeometry = new LineString(mapCoordinates);

        GeoJsonMapFeature mapFeature = new GeoJsonMapFeature(2, mapGeometry, mapProperties);
        GeoJsonMapFeature[] mapFeatureList = new GeoJsonMapFeature[] { mapFeature };
        MapFeatureCollection<GeoJsonMapFeature> mapFeatureCollection = new MapFeatureCollection<GeoJsonMapFeature>(mapFeatureList);

        // Build the GeoJSON ConnectingLanesFeatureCollection
        ConnectingLanesProperties clProperties = new ConnectingLanesProperties();
        clProperties.setSignalGroupId(1);
        clProperties.setIngressLaneId(2);
        clProperties.setEgressLaneId(3);

        double[][] clCoordinates = new double[][] { { 39.7392, 104.9903 }, { 39.7390, 104.9907 } };
        LineString clGeometry = new LineString(clCoordinates);

        GeoJsonConnectingLanesFeature clFeature = new GeoJsonConnectingLanesFeature("id", clGeometry, clProperties);
        GeoJsonConnectingLanesFeature[] clFeatureList = new GeoJsonConnectingLanesFeature[] { clFeature };
        ConnectingLanesFeatureCollection<GeoJsonConnectingLanesFeature> clFeatureCollection = new ConnectingLanesFeatureCollection<GeoJsonConnectingLanesFeature>(clFeatureList);

        // Build shared properties
        MapSharedProperties props = new MapSharedProperties();
        props.setCti4501Conformant(true);
        
        ProcessedMap<GeoJsonMapFeature, GeoJsonConnectingLanesFeature> geojsonProcessedMapPojo = new ProcessedMap<GeoJsonMapFeature, GeoJsonConnectingLanesFeature>();
        geojsonProcessedMapPojo.setMapFeatureCollection(mapFeatureCollection);
        geojsonProcessedMapPojo.setConnectingLanesFeatureCollection(clFeatureCollection);
        geojsonProcessedMapPojo.setProperties(props);

        assertEquals(100.9903, geojsonProcessedMapPojo.getMapFeatureCollection().getFeatures()[0].getGeometry().getCoordinates()[0][1]);
        assertEquals(39.7392, geojsonProcessedMapPojo.getConnectingLanesFeatureCollection().getFeatures()[0].getGeometry().getCoordinates()[0][0]);

        ProcessedMap<WKTMapFeature, WKTConnectingLanesFeature> wktProcessedMapPojo = WKTHandler.processedMapGeoJSON2WKT(geojsonProcessedMapPojo);

        assertEquals("LINESTRING (39.7257 100.9903, 39.739 104.9907)", wktProcessedMapPojo.getMapFeatureCollection().getFeatures()[0].getGeometry());
        assertEquals("LINESTRING (39.7392 104.9903, 39.739 104.9907)", wktProcessedMapPojo.getConnectingLanesFeatureCollection().getFeatures()[0].getGeometry());
    }
}
