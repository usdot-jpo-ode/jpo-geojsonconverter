package us.dot.its.jpo.geojsonconverter.pojos.common;

import us.dot.its.jpo.geojsonconverter.pojos.ProcessedBitstring;

public class ProcessedBrakeAppliedStatus extends ProcessedBitstring {
    public ProcessedBrakeAppliedStatus() {
        super("unavailable", "leftFront", "leftRear", "rightFront", "rightRear");
    }
}
