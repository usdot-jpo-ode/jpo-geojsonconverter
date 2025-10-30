package us.dot.its.jpo.geojsonconverter.pojos.geojson.map;

import us.dot.its.jpo.geojsonconverter.pojos.ProcessedBitstring;

public class ProcessedLaneAttributes_Sidewalk extends ProcessedBitstring {
    public ProcessedLaneAttributes_Sidewalk() {
        super("sidewalk-RevocableLane", "bicyleUseAllowed", "isSidewalkFlyOverLane", "walkBikes");
    }
}
