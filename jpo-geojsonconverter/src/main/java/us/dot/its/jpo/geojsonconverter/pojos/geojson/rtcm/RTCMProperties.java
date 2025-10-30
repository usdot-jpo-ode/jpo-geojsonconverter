package us.dot.its.jpo.geojsonconverter.pojos.geojson.rtcm;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Generated;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import us.dot.its.jpo.geojsonconverter.pojos.ProcessedValidationMessage;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Decoded properties from RTCM messages
 *
 * <p>Contains fields from the J2735 message required by CIMMS and CTI4501</p>
 *
 * <p>Includes the decoded RTCM payloads with station ID and message type</p>
 *
 * <p>CTI 4501 v01.01: Sec. 4.3.3.5.1 specification:</p>
 * <p>On what to include from the FullPositionVector</p>
 *
 * <p>"The anchorPoint (DF_FullPositionVector) for a connection intersection
 *  shall  include UTC, latitude, longitude, and elevation. UTC is the time
 *  at which the CI receives the corrections information included in
 *  RTCMmessage list from the reference station. The latitude, longitude,
 *  and elevation are at the location of the RTCM reference station antenna.
 *  All other fields in DF_FullPositionVector shall not be included."
 *  </p>
 *
 * <p>Items that must NOT be included per CTI4501 are:
 *  <ul>
 *      <li>MinuteOfTheYear timestamp</li>
 *      <li>RegionalExtension</li>
 *      <li>RTCMheader</li>
 *  </ul>
 *  and are not included in this class, see the validation messages which
 *  indicate if present.</p>
 */
@Data
@Generated
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@Slf4j
public class RTCMProperties {

    /* -----------------------------------------------------------------------
     * Metadata properties
     * ---------------------------------------------------------------------*/
    private int schemaVersion = -1;
    private String messageType = "RTCM";
    private ZonedDateTime odeReceivedAt;
    private String originIp;
    private String asn1;

    /* -----------------------------------------------------------------------
     * CTI 4501 required fields from RTCMcorrections message frame
     * ----------------------------------------------------------------------*/

    /**
     * MsgCount
     */
    private int msgCnt;

    /**
     * RTCM-Revision.  Must equal "rtcmRev3".
     */
    private String rev;

    /* -----------------------------------------------------------------------
     * CTI 4501 required fields from the FullPositionVector
     * ---------------------------------------------------------------------*/

    /**
     * UTC Timestamp in epoch milliseconds
     */
    private Long utcTime;

    /**
     * Longitude in decimal degrees.
     */
    private Double longitude;

    /**
     * Latitude in decimal degrees.
     */
    private Double latitude;

    /**
     * Elevation in meters.
     */
    private Double elevation;

    /* ------------------------------------------------------------------------
       Key fields from the decoded message
     *-----------------------------------------------------------------------*/

    /**
     * RTCM message types included
     */
    private Set<Integer> messageTypes;

    /**
     * Station ID.
     */
    private Integer stationId;


    /**
     * Decoded messages
     */
    private List<DecodedRTCMmessage> messages = new ArrayList<>();

    /* -----------------------------------------------------------------------
       Validation
     ------------------------------------------------------------------------*/
    private List<ProcessedValidationMessage> validationMessages = new ArrayList<>();

    private boolean cti4501Conformant = true;

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
}
