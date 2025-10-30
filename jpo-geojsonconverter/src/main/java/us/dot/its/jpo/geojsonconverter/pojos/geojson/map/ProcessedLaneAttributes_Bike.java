package us.dot.its.jpo.geojsonconverter.pojos.geojson.map;

import us.dot.its.jpo.geojsonconverter.pojos.ProcessedBitstring;

public class ProcessedLaneAttributes_Bike extends ProcessedBitstring {
    public ProcessedLaneAttributes_Bike() {
        super("bikeRevocableLane", "pedestrianUseAllowed", "isBikeFlyOverLane", "fixedCycleTime", "biDirectionalCycleTimes", "isolatedByBarrier", "unsignalizedSegmentsPresent");
    }
}
