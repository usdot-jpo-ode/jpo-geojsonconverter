package us.dot.its.jpo.geojsonconverter.pojos.geojson.bsm;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Generated;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Represents a processed BSM (Basic Safety Message) J2735 AccelerationSet4Way.
 * <p>
 * accelLat - meters per second squared (m/s^2)
 * <p>
 * accelLong - meters per second squared (m/s^2)
 * <p>
 * accelVert - Gs (vertical acceleration)
 * <p>
 * accelYaw - degrees per second (yaw rate)
 */
@Data
@Generated
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonPropertyOrder({"accelLat", "accelLong", "accelVert", "accelYaw"})
@Slf4j
public class ProcessedBsmAccelerationSet4Way {
    private Double accelLat;
    private Double accelLong;
    private Double accelVert;
    private Double accelYaw;
}
