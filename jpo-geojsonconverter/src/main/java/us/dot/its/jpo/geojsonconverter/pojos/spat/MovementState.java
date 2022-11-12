
package us.dot.its.jpo.geojsonconverter.pojos.spat;

import java.util.List;
import com.fasterxml.jackson.annotation.JsonProperty;

public class MovementState {

    @JsonProperty("movementName")
    private String movementName;
    @JsonProperty("signalGroup")
    private Integer signalGroup;
    @JsonProperty("stateTimeSpeed")
    private List<MovementEvent> stateTimeSpeed = null;

    @JsonProperty("movementName")
    public String getMovementName() {
        return movementName;
    }

    @JsonProperty("movementName")
    public void setMovementName(String movementName) {
        this.movementName = movementName;
    }

    @JsonProperty("signalGroup")
    public Integer getSignalGroup() {
        return signalGroup;
    }

    @JsonProperty("signalGroup")
    public void setSignalGroup(Integer signalGroup) {
        this.signalGroup = signalGroup;
    }

    @JsonProperty("stateTimeSpeed")
    public List<MovementEvent> getStateTimeSpeed() {
        return stateTimeSpeed;
    }

    @JsonProperty("stateTimeSpeed")
    public void setStateTimeSpeed(List<MovementEvent> stateTimeSpeed) {
        this.stateTimeSpeed = stateTimeSpeed;
    }

}
