package us.dot.its.jpo.geojsonconverter.serialization.deserializers;

import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.fail;

import org.apache.commons.io.IOUtils;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.Resource;
import org.junit.runner.RunWith;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import us.dot.its.jpo.geojsonconverter.pojos.geojson.LineString;
import us.dot.its.jpo.geojsonconverter.pojos.geojson.map.ProcessedMap;

@SpringBootTest({
    "processed.map.json=classpath:json/sample.processed-map.json",
    "processed.map.wkt.json=classpath:json/sample.processed-map-wkt.json"})
@RunWith(SpringRunner.class)
@ActiveProfiles("test")
public class ProcessedMapDeserializerTest {

    @Test
    public void deserializeExceptionTest() {
        try (ProcessedMapDeserializer<BadClass> deserializer = new ProcessedMapDeserializer<BadClass>(BadClass.class)) {
            assertThrows(RuntimeException.class, () -> {
                deserializer.deserialize("topic", new byte[] { (byte)0 });
            });
        }
    }

    @Test
    public void deserializeNullTest(){
        try (ProcessedMapDeserializer<TestClass> deserializer = new ProcessedMapDeserializer<TestClass>(TestClass.class)) {
            ProcessedMap<TestClass> result = deserializer.deserialize("topic", null);
            assertNull(result);
        }
    }

    @Test
    public void testProcessedMapGeoJsonDeserializer() {
        try (ProcessedMapDeserializer<LineString> serializer = new ProcessedMapDeserializer<LineString>(LineString.class)) {
            byte[] mapBytes = IOUtils.toByteArray(validMapGeoJsonResource.getInputStream()); 

            ProcessedMap<LineString> map = serializer.deserialize("the_topic", mapBytes);
            assertNotNull(map);
            assertEquals(1, map.getMapFeatureCollection().getFeatures().length);
            assertEquals(2, map.getConnectingLanesFeatureCollection().getFeatures().length);
            
        } catch (Exception e) {
            fail("Unexpected exception: " + e);
        }
    }

    @Test
    public void testProcessedMapWKTDeserializer() {
        try (ProcessedMapDeserializer<String> serializer = new ProcessedMapDeserializer<String>(String.class)) {
            byte[] mapBytes = IOUtils.toByteArray(validMapWKTJsonResource.getInputStream()); 

            ProcessedMap<String> map = serializer.deserialize("the_topic", mapBytes);
            assertNotNull(map);
            assertEquals(21, map.getMapFeatureCollection().getFeatures().length);
            assertEquals(
                "LINESTRING (-104.8106324 39.5948319, -104.8111524 39.5948179, -104.811631 39.5948066, -104.8122554 39.5948066, -104.8129861 39.5948073, -104.81601 39.5948229)", 
                map.getMapFeatureCollection().getFeatures()[0].getGeometry());
            
        } catch (Exception e) {
            fail("Unexpected exception: " + e);
        }
    }

    @Value("${processed.map.json}")
    private Resource validMapGeoJsonResource;

    @Value("${processed.map.wkt.json}")
    private Resource validMapWKTJsonResource;

    private class BadClass {
        // Private inner class to break Jackson deserialization
    }
}
