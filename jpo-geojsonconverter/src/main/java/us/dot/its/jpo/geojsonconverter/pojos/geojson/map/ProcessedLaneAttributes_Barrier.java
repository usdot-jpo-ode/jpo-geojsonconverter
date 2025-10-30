package us.dot.its.jpo.geojsonconverter.pojos.geojson.map;

import us.dot.its.jpo.geojsonconverter.pojos.ProcessedBitstring;

public class ProcessedLaneAttributes_Barrier extends ProcessedBitstring {
    public ProcessedLaneAttributes_Barrier() {
        super("median-RevocableLane", "median", "whiteLineHashing", "stripedLines", "doubleStripedLines", "trafficCones", "constructionBarrier", "trafficChannels", "lowCurbs", "highCurbs");
    }
}
