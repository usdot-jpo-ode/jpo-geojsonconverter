package us.dot.its.jpo.geojsonconverter.geojson.spat;

import com.fasterxml.jackson.annotation.JsonProperty;

public class SpatMovementEvent {
    @JsonProperty("event_state")
    private String eventState;
    @JsonProperty("start_time")
    private Integer startTime;
    @JsonProperty("min_end_time")
    private Integer minEndTime;
    @JsonProperty("max_end_time")
    private Integer maxEndTime;

    public void setEventState(String eventState) {
        this.eventState = eventState;
    }

    public String getEventState() {
        return this.eventState;
    }

    public void setStartTime(Integer startTime) {
        this.startTime = startTime;
    }

    public Integer getStartTime() {
        return this.startTime;
    }

    public void setMinEndTime(Integer minEndTime) {
        this.minEndTime = minEndTime;
    }

    public Integer getMinEndTime() {
        return this.minEndTime;
    }

    public void setMaxEndTime(Integer maxEndTime) {
        this.maxEndTime = maxEndTime;
    }

    public Integer getMaxEndTime() {
        return this.maxEndTime;
    }
}
