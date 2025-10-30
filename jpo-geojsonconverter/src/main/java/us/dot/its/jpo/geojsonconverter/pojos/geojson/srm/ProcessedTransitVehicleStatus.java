package us.dot.its.jpo.geojsonconverter.pojos.geojson.srm;

import us.dot.its.jpo.geojsonconverter.pojos.ProcessedBitstring;

public class ProcessedTransitVehicleStatus extends ProcessedBitstring {
    public ProcessedTransitVehicleStatus() {
        super("loading", "anADAuse", "aBikeLoad", "doorOpen", "charging", "atStopLine");
    }
}
