
package us.dot.its.jpo.geojsonconverter.pojos.spat;

import java.util.List;
import javax.annotation.Generated;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "movementName",
    "signalGroup",
    "stateTimeSpeed"
})
@Generated("jsonschema2pojo")
public class MovementState {

    @JsonProperty("movementName")
    private String movementName;
    @JsonProperty("signalGroup")
    private Integer signalGroup;
    @JsonProperty("stateTimeSpeed")
    private List<MovementEventList> stateTimeSpeed = null;

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
    public List<MovementEventList> getStateTimeSpeed() {
        return stateTimeSpeed;
    }

    @JsonProperty("stateTimeSpeed")
    public void setStateTimeSpeed(List<MovementEventList> stateTimeSpeed) {
        this.stateTimeSpeed = stateTimeSpeed;
    }

}
