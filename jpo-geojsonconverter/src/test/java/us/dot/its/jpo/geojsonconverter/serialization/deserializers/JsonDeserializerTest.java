package us.dot.its.jpo.geojsonconverter.serialization.deserializers;

import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.Assert.assertThrows;

import java.io.UnsupportedEncodingException;

import org.junit.Test;

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