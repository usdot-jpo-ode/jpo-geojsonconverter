package us.dot.its.jpo.geojsonconverter.serialization.deserializers;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.Resource;

import static org.junit.Assert.assertNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.fail;

import org.apache.commons.io.IOUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import us.dot.its.jpo.geojsonconverter.pojos.geojson.Point;
import us.dot.its.jpo.geojsonconverter.pojos.geojson.bsm.ProcessedBsm;

@SpringBootTest({
    "processed.bsm.json=classpath:json/sample.processed-bsm.json"})
@RunWith(SpringRunner.class)
@ActiveProfiles("test")
public class ProcessedBsmDeserializerTest {
    @Test
    public void deserializeExceptionTest() {
        try (ProcessedBsmDeserializer<BadClass> deserializer = new ProcessedBsmDeserializer<BadClass>(BadClass.class)) {
            assertThrows(RuntimeException.class, () -> {
                deserializer.deserialize("topic", new byte[] { (byte)0 });
            });
        }
    }

    @Test
    public void deserializeNullTest(){
        try (ProcessedBsmDeserializer<TestClass> deserializer = new ProcessedBsmDeserializer<TestClass>(TestClass.class)) {
            ProcessedBsm<TestClass> result = deserializer.deserialize("topic", null);
            assertNull(result);
        }
    }

    @Test
    public void testProcessedBsmJsonDeserializer() {
        try (ProcessedBsmDeserializer<Point> serializer = new ProcessedBsmDeserializer<Point>(Point.class)) {
            byte[] bsmBytes = IOUtils.toByteArray(validBsmGeoJsonResource.getInputStream()); 

            ProcessedBsm<Point> bsm = serializer.deserialize("the_topic", bsmBytes);
            assertNotNull(bsm);
            assertEquals(1, bsm.getFeatures().length);
            assertEquals("172.19.0.1", bsm.getOriginIp());
            
        } catch (Exception e) {
            fail("Unexpected exception: " + e);
        }
    }

    @Value("${processed.bsm.json}")
    private Resource validBsmGeoJsonResource;

    private class BadClass {
        // Private inner class to break Jackson deserialization
    }
}
