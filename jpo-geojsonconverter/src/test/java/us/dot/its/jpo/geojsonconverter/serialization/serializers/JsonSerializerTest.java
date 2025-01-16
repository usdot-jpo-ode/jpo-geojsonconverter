package us.dot.its.jpo.geojsonconverter.serialization.serializers;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

import static org.junit.Assert.fail;

import org.apache.commons.io.IOUtils;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.Resource;
import org.junit.runner.RunWith;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;

import us.dot.its.jpo.geojsonconverter.DateJsonMapper;
import us.dot.its.jpo.geojsonconverter.pojos.geojson.LineString;
import us.dot.its.jpo.geojsonconverter.pojos.geojson.Point;
import us.dot.its.jpo.geojsonconverter.pojos.geojson.bsm.ProcessedBsm;
import us.dot.its.jpo.geojsonconverter.pojos.geojson.map.ProcessedMap;
import us.dot.its.jpo.geojsonconverter.pojos.spat.ProcessedSpat;


@SpringBootTest({
        "processed.spat.json=classpath:json/sample.processed-spat.json",
        "processed.map.json=classpath:json/sample.processed-map.json",
        "processed.map.wkt.json=classpath:json/sample.processed-map-wkt.json",
        "processed.bsm.json=classpath:json/sample.processed-bsm.json"
})
@RunWith(SpringRunner.class)
@ActiveProfiles("test")
public class JsonSerializerTest {
    @Test
    public void testSerialize() {
        try (JsonSerializer<TestClass> serializer = new JsonSerializer<TestClass>()) {
            TestClass testClass = new TestClass();
            testClass.setProp("Abc");
            byte[] bytes = serializer.serialize("the_topic", testClass);
            assertNotNull(bytes);
            assertTrue(bytes.length > 0);
            assertEquals(testClass.toString(), new String(bytes));
        }
    }

    @Test
    public void testSerializeException() {
        try (JsonSerializer<BadClass> serializer = new JsonSerializer<BadClass>()) {
            BadClass badClass = new BadClass();
            assertNull(serializer.serialize("the_topic", badClass));
        } catch (Exception e) {
            fail("Unexpected exception: " + e);
        }
    }

    @Test
    public void testProcessedSpatSerializer() {
        try (JsonSerializer<ProcessedSpat> serializer = new JsonSerializer<ProcessedSpat>()) {
            BufferedInputStream inputStream = new BufferedInputStream(validSpatJsonResource.getInputStream());
            String spatString = IOUtils.toString(inputStream, "UTF-8"); 
            ObjectMapper mapper = DateJsonMapper.getInstance();
            ProcessedSpat spat = mapper.readValue(spatString, ProcessedSpat.class);
            
            byte[] bytes = serializer.serialize("the_topic", spat);
            assertNotNull(bytes);
            assertTrue(bytes.length > 0);
            assertEquals(spat.toString(), new String(bytes));
        } catch (Exception e) {
            fail("Unexpected exception: " + e);
        }
    }

    @Test
    public void testProcessedMapGeoJsonSerializer() {
        try (JsonSerializer<ProcessedMap<LineString>> serializer = new JsonSerializer<ProcessedMap<LineString>>()) {
            BufferedInputStream inputStream = new BufferedInputStream(validMapGeoJsonResource.getInputStream());
            String mapString = IOUtils.toString(inputStream, "UTF-8"); 
            ObjectMapper mapper = DateJsonMapper.getInstance();
            JavaType javaType = mapper.getTypeFactory().constructParametricType(ProcessedMap.class, LineString.class);
            ProcessedMap<LineString> map = mapper.readValue(mapString, javaType);
            
            byte[] bytes = serializer.serialize("the_topic", map);
            assertNotNull(bytes);
            assertTrue(bytes.length > 0);
            assertEquals(map.toString(), new String(bytes));
        } catch (Exception e) {
            fail("Unexpected exception: " + e);
        }
    }

    @Test
    public void testProcessedMapWKTJsonSerializer() {
        try (JsonSerializer<ProcessedMap<String>> serializer = new JsonSerializer<ProcessedMap<String>>()) {
            BufferedInputStream inputStream = new BufferedInputStream(validMapWKTJsonResource.getInputStream());
            String mapString = IOUtils.toString(inputStream, "UTF-8"); 
            ObjectMapper mapper = DateJsonMapper.getInstance();
            JavaType javaType = mapper.getTypeFactory().constructParametricType(ProcessedMap.class, String.class);
            ProcessedMap<String> map = mapper.readValue(mapString, javaType);
            
            byte[] bytes = serializer.serialize("the_topic", map);
            assertNotNull(bytes);
            assertTrue(bytes.length > 0);
            assertEquals(map.toString(), new String(bytes));
        } catch (Exception e) {
            fail("Unexpected exception: " + e);
        }
    }

    @Test
    public void testProcessedBsmJsonSerializer() throws IOException {
        try (var serializer = new JsonSerializer<ProcessedBsm<Point>>();
            BufferedInputStream inputStream = new BufferedInputStream(validProcessedBsmResource.getInputStream())) {
            final String bsmString = IOUtils.toString(inputStream, StandardCharsets.UTF_8);
            final ObjectMapper mapper = DateJsonMapper.getInstance();
            final JavaType javaType = mapper.getTypeFactory().constructParametricType(ProcessedBsm.class, Point.class);
            final ProcessedBsm<Point> bsm = mapper.readValue(bsmString, javaType);
            final byte[] bytes = serializer.serialize("the_topic", bsm);
            assertNotNull(bytes);
            assertTrue(bytes.length > 0);
            assertEquals(bsm.toString(), new String(bytes));
        }
    }

    @Value("${processed.spat.json}")
    private Resource validSpatJsonResource;

    @Value("${processed.map.json}")
    private Resource validMapGeoJsonResource;

    @Value("${processed.map.wkt.json}")
    private Resource validMapWKTJsonResource;

    @Value("${processed.bsm.json}")
    private Resource validProcessedBsmResource;

    private class BadClass {
        // Class with no properties to break Jackson serialization
    }

}

class TestClass {
    public TestClass() {}

    private String prop;
    public String getProp() { return prop; }
    public void setProp(String prop) { this.prop = prop; }
    public String toString() { return String.format("{\"prop\":\"%s\"}", prop); }
}