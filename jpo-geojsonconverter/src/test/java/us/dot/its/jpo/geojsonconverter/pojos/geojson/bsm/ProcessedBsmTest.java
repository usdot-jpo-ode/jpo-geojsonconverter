package us.dot.its.jpo.geojsonconverter.pojos.geojson.bsm;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.math.BigDecimal;

import org.junit.Before;
import org.junit.Test;

import us.dot.its.jpo.geojsonconverter.pojos.geojson.LineString;
import us.dot.its.jpo.geojsonconverter.pojos.geojson.Point;
import us.dot.its.jpo.geojsonconverter.pojos.geojson.map.ProcessedMap;
import us.dot.its.jpo.ode.plugin.j2735.J2735AccelerationSet4Way;
import us.dot.its.jpo.ode.plugin.j2735.J2735BrakeSystemStatus;
import us.dot.its.jpo.ode.plugin.j2735.J2735PositionalAccuracy;
import us.dot.its.jpo.ode.plugin.j2735.J2735TransmissionState;
import us.dot.its.jpo.ode.plugin.j2735.J2735VehicleSize;

public class ProcessedBsmTest {
    ProcessedBsm<Point> feature;

    @Before
    public void setup() {
        BsmProperties properties = new BsmProperties();
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
        Point geometry = new Point(coordinates);

        feature = new ProcessedBsm<Point>(null, geometry, properties);
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testBsmFeatureCollectionGeoJson() {
        ProcessedBsm<Point>[] expectedFeatureList = new ProcessedBsm[] { feature };
        ProcessedBsmCollection<Point> object = new ProcessedBsmCollection<Point>(expectedFeatureList);
        assertEquals(expectedFeatureList, object.getFeatures());
    }

    @Test
    public void testEquals() {
        ProcessedBsmCollection<Point> object = new ProcessedBsmCollection<Point>(null);
        object.setOriginIp("10.0.0.15");
        ProcessedBsmCollection<Point> otherObject = new ProcessedBsmCollection<Point>(null);

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
        ProcessedMap<LineString> ProcessedMapPojo = new ProcessedMap<LineString>();
        Integer hash = ProcessedMapPojo.hashCode();
        assertNotNull(hash);
    }
    
    @Test
    public void testToString() {
        ProcessedMap<LineString> ProcessedMapPojo = new ProcessedMap<LineString>();
        String string = ProcessedMapPojo.toString();
        assertNotNull(string);
    }
}
