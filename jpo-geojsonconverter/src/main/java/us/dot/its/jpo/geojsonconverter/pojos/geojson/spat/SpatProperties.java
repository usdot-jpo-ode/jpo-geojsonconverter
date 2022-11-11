package us.dot.its.jpo.geojsonconverter.pojos.geojson.spat;

import com.fasterxml.jackson.annotation.JsonProperty;

public class SpatProperties {
    @JsonProperty("signal_group_id")
    private Integer signalGroupId;
    @JsonProperty("ip")
    private String ip;
    @JsonProperty("ode_received_at")
    private String odeReceivedAt;
    @JsonProperty("timestamp")
    private Integer timestamp;
    @JsonProperty("movement_events")
    private SpatMovementEvent[] movementEvents;

    public void setSignalGroupId(Integer signalGroupId) {
        this.signalGroupId = signalGroupId;
    }

    public Integer getSignalGroupId() {
        return this.signalGroupId;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getIp() {
        return this.ip;
    }

    public void setOdeReceivedAt(String odeReceivedAt) {
        this.odeReceivedAt = odeReceivedAt;
    }

    public String getOdeReceivedAt() {
        return this.odeReceivedAt;
    }

    public void setTimestamp(Integer timestamp) {
        this.timestamp = timestamp;
    }

    public Integer getTimestamp() {
        return this.timestamp;
    }

    public void setMovementEvents(SpatMovementEvent[] movementEvents) {
        this.movementEvents = movementEvents;
    }

    public SpatMovementEvent[] getMovementEvents() {
        return this.movementEvents;
    }
}
