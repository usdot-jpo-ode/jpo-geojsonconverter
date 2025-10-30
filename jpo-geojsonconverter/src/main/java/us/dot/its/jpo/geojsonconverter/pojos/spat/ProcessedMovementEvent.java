
package us.dot.its.jpo.geojsonconverter.pojos.spat;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Generated;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;


/**
 * Contains information regarding the dynamic flow of traffic in relation to a signal group.
 * <p>
 * eventState - The signal group's phase state.
 * <p>
 * timing - The collection of timing details for the signal group.
 * <p>
 * speeds - A list of advisory speeds for the signal group.
 */
@Data
@Generated
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@Slf4j
public class ProcessedMovementEvent {
    private ProcessedMovementPhaseState eventState;
    private TimingChangeDetails timing;
    private ProcessedAdvisorySpeedList speeds;
}
