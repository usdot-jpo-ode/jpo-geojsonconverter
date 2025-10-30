package us.dot.its.jpo.geojsonconverter.pojos.geojson.map;

import us.dot.its.jpo.geojsonconverter.pojos.ProcessedBitstring;

public class ProcessedLaneSharing extends ProcessedBitstring {
    public ProcessedLaneSharing() {
        super("overlappingLaneDescriptionProvided", "multipleLanesTreatedAsOneLane", "otherNonMotorizedTrafficTypes", "individualMotorizedVehicleTraffic", "busVehicleTraffic", "taxiVehicleTraffic", "pedestriansTraffic", "cyclistVehicleTraffic", "trackedVehicleTraffic", "reserved");
    }
}
