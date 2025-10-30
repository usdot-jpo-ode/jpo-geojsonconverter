package us.dot.its.jpo.geojsonconverter.pojos.geojson.map;

import us.dot.its.jpo.geojsonconverter.pojos.ProcessedBitstring;

public class ProcessedLaneAttributes_Striping extends ProcessedBitstring {
    public ProcessedLaneAttributes_Striping() {
        super("stripeToConnectingLanesRevocableLane", "stripeDrawOnLeft", "stripeDrawOnRight", "stripeToConnectingLanesLeft", "stripeToConnectingLanesRight", "stripeToConnectingLanesAhead");
    }
}
