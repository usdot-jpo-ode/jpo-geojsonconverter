package us.dot.its.jpo.geojsonconverter.pojos.geojson.map;

import us.dot.its.jpo.geojsonconverter.pojos.ProcessedBitstring;

public class ProcessedLaneAttributes_Crosswalk extends ProcessedBitstring {
    public ProcessedLaneAttributes_Crosswalk() {
        super("crosswalkRevocableLane", "bicyleUseAllowed", "isXwalkFlyOverLane", "fixedCycleTime", "biDirectionalCycleTimes", "hasPushToWalkButton", "audioSupport", "rfSignalRequestPresent", "unsignalizedSegmentsPresent");
    }
}
