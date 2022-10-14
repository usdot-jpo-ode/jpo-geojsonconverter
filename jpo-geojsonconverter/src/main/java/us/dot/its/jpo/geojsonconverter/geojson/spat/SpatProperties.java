package us.dot.its.jpo.geojsonconverter.geojson.spat;

import com.fasterxml.jackson.annotation.JsonProperty;

public class SpatProperties {
    @JsonProperty("lane_id")
    private Integer laneId;
    @JsonProperty("ip")
    private String ip;
    @JsonProperty("ode_received_at")
    private String odeReceivedAt;

    public void setLaneId(Integer laneId) {
        this.laneId = laneId;
    }

    public Integer getLaneId() {
        return this.laneId;
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
}
