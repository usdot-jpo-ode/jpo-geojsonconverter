package us.dot.its.jpo.geojsonconverter.pojos.ssm;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Generated;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import us.dot.its.jpo.geojsonconverter.pojos.common.*;

import java.time.Duration;
import java.time.ZonedDateTime;

/**
 * A signal status response within a {@link ProcessedSsm}.
 * Represents a flattened view of an individual DF_SignalStatusPackage frame containing a response to one specific
 * request from a vehicle, and it's parent DF_SignalStatus, representing an intersection.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Generated
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@Slf4j
public class ProcessedSignalStatus {

    // Fields from DF_SignalRequesterInfo
    private String vehicleID;
    private Integer requestID;
    private Integer requesterSequenceNumber;
    private ProcessedBasicVehicleRole requesterRole;
    private ProcessedRequestSubRole requesterSubrole;
    private ProcessedRequestImportanceLevel requestImportanceLevel;
    private Integer requesterIso3833VehicleType;
    private ProcessedVehicleType requesterHpmsType;

    // Fields from inboundOn IntersectionAccessPoint
    private Integer inboundOnLaneID;
    private Integer inboundOnApproachID;
    private Integer inboundOnLaneConnectionID;

    // Fields from outboundOn IntersectionAccessPoint
    private Integer outboundOnLaneID;
    private Integer outboundOnApproachID;
    private Integer outboundOnLaneConnectionID;

    /**
     * ETA from the SignalStatusPackage MinuteOfYear and second/DSecond fields
     */
    private ZonedDateTime estimatedTimeOfArrival;

    /**
     * From DF_SignalStatusPackage.duration
     */
    private Duration estimatedTimeOfArrivalDurationSeconds;

    private ProcessedPrioritizationResponseStatus status;
}
