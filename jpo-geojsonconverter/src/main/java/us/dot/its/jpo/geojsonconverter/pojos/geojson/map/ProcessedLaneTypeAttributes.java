package us.dot.its.jpo.geojsonconverter.pojos.geojson.map;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ProcessedLaneTypeAttributes {
    private ProcessedLaneAttributes_Vehicle vehicle;
    private ProcessedLaneAttributes_Crosswalk crosswalk;
    private ProcessedLaneAttributes_Bike bikeLane;
    private ProcessedLaneAttributes_Sidewalk sidewalk;
    private ProcessedLaneAttributes_Barrier median;
    private ProcessedLaneAttributes_Striping striping;
    private ProcessedLaneAttributes_TrackedVehicle trackedVehicle;
    private ProcessedLaneAttributes_Parking parking;
}
