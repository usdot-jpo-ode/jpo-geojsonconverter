package us.dot.its.jpo.geojsonconverter.pojos.geojson.map;

import us.dot.its.jpo.geojsonconverter.pojos.ProcessedBitstring;

public class ProcessedLaneAttributes_TrackedVehicle extends ProcessedBitstring {
    public ProcessedLaneAttributes_TrackedVehicle() {
        super("spec-RevocableLane", "spec-commuterRailRoadTrack", "spec-lightRailRoadTrack", "spec-heavyRailRoadTrack", "spec-otherRailType");
    }
}
