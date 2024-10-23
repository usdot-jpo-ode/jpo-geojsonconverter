package us.dot.its.jpo.geojsonconverter.pojos.geojson.bsm;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.math.BigDecimal;

import org.junit.Test;

import us.dot.its.jpo.ode.plugin.j2735.J2735AccelerationSet4Way;
import us.dot.its.jpo.ode.plugin.j2735.J2735BrakeSystemStatus;
import us.dot.its.jpo.ode.plugin.j2735.J2735PositionalAccuracy;
import us.dot.its.jpo.ode.plugin.j2735.J2735TransmissionState;
import us.dot.its.jpo.ode.plugin.j2735.J2735VehicleSize;

public class BsmPropertiesTest {
    @Test
    public void testAccelSet() {
        J2735AccelerationSet4Way expectedAccelSet = new J2735AccelerationSet4Way();
        expectedAccelSet.setAccelLat(new BigDecimal(2001));
        expectedAccelSet.setAccelLong(new BigDecimal(0));
        expectedAccelSet.setAccelVert(new BigDecimal(-127));
        expectedAccelSet.setAccelYaw(new BigDecimal(0));

        BsmProperties bsmProperties = new BsmProperties();
        bsmProperties.setAccelSet(expectedAccelSet);
        assertEquals(expectedAccelSet.getAccelLat(), bsmProperties.getAccelSet().getAccelLat());
    }

    @Test
    public void testAccuracy() {
        J2735PositionalAccuracy expectedPositionAcc = new J2735PositionalAccuracy();
        expectedPositionAcc.setSemiMajor(new BigDecimal(5));
        expectedPositionAcc.setSemiMinor(new BigDecimal(2));
        expectedPositionAcc.setOrientation(new BigDecimal(0));

        BsmProperties bsmProperties = new BsmProperties();
        bsmProperties.setAccuracy(expectedPositionAcc);
        assertEquals(expectedPositionAcc.getSemiMajor(), bsmProperties.getAccuracy().getSemiMajor());
    }

    @Test
    public void testAngle() {
        BigDecimal expectedAngle = new BigDecimal(0.0);
        BsmProperties bsmProperties = new BsmProperties();
        bsmProperties.setAngle(expectedAngle);
        assertEquals(expectedAngle, bsmProperties.getAngle());
    }

    @Test
    public void testBrakes() {
        J2735BrakeSystemStatus expectedBrakes = new J2735BrakeSystemStatus();
        expectedBrakes.setAbs("unavailable");
        expectedBrakes.setAuxBrakes("unavailable");
        expectedBrakes.setBrakeBoost("unavailable");
        expectedBrakes.setScs("unavailable");
        expectedBrakes.setTraction("unavailable");

        BsmProperties bsmProperties = new BsmProperties();
        bsmProperties.setBrakes(expectedBrakes);
        assertEquals(expectedBrakes.getAbs(), bsmProperties.getBrakes().getAbs());
    }

    @Test
    public void testHeading() {
        BigDecimal expectedHeading = new BigDecimal(0.0);
        BsmProperties bsmProperties = new BsmProperties();
        bsmProperties.setHeading(expectedHeading);
        assertEquals(expectedHeading, bsmProperties.getHeading());
    }

    @Test
    public void testId() {
        String expectedId = new String("test");
        BsmProperties bsmProperties = new BsmProperties();
        bsmProperties.setId(expectedId);
        assertEquals(expectedId, bsmProperties.getId());
    }

    @Test
    public void testSecMark() {
        Integer expectedSecMark = 180;
        BsmProperties bsmProperties = new BsmProperties();
        bsmProperties.setSecMark(expectedSecMark);
        assertEquals(expectedSecMark, bsmProperties.getSecMark());
    }

    @Test
    public void testMsgCnt() {
        Integer expectedMsgCnt = 20;
        BsmProperties bsmProperties = new BsmProperties();
        bsmProperties.setMsgCnt(expectedMsgCnt);
        assertEquals(expectedMsgCnt, bsmProperties.getMsgCnt());
    }

    @Test
    public void testSize() {
        J2735VehicleSize expectedSize = new J2735VehicleSize();
        expectedSize.setLength(586);
        expectedSize.setWidth(208);

        BsmProperties bsmProperties = new BsmProperties();
        bsmProperties.setSize(expectedSize);
        assertEquals(expectedSize.getWidth(), bsmProperties.getSize().getWidth());
    }

    @Test
    public void testSpeed() {
        BigDecimal expectedSpeed = new BigDecimal(100.0);
        BsmProperties bsmProperties = new BsmProperties();
        bsmProperties.setSpeed(expectedSpeed);
        assertEquals(expectedSpeed, bsmProperties.getSpeed());
    }

    @Test
    public void testTransmission() {
        J2735TransmissionState expectedTransmission = J2735TransmissionState.FORWARDGEARS;
        BsmProperties bsmProperties = new BsmProperties();
        bsmProperties.setTransmission(expectedTransmission);
        assertEquals(expectedTransmission, bsmProperties.getTransmission());
    }

    @Test
    public void testEquals() {
        BsmProperties object = new BsmProperties();
        BsmProperties otherObject = new BsmProperties();
        otherObject.setId("test");
        
        boolean equals = object.equals(object);
        assertEquals(true, equals);
        
        boolean otherEquals = object.equals(otherObject);
        assertEquals(false, otherEquals);

        String string = "string";
        boolean notEquals = otherObject.equals(string);
        assertEquals(false, notEquals);
    }
    
    @Test
    public void testHashCode() {
        BsmProperties bsmProperties = new BsmProperties();
        Integer hash = bsmProperties.hashCode();
        assertNotNull(hash);
    }
    
    @Test
    public void testToString() {
        BsmProperties bsmProperties = new BsmProperties();
        String string = bsmProperties.toString();
        assertNotNull(string);
    }
}
