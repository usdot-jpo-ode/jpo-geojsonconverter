package us.dot.its.jpo.geojsonconverter.pojos.geojson.srm;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Generated;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import us.dot.its.jpo.geojsonconverter.pojos.ProcessedValidationMessage;
import us.dot.its.jpo.geojsonconverter.pojos.common.*;

import java.time.Duration;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Generated
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@Slf4j
public class SrmProperties {

    // Metadata properties
    private int schemaVersion = -1;
    private final String messageType = "SRM";
    private ZonedDateTime odeReceivedAt;
    private String originIp;
    private String asn1;

    // Payload properties

    // Top-level timestamp of the SignalRequestMessage
    private ZonedDateTime timeStamp;
    private Integer sequenceNumber;

    // Fields from DF_RequestorDescription
    // VehicleID is required.  It may be a TemporaryID or StationID.
    private String vehicleID;

    // RequestorType
    private ProcessedBasicVehicleRole role;
    private ProcessedRequestSubRole subrole;
    private ProcessedRequestImportanceLevel importanceLevel;
    private Integer iso3833VehicleType;
    private ProcessedVehicleType hpmsType;

    // Fields from RequestorPositionVector
    private Double latitude;
    private Double longitude;
    private Double elevation;
    private Double heading;
    private ProcessedTransmissionState transmission;
    /**
     * Speed in meters/second.
     */
    private Double speedMetersPerSecond;

    private String name;
    private String routeName;
    private ProcessedTransitVehicleStatus transitStatus;
    private ProcessedTransitVehicleOccupancy transitOccupancy;

    // DE_DeltaTime
    private Duration transitScheduleSeconds;

    private List<ProcessedSignalRequest> requests;

    /* -----------------------------------------------------------------------
        Validation
    ------------------------------------------------------------------------*/
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

}
