package us.dot.its.jpo.geojsonconverter.pojos.common;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.Generated;

@Generated
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class ProcessedBrakeSystemStatus {
    private ProcessedBrakeAppliedStatus wheelBrakes;
    private ProcessedTractionControlStatus traction;
    private ProcessedAntiLockBrakeStatus abs;
    private ProcessedStabilityControlStatus scs;
    private ProcessedBrakeBoostApplied brakeBoost;
    private ProcessedAuxiliaryBrakeStatus auxBrakes;
}
