package us.dot.its.jpo.geojsonconverter.serialization.serializers;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.Assert.fail;

import org.junit.Test;

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