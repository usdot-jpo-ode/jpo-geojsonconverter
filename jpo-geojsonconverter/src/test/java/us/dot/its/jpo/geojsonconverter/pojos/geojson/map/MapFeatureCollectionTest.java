package us.dot.its.jpo.geojsonconverter.pojos.geojson.map;

import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;

import org.junit.Before;
import org.junit.Test;

import us.dot.its.jpo.geojsonconverter.pojos.geojson.LineString;

public class MapFeatureCollectionTest {
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
        MapFeatureCollection featureCollection = new MapFeatureCollection(featureList);
        assertNotNull(featureCollection);
    }

    @Test
    public void testGeoJSONType() {
        MapFeature[] featureList = new MapFeature[] { feature };
        MapFeatureCollection featureCollection = new MapFeatureCollection(featureList);
        assertEquals("FeatureCollection", featureCollection.getGeoJSONType());
    }

    @Test
    public void testType() {
        MapFeature[] featureList = new MapFeature[] { feature };
        MapFeatureCollection featureCollection = new MapFeatureCollection(featureList);
        assertEquals("FeatureCollection", featureCollection.getType());
        featureCollection.setType("Test");
        assertEquals("FeatureCollection", featureCollection.getType());
    }

    @Test
    public void testToString() {
        String expectedString = "{\"type\":\"FeatureCollection\",\"features\":[{\"type\":\"Feature\",\"id\":2,\"geometry\":{\"type\":\"LineString\",\"coordinates\":[[39.7392,104.9903],[39.739,104.9907]]},\"properties\":{\"nodes\":null,\"messageType\":\"MAP\",\"odeReceivedAt\":\"2022-01-01T00:00:00Z\",\"originIp\":\"10.0.0.1\",\"intersectionName\":null,\"region\":null,\"intersectionId\":null,\"msgIssueRevision\":null,\"revision\":null,\"refPoint\":null,\"cti4501Conformant\":null,\"validationMessages\":null,\"laneWidth\":null,\"speedLimits\":null,\"mapSource\":null,\"timeStamp\":null,\"laneId\":2,\"laneName\":null,\"sharedWith\":null,\"egressApproach\":1,\"ingressApproach\":0,\"ingressPath\":false,\"egressPath\":true,\"maneuvers\":null,\"connectsTo\":null}}]}";
        MapFeature[] featureList = new MapFeature[] { feature };
        MapFeatureCollection featureCollection = new MapFeatureCollection(featureList);
        assertEquals(expectedString, featureCollection.toString());
    }
}
