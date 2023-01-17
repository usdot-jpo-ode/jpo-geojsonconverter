package us.dot.its.jpo.geojsonconverter.serialization.deserializers;

import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.fail;

import java.io.UnsupportedEncodingException;

import org.apache.commons.io.IOUtils;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.Resource;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;

import us.dot.its.jpo.geojsonconverter.pojos.geojson.map.ProcessedMap;
import us.dot.its.jpo.geojsonconverter.pojos.spat.ProcessedSpat;

@SpringBootTest({
    "processed.spat.json=classpath:json/sample.processed-spat.json",
    "processed.map.json=classpath:json/sample.processed-map.json"})
@RunWith(SpringRunner.class)
public class JsonDeserializerTest {
    @Test
    public void deserializeTest() {
        final String testString = "The test string";
        TestClass testObj = new TestClass();
        testObj.setProp(testString);
        byte[] data;
        try {
            data = testObj.toString().getBytes("UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }

        try (JsonDeserializer<TestClass> deserializer = new JsonDeserializer<TestClass>(TestClass.class)) {
            TestClass result = deserializer.deserialize("topic", data);
            assertNotNull(result);
            assertEquals(testObj.getProp(), result.getProp());
        }
    }

    @Test
    public void deserializeExceptionTest() {
        try (JsonDeserializer<BadClass> deserializer = new JsonDeserializer<BadClass>(BadClass.class)) {
            assertThrows(RuntimeException.class, () -> {
                deserializer.deserialize("topic", new byte[] { (byte)0 });
            });
        }
    }

    @Test
    public void deserializeNullTest(){
        try (JsonDeserializer<TestClass> deserializer = new JsonDeserializer<TestClass>(TestClass.class)) {
            TestClass result = deserializer.deserialize("topic", null);
            assertNull(result);
        }
    }

    @Test
    public void testProcessedSpatDeserializer() {
        try (JsonDeserializer<ProcessedSpat> serializer = new JsonDeserializer<ProcessedSpat>(ProcessedSpat.class)) {
            byte[] spatBytes = IOUtils.toByteArray(validSpatJsonResource.getInputStream()); 
            String spatString = new String(spatBytes).strip().replace("\n", "").replace("\r", "").replace(" ", "");

            ProcessedSpat spat = serializer.deserialize("the_topic", spatBytes);
            assertNotNull(spat);
            assertEquals(false, spat.getCti4501Conformant());
            assertEquals("2022-11-17T22:55:28.744Z[UTC]", spat.getUtcTimeStamp().toString());
            assertEquals(spatString, spat.toString().replace(" ", ""));
        } catch (Exception e) {
            fail("Unexpected exception: " + e);
        }
    }

    @Test
    public void testProcessedMapDeserializer() {
        try (JsonDeserializer<ProcessedMap> serializer = new JsonDeserializer<ProcessedMap>(ProcessedMap.class)) {
            byte[] mapBytes = IOUtils.toByteArray(validMapJsonResource.getInputStream()); 

            ProcessedMap map = serializer.deserialize("the_topic", mapBytes);
            assertNotNull(map);
            assertEquals(1, map.getMapFeatureCollection().getFeatures().length);
            assertEquals(2, map.getConnectingLanesFeatureCollection().getFeatures().length);
        } catch (Exception e) {
            fail("Unexpected exception: " + e);
        }
    }
    
    @Value("${processed.spat.json}")
    private Resource validSpatJsonResource;

    @Value("${processed.map.json}")
    private Resource validMapJsonResource;

    private class BadClass {
        // Private inner class to break Jackson deserialization
    }
}

class TestClass {
    public TestClass() {}

    private String prop;
    public String getProp() { return prop; }
    public void setProp(String prop) { this.prop = prop; }
    public String toString() { return String.format("{\"prop\":\"%s\"}", prop); }
}