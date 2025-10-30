package us.dot.its.jpo.geojsonconverter.pojos.ssm;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Generated;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import us.dot.its.jpo.geojsonconverter.DateJsonMapper;
import us.dot.its.jpo.geojsonconverter.pojos.ProcessedValidationMessage;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * A Java object representing a processed J2735 MSG_SignalStatusMessage.
 * <p>
 * The SSM may contain responses to multiple requests from multiple vehicles.
 * </p>
 * <p>
 * Similar to a SPAT, the SSM is associated with an intersection, but does not contain any geographic information
 * itself, therefore this is a plain Java object, not a GeoJSON object. It must be matched with a corresponding MAP
 * message to find the intersection location.
 * </p>
 * <p>
 * It is possible, in theory, for an SSM to contain SignalRequests for more than one intersection, but that scenario is
 * not supported by this library. If an SSM containing multiple intersection were received, all but the first one would
 * be discarded by the converter (similar to the behavior for SPATs).
 * </p>
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Generated
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@Slf4j
public class ProcessedSsm {
    private int schemaVersion = -1;
    private final String messageType = "SSM";
    private String asn1;

    // ------------------------------------------------------------------------
    // Metadata from the ODE header
    private ZonedDateTime odeReceivedAt;
    private String originIp;


    // ------------------------------------------------------------------------
    // Data from the MessageFrame payload

    /**
     * Top-level timestamp of MSG_SignalStatusMessage
     */
    private ZonedDateTime timeStamp;

    /**
     * Top-level sequence number (DE_MsgCount) of MSG_SignalStatusMessage
     */
    private Integer sequenceNumber;

    /**
     * Sequence number (DE_MsgCount) of the DF_SignalStatus frame
     */
    private Integer statusSequenceNumber;

    /**
     * DE_RoadRequlatorID
     */
    private Integer region;

    /**
     * DE_IntersectionID
     */
    private Integer intersectionId;

    private List<ProcessedSignalStatus> statusList;

    /*
     * ------------------------------------------------------------------------ 
     * Validation
     * ------------------------------------------------------------------------
     */
    private List<ProcessedValidationMessage> validationMessages = new ArrayList<>();

    public void addValidationMessage(ProcessedValidationMessage message) {
        if (validationMessages == null) {
            validationMessages = new ArrayList<ProcessedValidationMessage>();
        }
        validationMessages.add(message);
    }

    public void addValidationMessages(List<ProcessedValidationMessage> messages) {
        if (validationMessages == null) {
            validationMessages = new ArrayList<>();
        }
        validationMessages.addAll(messages);
    }

    public void addValidationMessage(String message) {
        var validationMessage = new ProcessedValidationMessage();
        validationMessage.setMessage(message);
        addValidationMessage(validationMessage);
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
