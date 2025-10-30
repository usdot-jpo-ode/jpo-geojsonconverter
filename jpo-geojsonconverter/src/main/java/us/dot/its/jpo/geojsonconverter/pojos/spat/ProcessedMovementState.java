package us.dot.its.jpo.geojsonconverter.pojos.spat;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Generated;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Used to convey various information about the current or future movement state of a designated collection of one or
 * more lanes of a common type.
 * <p>
 * movementName - Human readable name of the intersection movement state.
 * <p>
 * signalGroup - The signal group ID associated with the movement state. Used for mapping to lists of lanes.
 * <p>
 * stateTimeSpeed - A set of movement data with phase and timing information.
 */
@Data
@Generated
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@Slf4j
public class ProcessedMovementState {
    private String movementName;
    private Integer signalGroup;
    private List<ProcessedMovementEvent> stateTimeSpeed = null;
}
