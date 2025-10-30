package us.dot.its.jpo.geojsonconverter.serialization.deserializers;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.Resource;

import org.apache.commons.io.IOUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import us.dot.its.jpo.asn.j2735.r2024.Common.AntiLockBrakeStatus;
import us.dot.its.jpo.asn.j2735.r2024.Common.AuxiliaryBrakeStatus;
import us.dot.its.jpo.asn.j2735.r2024.Common.BrakeAppliedStatus;
import us.dot.its.jpo.asn.j2735.r2024.Common.BrakeBoostApplied;
import us.dot.its.jpo.asn.j2735.r2024.Common.StabilityControlStatus;
import us.dot.its.jpo.asn.j2735.r2024.Common.TractionControlStatus;
import us.dot.its.jpo.asn.j2735.r2024.Common.TransmissionState;
import us.dot.its.jpo.geojsonconverter.pojos.common.*;
import us.dot.its.jpo.geojsonconverter.pojos.geojson.Point;
import us.dot.its.jpo.geojsonconverter.pojos.geojson.bsm.ProcessedBsm;

import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest({"processed.bsm.json=classpath:json/sample.processed-bsm.json"})
@RunWith(SpringRunner.class)
@ActiveProfiles("test")
public class ProcessedBsmDeserializerTest {
    @Test
    public void deserializeExceptionTest() {
        try (ProcessedBsmDeserializer<BadClass> deserializer = new ProcessedBsmDeserializer<BadClass>(BadClass.class)) {
            assertThrows(RuntimeException.class, () -> {
                deserializer.deserialize("topic", new byte[] {(byte) 0});
            });
        }
    }

    @Test
    public void deserializeNullTest() {
        try (ProcessedBsmDeserializer<TestClass> deserializer =
                new ProcessedBsmDeserializer<TestClass>(TestClass.class)) {
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
            assertEquals("172.18.0.1", props.getOriginIp());
            assertEquals(313.25D, props.getHeading());
            assertEquals("31325433", props.getId());
            assertEquals(37, props.getMsgCnt());
            assertEquals(25399, props.getSecMark());
            assertEquals(ProcessedTransmissionState.UNAVAILABLE, props.getTransmission());
            var accelSet = props.getAccelSet();
            var size = props.getSize();
            assertEquals(190, size.getWidth());
            assertEquals(570, size.getLength());
            assertNotNull(accelSet);
            assertEquals(0D, accelSet.getAccelLat());
            assertEquals(0.27D, accelSet.getAccelLong());
            assertEquals(0D, accelSet.getAccelVert());
            assertEquals(0D, accelSet.getAccelYaw());
            var accuracy = props.getAccuracy();
            assertNotNull(accuracy);
            assertEquals(9.3D, accuracy.getSemiMajor());
            assertEquals(12.05D, accuracy.getSemiMinor());
            assertEquals(null, accuracy.getOrientation());
            var brakes = props.getBrakes();
            assertNotNull(brakes);
            assertEquals(ProcessedTractionControlStatus.UNAVAILABLE, brakes.getTraction());
            assertEquals(ProcessedAntiLockBrakeStatus.UNAVAILABLE, brakes.getAbs());
            assertEquals(ProcessedStabilityControlStatus.UNAVAILABLE, brakes.getScs());
            assertEquals(ProcessedBrakeBoostApplied.UNAVAILABLE, brakes.getBrakeBoost());
            assertEquals(ProcessedAuxiliaryBrakeStatus.UNAVAILABLE, brakes.getAuxBrakes());
            ProcessedBrakeAppliedStatus wheelBrakes = brakes.getWheelBrakes();
            assertNotNull(wheelBrakes);
            var geometry = bsm.getGeometry();
            assertNotNull(geometry);
            assertEquals("Point", geometry.getType());
            var coords = geometry.getCoordinates();
            assertNotNull(coords);
            assertEquals(2, coords.length);
            assertEquals(-105.0317754, coords[0]);
            assertEquals(40.5659938, coords[1]);

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
