package us.dot.its.jpo.geojsonconverter.pojos.geojson.srm;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Generated;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.time.Duration;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;

/**
 * A SignalRuest within a ProcessedSrm.
 * Includes information about the request, but does not include
 * requestor information or location.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Generated
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@Slf4j
public class ProcessedSignalRequest {
    // Requestor Description properties
    private Integer region;
    private Integer intersectionId;
    private Integer requestID;
    private ProcessedPriorityRequestType priorityRequestType;

    // Intersection Access Point may be either LaneID, ApproachID, or LaneConnectionID

    // Inbound access point is required
    private Integer inboundLaneID;
    private Integer inboundApproachID;
    private Integer inboundLaneConnectionID;

    // Outbound access point is optional
    private Integer outboundLaneID;
    private Integer outboundApproachID;
    private Integer outboundLaneConnectionID;

    // Timestamp of the DF_SignalRequestPackage
    private ZonedDateTime estimatedTimeOfArrival;

    private Duration estimatedTimeOfArrivalDurationSeconds;
}
