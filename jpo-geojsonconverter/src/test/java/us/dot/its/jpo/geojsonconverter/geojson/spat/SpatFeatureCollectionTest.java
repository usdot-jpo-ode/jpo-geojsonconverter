package us.dot.its.jpo.geojsonconverter.geojson.spat;

import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.Before;
import org.junit.Test;

import us.dot.its.jpo.geojsonconverter.geojson.Point;

public class SpatFeatureCollectionTest {
    SpatFeature feature;

    @Before
    public void setup() {
        SpatProperties properties = new SpatProperties();
        properties.setSignalGroupId(2);
        properties.setIp("10.0.0.1");
        properties.setOdeReceivedAt("2022-01-01T00:00:00");
        properties.setTimestamp(30200);
        SpatMovementEvent movementEvent = new SpatMovementEvent();
        movementEvent.setEventState("TEST_STATE");
        movementEvent.setStartTime(4000);
        movementEvent.setMinEndTime(12000);
        movementEvent.setMaxEndTime(15000);
        SpatMovementEvent[] movementEventList = new SpatMovementEvent[] { movementEvent };
        properties.setMovementEvents(movementEventList);

        double[] coordinates = new double[] { 39.7392, 104.9903 };
        Point geometry = new Point(coordinates);

        feature = new SpatFeature(2, geometry, properties);
    }

    @Test
    public void testSpatFeatureCollection() {
        SpatFeature[] featureList = new SpatFeature[] { feature };
        SpatFeatureCollection featureCollection = new SpatFeatureCollection(featureList);
        assertNotNull(featureCollection);
    }

    @Test
    public void testGeoJSONType() {
        SpatFeature[] featureList = new SpatFeature[] { feature };
        SpatFeatureCollection featureCollection = new SpatFeatureCollection(featureList);
        assertEquals("FeatureCollection", featureCollection.getGeoJSONType());
    }

    @Test
    public void testType() {
        SpatFeature[] featureList = new SpatFeature[] { feature };
        SpatFeatureCollection featureCollection = new SpatFeatureCollection(featureList);
        assertEquals("FeatureCollection", featureCollection.getType());
        featureCollection.setType("Test");
        assertEquals("FeatureCollection", featureCollection.getType());
    }

    @Test
    public void testToString() {
        String expectedString = "{\"type\":\"FeatureCollection\",\"features\":[{\"type\":\"Feature\",\"id\":2,\"geometry\":{\"type\":\"Point\",\"coordinates\":[39.7392,104.9903]},\"properties\":{\"signal_group_id\":2,\"ip\":\"10.0.0.1\",\"ode_received_at\":\"2022-01-01T00:00:00\",\"timestamp\":30200,\"movement_events\":[{\"event_state\":\"TEST_STATE\",\"start_time\":4000,\"min_end_time\":12000,\"max_end_time\":15000}]}}]}";
        SpatFeature[] featureList = new SpatFeature[] { feature };
        SpatFeatureCollection featureCollection = new SpatFeatureCollection(featureList);
        assertEquals(expectedString, featureCollection.toString());
    }
}
