package us.dot.its.jpo.geojsonconverter.serialization.deserializers;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.Resource;

import org.apache.commons.io.IOUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import us.dot.its.jpo.geojsonconverter.pojos.geojson.Point;
import us.dot.its.jpo.geojsonconverter.pojos.geojson.bsm.ProcessedBsm;
import us.dot.its.jpo.ode.plugin.j2735.J2735BitString;
import us.dot.its.jpo.ode.plugin.j2735.J2735TransmissionState;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;


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

            var props = bsm.getProperties();
            assertEquals("172.19.0.1", props.getOriginIp());
            assertEquals(new BigDecimal("359.4"), props.getHeading());
            assertEquals("12A7A951", props.getId());
            assertEquals(25, props.getMsgCnt());
            assertEquals(2800, props.getSecMark());
            assertEquals(J2735TransmissionState.UNAVAILABLE, props.getTransmission());
            var accelSet = props.getAccelSet();
            var size = props.getSize();
            assertEquals(208, size.getWidth());
            assertEquals(586, size.getLength());
            assertNotNull(accelSet);
            assertEquals(new BigDecimal(2001), accelSet.getAccelLat());
            assertEquals(new BigDecimal(0), accelSet.getAccelLong());
            assertEquals(new BigDecimal(-127), accelSet.getAccelVert());
            assertEquals(new BigDecimal(0), accelSet.getAccelYaw());
            var accuracy = props.getAccuracy();
            assertNotNull(accuracy);
            assertEquals(new BigDecimal(5), accuracy.getSemiMajor());
            assertEquals(new BigDecimal(2), accuracy.getSemiMinor());
            assertEquals(new BigDecimal(0), accuracy.getOrientation());
            var brakes = props.getBrakes();
            assertNotNull(brakes);
            assertEquals("unavailable", brakes.getTraction());
            assertEquals("unavailable", brakes.getAbs());
            assertEquals("unavailable", brakes.getScs());
            assertEquals("unavailable", brakes.getBrakeBoost());
            assertEquals("unavailable", brakes.getAuxBrakes());
            J2735BitString wheelBrakes = brakes.getWheelBrakes();
            assertNotNull(wheelBrakes);
            assertFalse(wheelBrakes.get("leftFront"));
            assertFalse(wheelBrakes.get("rightFront"));
            assertTrue(wheelBrakes.get("unavailable"));
            assertFalse(wheelBrakes.get("leftRear"));
            assertFalse(wheelBrakes.get("rightRear"));
            var geometry = bsm.getGeometry();
            assertNotNull(geometry);
            assertEquals("Point", geometry.getType());
            var coords = geometry.getCoordinates();
            assertNotNull(coords);
            assertEquals(2, coords.length);
            assertEquals(-105.0342901, coords[0]);
            assertEquals(40.5671913, coords[1]);
            
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
