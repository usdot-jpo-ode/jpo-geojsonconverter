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
 * Represents a processed BSM (Basic Safety Message) J2735 PositionalAccuracy.
 * <p>
 * semiMajor - meters (m)
 * <p>
 * semiMinor - meters (m)
 * <p>
 * orientation - Degrees relative to true north
 */
@Data
@Generated
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonPropertyOrder({"semiMajor", "semiMinor", "orientation"})
@Slf4j
public class ProcessedBsmPositionalAccuracy {
    private Double semiMajor;
    private Double semiMinor;
    private Double orientation;
}
