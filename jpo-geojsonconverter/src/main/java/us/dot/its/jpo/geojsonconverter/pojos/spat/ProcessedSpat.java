package us.dot.its.jpo.geojsonconverter.pojos.spat;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Generated;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;


import us.dot.its.jpo.geojsonconverter.DateJsonMapper;
import us.dot.its.jpo.geojsonconverter.pojos.ProcessedValidationMessage;
import us.dot.its.jpo.geojsonconverter.pojos.common.ProcessedIntersectionReferenceID;

/**
 * Represents a processed SPaT (Signal Phase and Timing) message.
 * <p>
 * schemaVersion - The jpo-geojsonconverter schema version for ProcessedSpat
 * <p>
 * messageType - SPAT
 * <p>
 * odeReceivedAt - The time the origin OdeSpatJson message was received by the ODE, in UTC
 * <p>
 * originIp - The IP address the origin OdeSpatJson message was received from
 * <p>
 * asn1 - The ASN.1 encoded string of the origin J2735 SPAT message
 * <p>
 * validationMessages - List of validation messages based on the OdeSpatJson schema
 * <p>
 * name - The intersection name defined in the SPaT message.
 * <p>
 * region - The region ID associated with the intersection. This may be null when intersection ID is present.
 * <p>
 * intersectionId - The intersection ID associated with the intersection. This may be null when region ID is present.
 * <p>
 * cti4501Conformant - Represents whether the SPaT message conforms to the CTI 4501 standard.
 * <p>
 * revision - The current revision of the SPaT message.
 * <p>
 * status - A bitstring represeting the status of the intersection.
 * <p>
 * utcTimeStamp - The timestamp of the SPaT message in UTC calculated from the SPaT message relative to the
 * odeReceivedAt.
 * <p>
 * enabledLanes - List of enabled lanes in the intersection.
 * <p>
 * states - All states for each signal group in the intersection including phase timestamps based on the calculated
 * utcTimeStamp.
 */
@Data
@Generated
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@Slf4j
public class ProcessedSpat {
    // Default schemaVersion is -1 for older messages that lack a schemaVersion value
    private int schemaVersion = -1;
    private String messageType = "SPAT";
    private String odeReceivedAt;
    private String originIp;
    private String asn1;
    private List<ProcessedValidationMessage> validationMessages = null;
    private String name;
    private Integer region;
    private Integer intersectionId;
    private boolean cti4501Conformant;
    private Integer revision;
    private ProcessedIntersectionStatusObject status;
    private ZonedDateTime utcTimeStamp;
    private List<Integer> enabledLanes = new ArrayList<>();
    private List<ProcessedMovementState> states = null;

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

    @Override
    public String toString() {
        ObjectMapper mapper = DateJsonMapper.getInstance();
        String testReturn = "";
        try {
            testReturn = (mapper.writeValueAsString(this));
        } catch (JsonProcessingException e) {
            log.error(e.getMessage(), e);
        }
        return testReturn;
    }
}
