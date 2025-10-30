package us.dot.its.jpo.geojsonconverter.pojos.geojson.map;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import java.time.ZonedDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Generated;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import us.dot.its.jpo.geojsonconverter.pojos.ProcessedValidationMessage;
import us.dot.its.jpo.geojsonconverter.pojos.common.ProcessedIntersectionReferenceID;
import us.dot.its.jpo.geojsonconverter.pojos.common.ProcessedSpeedLimitList;
import us.dot.its.jpo.ode.model.OdeMessageFrameMetadata.Source;

@Data
@Generated
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@Slf4j
public class MapSharedProperties {
    // Default schemaVersion is -1 for older messages that lack a schemaVersion value
    private int schemaVersion = -1;
    private String messageType = "MAP";
    private ZonedDateTime odeReceivedAt;
    private String originIp;
    private String asn1;
    private String intersectionName;
    private Integer region;
    private Integer intersectionId;
    private Integer msgIssueRevision;
    private Integer revision;
    private MapRefPoint refPoint;
    private Boolean cti4501Conformant;
    private List<ProcessedValidationMessage> validationMessages;
    private Integer laneWidth;
    private ProcessedSpeedLimitList speedLimits;
    private Source mapSource;
    private ZonedDateTime timeStamp;

    /**
     * Sets both intersection ID and region with null checks
     * 
     * @param referenceID IntersectionReferenceID
     */
    public void setIntersectionReferenceID(ProcessedIntersectionReferenceID referenceID) {
        if (referenceID != null) {
            setIntersectionId(referenceID.getId());
            setRegion(referenceID.getRegion());
        }
    }
}
