
package us.dot.its.jpo.geojsonconverter.pojos.spat;

import javax.annotation.Generated;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "eventState",
    "timing",
    "speeds"
})
@Generated("jsonschema2pojo")
public class MovementEventList {

    @JsonProperty("eventState")
    private String eventState;
    @JsonProperty("timing")
    private TimingChangeDetails timing;
    @JsonProperty("speeds")
    private Double speeds;

    @JsonProperty("eventState")
    public String getEventState() {
        return eventState;
    }

    @JsonProperty("eventState")
    public void setEventState(String eventState) {
        this.eventState = eventState;
    }

    @JsonProperty("timing")
    public TimingChangeDetails getTiming() {
        return timing;
    }

    @JsonProperty("timing")
    public void setTiming(TimingChangeDetails timing) {
        this.timing = timing;
    }

    @JsonProperty("speeds")
    public Double getSpeeds() {
        return speeds;
    }

    @JsonProperty("speeds")
    public void setSpeeds(Double speeds) {
        this.speeds = speeds;
    }

}
