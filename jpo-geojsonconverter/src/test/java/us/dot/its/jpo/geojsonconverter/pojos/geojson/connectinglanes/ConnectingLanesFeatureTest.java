package us.dot.its.jpo.geojsonconverter.pojos.geojson.connectinglanes;

import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;

import org.junit.Before;
import org.junit.Test;

import us.dot.its.jpo.geojsonconverter.pojos.geojson.LineString;

public class ConnectingLanesFeatureTest {
    ConnectingLanesProperties properties;
    LineString geometry;

    @Before
    public void setup() {
        properties = new ConnectingLanesProperties();
        properties.setSignalGroupId(1);
        properties.setIngressLaneId(2);
        properties.setEgressLaneId(3);

        double[][] coordinates = new double[][] { { 39.7392, 104.9903 }, { 39.7390, 104.9907 } };
        geometry = new LineString(coordinates);
    }

    // @Test
    // public void testConnectingLanesFeatureConstructor() {
    //     ConnectingLanesFeature feature = new ConnectingLanesFeature(properties.getLaneId(), geometry, properties);
    //     assertNotNull(feature);
    // }

    // @Test
    // public void testGeoJSONType() {
    //     ConnectingLanesFeature feature = new ConnectingLanesFeature(properties.getLaneId(), geometry, properties);
    //     assertEquals("Feature", feature.getGeoJSONType());
    // }

    // @Test
    // public void testType() {
    //     ConnectingLanesFeature feature = new ConnectingLanesFeature(properties.getLaneId(), geometry, properties);
    //     assertEquals("Feature", feature.getType());
    // }

    // @Test
    // public void testToString() {
    //     String expectedString = "{\"type\":\"Feature\",\"id\":2,\"geometry\":{\"type\":\"LineString\",\"coordinates\":[[39.7392,104.9903],[39.739,104.9907]]},\"properties\":{\"nodes\":null,\"messageType\":\"MAP\",\"odeReceivedAt\":\"2022-01-01T00:00:00Z\",\"originIp\":\"10.0.0.1\",\"intersectionName\":null,\"region\":null,\"intersectionId\":null,\"msgIssueRevision\":null,\"revision\":null,\"refPoint\":null,\"cti4501Conformant\":null,\"validationMessages\":null,\"laneWidth\":null,\"speedLimits\":null,\"mapSource\":null,\"timeStamp\":null,\"laneId\":2,\"laneName\":null,\"sharedWith\":null,\"egressApproach\":1,\"ingressApproach\":0,\"ingressPath\":false,\"egressPath\":true,\"maneuvers\":null,\"connectsTo\":null}}";
    //     ConnectingLanesFeature feature = new ConnectingLanesFeature(properties.getLaneId(), geometry, properties);
    //     assertEquals(expectedString, feature.toString());
    // }

    // @Test
    // public void testGetId() {
    //     ConnectingLanesFeature feature = new ConnectingLanesFeature(properties.getLaneId(), geometry, properties);
    //     assertEquals(2, feature.getId());
    // }

    // @Test
    // public void testGetGeometry() {
    //     ConnectingLanesFeature feature = new ConnectingLanesFeature(properties.getLaneId(), geometry, properties);
    //     assertEquals(39.7392, feature.getGeometry().getCoordinates()[0][0]);
    // }

    // @Test
    // public void testGetProperties() {
    //     ConnectingLanesFeature feature = new ConnectingLanesFeature(properties.getLaneId(), geometry, properties);
    //     assertEquals(2, feature.getProperties().getLaneId());
    // }
}
