package us.dot.its.jpo.geojsonconverter.geojson.spat;

import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.Before;
import org.junit.Test;

import us.dot.its.jpo.geojsonconverter.geojson.Point;

public class SpatFeatureTest {
    SpatProperties properties;
    Point geometry;

    @Before
    public void setup() {
        properties = new SpatProperties();
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
        geometry = new Point(coordinates);
    }

    @Test
    public void testSpatFeatureConstructor() {
        SpatFeature feature = new SpatFeature(properties.getSignalGroupId(), geometry, properties);
        assertNotNull(feature);
    }

    @Test
    public void testGeoJSONType() {
        SpatFeature feature = new SpatFeature(properties.getSignalGroupId(), geometry, properties);
        assertEquals("Feature", feature.getGeoJSONType());
    }

    @Test
    public void testType() {
        SpatFeature feature = new SpatFeature(properties.getSignalGroupId(), geometry, properties);
        assertEquals("Feature", feature.getType());
    }

    @Test
    public void testToString() {
        String expectedString = "{\"type\":\"Feature\",\"id\":2,\"geometry\":{\"type\":\"Point\",\"coordinates\":[39.7392,104.9903]},\"properties\":{\"signal_group_id\":2,\"ip\":\"10.0.0.1\",\"ode_received_at\":\"2022-01-01T00:00:00\",\"timestamp\":30200,\"movement_events\":[{\"event_state\":\"TEST_STATE\",\"start_time\":4000,\"min_end_time\":12000,\"max_end_time\":15000}]}}";
        SpatFeature feature = new SpatFeature(properties.getSignalGroupId(), geometry, properties);
        assertEquals(expectedString, feature.toString());
    }

    @Test
    public void testGetId() {
        SpatFeature feature = new SpatFeature(properties.getSignalGroupId(), geometry, properties);
        assertEquals(2, feature.getId());
    }

    @Test
    public void testGetGeometry() {
        SpatFeature feature = new SpatFeature(properties.getSignalGroupId(), geometry, properties);
        assertEquals(39.7392, feature.getGeometry().getCoordinates()[0]);
    }

    @Test
    public void testGetProperties() {
        SpatFeature feature = new SpatFeature(properties.getSignalGroupId(), geometry, properties);
        assertEquals(30200, feature.getProperties().getTimestamp());
    }
}
