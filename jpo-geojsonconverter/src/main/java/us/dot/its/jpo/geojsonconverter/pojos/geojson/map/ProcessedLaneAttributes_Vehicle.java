package us.dot.its.jpo.geojsonconverter.pojos.geojson.map;

import us.dot.its.jpo.geojsonconverter.pojos.ProcessedBitstring;

public class ProcessedLaneAttributes_Vehicle extends ProcessedBitstring {
    public ProcessedLaneAttributes_Vehicle() {
        super("isVehicleRevocableLane", "isVehicleFlyOverLane", "hovLaneUseOnly", "restrictedToBusUse", "restrictedToTaxiUse", "restrictedFromPublicUse", "hasIRbeaconCoverage", "permissionOnRequest");
    }
}
