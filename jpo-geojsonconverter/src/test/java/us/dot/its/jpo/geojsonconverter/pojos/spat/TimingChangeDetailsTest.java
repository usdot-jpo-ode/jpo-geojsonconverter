package us.dot.its.jpo.geojsonconverter.pojos.spat;

import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.ZonedDateTime;

import org.junit.Test;

public class TimingChangeDetailsTest {
    @Test
    public void testGettersSetters() {
        TimingChangeDetails object = new TimingChangeDetails();
        ZonedDateTime date = ZonedDateTime.now();

        object.setStartTime(date);
        ZonedDateTime startResponse = object.getStartTime();
        assertEquals(startResponse, date);

        object.setMinEndTime(date);
        ZonedDateTime minEndTimeResponse = object.getMinEndTime();
        assertEquals(minEndTimeResponse, date);

        object.setMaxEndTime(date);
        ZonedDateTime maxEndTimeResponse = object.getMaxEndTime();
        assertEquals(maxEndTimeResponse, date);

        object.setLikelyTime(date);
        ZonedDateTime likelyTimeResponse = object.getLikelyTime();
        assertEquals(likelyTimeResponse, date);

        object.setConfidence(1);
        Integer confidenceResponse = object.getConfidence();
        assertEquals(confidenceResponse, 1);

        object.setNextTime(date);
        ZonedDateTime nextTimeResponse = object.getNextTime();
        assertEquals(nextTimeResponse, date);
    }

    @Test
    public void testEquals() {
        TimingChangeDetails object = new TimingChangeDetails();
        TimingChangeDetails otherObject = new TimingChangeDetails();
        otherObject.setConfidence(5);

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
        TimingChangeDetails object = new TimingChangeDetails();

        Integer hash = object.hashCode();
        assertNotNull(hash);
    }
    
    @Test
    public void testToString() {
        TimingChangeDetails object = new TimingChangeDetails();

        String string = object.toString();
        assertNotNull(string);
    }
}
