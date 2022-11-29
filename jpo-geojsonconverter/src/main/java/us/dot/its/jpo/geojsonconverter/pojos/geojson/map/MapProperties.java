package us.dot.its.jpo.geojsonconverter.pojos.geojson.map;

import com.fasterxml.jackson.annotation.JsonProperty;

import us.dot.its.jpo.ode.plugin.j2735.OdePosition3D;

public class MapProperties {
    @JsonProperty("nodes")
    private MapNode[] nodes;
    @JsonProperty("messageType")
    private String messageType;
    @JsonProperty("odeReceivedAt")
    private String odeReceivedAt;
    @JsonProperty("originIp")
    private String originIp;
    @JsonProperty("intersectionName")
    private String intersectionName;
    @JsonProperty("region")
    private Integer region;
    @JsonProperty("intersectionId")
    private Integer intersectionId;
    @JsonProperty("msgIssueRevision")
    private Integer msgIssueRevision;
    @JsonProperty("revision")
    private Integer revision;
    @JsonProperty("refPoint")
    private OdePosition3D refPoint;
    @JsonProperty("cti4501Conformant")
    private Boolean cti4501Conformant;



    @JsonProperty("lane_id")
    private Integer laneId;
    @JsonProperty("ip")
    private String ip;
    @JsonProperty("egress_approach")
    private Integer egressApproach;
    @JsonProperty("ingress_approach")
    private Integer ingressApproach;
    @JsonProperty("ingress_path")
    private Boolean ingressPath;
    @JsonProperty("egress_path")
    private Boolean egressPath;
    @JsonProperty("connected_lanes")
    private Integer[] connectedLanes;

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

    public void setEgressApproach(Integer egressApproach) {
        this.egressApproach = egressApproach;
    }

    public Integer getEgressApproach() {
        return this.egressApproach;
    }

    public void setIngressApproach(Integer ingressApproach) {
        this.ingressApproach = ingressApproach;
    }

    public Integer getIngressApproach() {
        return this.ingressApproach;
    }

    public void setIngressPath(Boolean ingressPath) {
        this.ingressPath = ingressPath;
    }

    public Boolean getIngressPath() {
        return this.ingressPath;
    }

    public void setEgressPath(Boolean egressPath) {
        this.egressPath = egressPath;
    }

    public Boolean getEgressPath() {
        return this.egressPath;
    }

    public void setConnectedLanes(Integer[] connectedLanes) {
        this.connectedLanes = connectedLanes;
    }

    public Integer[] getConnectedLanes() {
        return this.connectedLanes;
    }
}
