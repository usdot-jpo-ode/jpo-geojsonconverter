package us.dot.its.jpo.geojsonconverter.pojos.spat;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

public class TimingChangeDetailsTest {
    @Test
    public void testGettersSetters() {
        TimingChangeDetails object = new TimingChangeDetails();

        object.setStartTime("startTime");
        String startResponse = object.getStartTime();
        assertEquals(startResponse, "startTime");

        object.setMinEndTime("minEndTime");
        String minEndTimeResponse = object.getMinEndTime();
        assertEquals(minEndTimeResponse, "minEndTime");

        object.setMaxEndTime("maxEndTime");
        String maxEndTimeResponse = object.getMaxEndTime();
        assertEquals(maxEndTimeResponse, "maxEndTime");

        object.setLikelyTime("likelyTime");
        String likelyTimeResponse = object.getLikelyTime();
        assertEquals(likelyTimeResponse, "likelyTime");

        object.setConfidence(1);
        Integer confidenceResponse = object.getConfidence();
        assertEquals(confidenceResponse, 1);

        object.setNextTime("nextTime");
        String nextTimeResponse = object.getNextTime();
        assertEquals(nextTimeResponse, "nextTime");
    }
}
