package us.dot.its.jpo.geojsonconverter.pojos.geojson.map;

import us.dot.its.jpo.geojsonconverter.pojos.ProcessedBitstring;

public class ProcessedLaneAttributes_Parking extends ProcessedBitstring {
    public ProcessedLaneAttributes_Parking() {
        super("parkingRevocableLane", "parallelParkingInUse", "headInParkingInUse", "doNotParkZone", "parkingForBusUse", "parkingForTaxiUse", "noPublicParkingUse");
    }
}
