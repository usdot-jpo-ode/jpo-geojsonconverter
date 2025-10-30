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

@SpringBootTest({"processed.map.json=classpath:json/sample.processed-map.json",
        "processed.map.wkt.json=classpath:json/sample.processed-map-wkt.json"})
@RunWith(SpringRunner.class)
@ActiveProfiles("test")
public class ProcessedMapDeserializerTest {

    @Test
    public void deserializeExceptionTest() {
        try (ProcessedMapDeserializer<BadClass> deserializer = new ProcessedMapDeserializer<BadClass>(BadClass.class)) {
            assertThrows(RuntimeException.class, () -> {
                deserializer.deserialize("topic", new byte[] {(byte) 0});
            });
        }
    }

    @Test
    public void deserializeNullTest() {
        try (ProcessedMapDeserializer<TestClass> deserializer =
                new ProcessedMapDeserializer<TestClass>(TestClass.class)) {
            ProcessedMap<TestClass> result = deserializer.deserialize("topic", null);
            assertNull(result);
        }
    }

    @Test
    public void testProcessedMapGeoJsonDeserializer() {
        try (ProcessedMapDeserializer<LineString> serializer =
                new ProcessedMapDeserializer<LineString>(LineString.class)) {
            byte[] mapBytes = IOUtils.toByteArray(validMapGeoJsonResource.getInputStream());

            ProcessedMap<LineString> map = serializer.deserialize("the_topic", mapBytes);
            assertNotNull(map);
            assertEquals(27, map.getMapFeatureCollection().getFeatures().length);
            assertEquals(17, map.getConnectingLanesFeatureCollection().getFeatures().length);

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
            assertEquals(27, map.getMapFeatureCollection().getFeatures().length);
            assertEquals(
                    "LINESTRING (-105.08731667516962 39.58083233014103, -105.08774523232292 39.581549630858326, -105.08788477758141 39.581774541083234, -105.08809870874727 39.58214795145665, -105.08860236054305 39.58298918229788, -105.08911197406428 39.58384553315423, -105.08961085029375 39.584704764013466)",
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
