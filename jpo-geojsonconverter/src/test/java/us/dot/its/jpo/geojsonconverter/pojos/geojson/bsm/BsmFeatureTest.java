package us.dot.its.jpo.geojsonconverter.pojos.geojson.bsm;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.math.BigDecimal;

import org.junit.Before;
import org.junit.Test;

import us.dot.its.jpo.geojsonconverter.pojos.geojson.Point;
import us.dot.its.jpo.ode.plugin.j2735.J2735AccelerationSet4Way;
import us.dot.its.jpo.ode.plugin.j2735.J2735BrakeSystemStatus;
import us.dot.its.jpo.ode.plugin.j2735.J2735PositionalAccuracy;
import us.dot.its.jpo.ode.plugin.j2735.J2735TransmissionState;
import us.dot.its.jpo.ode.plugin.j2735.J2735VehicleSize;

public class BsmFeatureTest {
    BsmProperties properties;
    Point geometry;

    @Before
    public void setup() {
        J2735AccelerationSet4Way accelSet = new J2735AccelerationSet4Way();
        accelSet.setAccelLat(new BigDecimal(2001));
        accelSet.setAccelLong(new BigDecimal(0));
        accelSet.setAccelVert(new BigDecimal(-127));
        accelSet.setAccelYaw(new BigDecimal(0));

        J2735PositionalAccuracy positionAcc = new J2735PositionalAccuracy();
        positionAcc.setSemiMajor(new BigDecimal(5));
        positionAcc.setSemiMinor(new BigDecimal(2));
        positionAcc.setOrientation(new BigDecimal(0));

        J2735BrakeSystemStatus brakes = new J2735BrakeSystemStatus();
        brakes.setAbs("unavailable");
        brakes.setAuxBrakes("unavailable");
        brakes.setBrakeBoost("unavailable");
        brakes.setScs("unavailable");
        brakes.setTraction("unavailable");

        J2735VehicleSize size = new J2735VehicleSize();
        size.setLength(586);
        size.setWidth(208);

        properties = new BsmProperties();
        properties.setAccelSet(accelSet);
        properties.setAccuracy(positionAcc);
        properties.setAngle(new BigDecimal(10));
        properties.setBrakes(brakes);
        properties.setHeading(new BigDecimal(10));
        properties.setId("12A7A951");
        properties.setMsgCnt(20);
        properties.setSecMark(280);
        properties.setSize(size);
        properties.setSpeed(new BigDecimal(100));
        properties.setTransmission(J2735TransmissionState.FORWARDGEARS);

        double[] coordinates = new double[] { 40.5671913, -105.0342901 };
        geometry = new Point(coordinates);
    }

    @Test
    public void testBsmFeatureConstructor() {
        ProcessedBsm<Point> feature = new ProcessedBsm<Point>(null, geometry, properties);
        assertNotNull(feature);
    }

    @Test
    public void testType() {
        ProcessedBsm<Point> feature = new ProcessedBsm<Point>(null, geometry, properties);
        assertEquals("Feature", feature.getType());
    }

    @Test
    public void testGeoJsonToString() {
        String expectedString = "{\"type\":\"Feature\",\"geometry\":{\"type\":\"Point\",\"coordinates\":[40.5671913,-105.0342901]},\"properties\":{\"accelSet\":{\"accelLat\":2001,\"accelLong\":0,\"accelVert\":-127,\"accelYaw\":0},\"accuracy\":{\"semiMajor\":5,\"semiMinor\":2,\"orientation\":0},\"angle\":10,\"brakes\":{\"traction\":\"unavailable\",\"abs\":\"unavailable\",\"scs\":\"unavailable\",\"brakeBoost\":\"unavailable\",\"auxBrakes\":\"unavailable\"},\"heading\":10,\"id\":\"12A7A951\",\"msgCnt\":20,\"secMark\":280,\"size\":{\"width\":208,\"length\":586},\"speed\":100,\"transmission\":\"FORWARDGEARS\"}}";
        ProcessedBsm<Point> feature = new ProcessedBsm<Point>(null, geometry, properties);
        assertEquals(expectedString, feature.toString());
    }

    @Test
    public void testGetId() {
        ProcessedBsm<Point> feature = new ProcessedBsm<Point>(2, geometry, properties);
        assertEquals(2, feature.getId());
    }

    @Test
    public void testGeoJsonGetGeometry() {
        ProcessedBsm<Point> feature = new ProcessedBsm<Point>(null, geometry, properties);
        assertEquals(40.5671913, feature.getGeometry().getCoordinates()[0]);
    }

    @Test
    public void testGetProperties() {
        ProcessedBsm<Point> feature = new ProcessedBsm<Point>(null, geometry, properties);
        assertEquals("12A7A951", feature.getProperties().getId());
    }
}
