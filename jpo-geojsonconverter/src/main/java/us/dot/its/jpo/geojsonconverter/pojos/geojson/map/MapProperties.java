package us.dot.its.jpo.geojsonconverter.pojos.geojson.map;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Generated;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Data
@Generated
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@Slf4j
public class MapProperties {
    private List<MapNode> nodes;
    private Integer laneId;
    private String laneName;
    private ProcessedLaneTypeAttributes laneType;
    private ProcessedLaneSharing sharedWith; // enum is of type J2735LaneSharing
    private Integer egressApproach;
    private Integer ingressApproach;
    private Boolean ingressPath;
    private Boolean egressPath;
    private ProcessedAllowedManeuvers maneuvers; // enum is of type J2735AllowedManeuvers
    private ProcessedConnectsToList connectsTo;
}
