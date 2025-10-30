package us.dot.its.jpo.geojsonconverter.pojos.geojson.map;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import us.dot.its.jpo.geojsonconverter.pojos.common.ProcessedIntersectionReferenceID;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class ProcessedConnection {
    private ProcessedConnectingLane connectingLane;
    private ProcessedIntersectionReferenceID remoteIntersection;
    private Integer signalGroup;
    private Integer userClass;
    private Integer connectionID;
}
