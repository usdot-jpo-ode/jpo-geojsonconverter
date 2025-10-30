package us.dot.its.jpo.geojsonconverter.pojos.geojson.psm;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import java.time.ZonedDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Generated;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import us.dot.its.jpo.geojsonconverter.pojos.ProcessedValidationMessage;

/**
 * Represents a processed PSM (Personal Safety Message) J2735 PositionalAccuracy.
 * <p>
 * schemaVersion - The jpo-geojsonconverter schema version for ProcessedPsm
 * <p>
 * messageType - PSM
 * <p>
 * odeReceivedAt - The time the origin OdePsmJson message was received by the ODE, in UTC
 * <p>
 * timeStamp - The time the origin PSM messages was generated, in UTC
 * <p>
 * originIp - The IP address the origin OdePsmJson message was received from
 * <p>
 * asn1 - The ASN.1 encoded string of the origin J2735 PSM message
 * <p>
 * validationMessages - List of validation messages based on the OdePsmJson schema
 * <p>
 * basicType - Used to describe the type of pedestrian or non-vehicular road user
 * <p>
 * id - The unique identifier for the pedestrian or non-vehicular road user the PSM was generated from at the time
 * <p>
 * msgCnt - The message count of the PSM
 * <p>
 * secMark - Milliseconds within a minute
 * <p>
 * speed - Speed in meters per second (m/s)
 * <p>
 * heading - Degrees
 */
@Data
@Generated
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonPropertyOrder({"schemaVersion", "messageType", "odeReceivedAt", "timeStamp", "originIp", "asn1",
        "validationMessages", "basicType", "id", "msgCnt", "secMark", "speed", "heading"})
@Slf4j
public class PsmProperties {
    // Metadata properties
    // Default schemaVersion is -1 for older messages that lack a schemaVersion value
    private int schemaVersion = -1;
    private String messageType = "PSM";
    private String odeReceivedAt;
    private ZonedDateTime timeStamp;
    private String originIp;
    private String asn1;
    private List<ProcessedValidationMessage> validationMessages = null;

    // Payload properties
    private ProcessedPersonalDeviceUserType basicType;
    private String id;
    private Integer msgCnt;
    private Integer secMark;
    private Double speed;
    private Double heading;
}
