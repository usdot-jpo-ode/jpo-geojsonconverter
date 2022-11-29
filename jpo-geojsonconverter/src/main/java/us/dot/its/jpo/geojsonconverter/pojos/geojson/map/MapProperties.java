package us.dot.its.jpo.geojsonconverter.pojos.geojson.map;

import java.util.List;

import us.dot.its.jpo.ode.plugin.j2735.OdePosition3D;
import us.dot.its.jpo.geojsonconverter.pojos.ProcessedValidationMessage;
import us.dot.its.jpo.ode.plugin.j2735.J2735RegulatorySpeedLimit;
import us.dot.its.jpo.ode.plugin.j2735.J2735BitString;

public class MapProperties {
    private List<MapNode> nodes;
    private String messageType;
    private String odeReceivedAt;
    private String originIp;
    private String intersectionName;
    private Integer region;
    private Integer intersectionId;
    private Integer msgIssueRevision;
    private Integer revision;
    private OdePosition3D refPoint;
    private Boolean cti4501Conformant;
    private List<ProcessedValidationMessage> validationMessages;
    private Integer laneWidth;
    private List<J2735RegulatorySpeedLimit> speedLimits;
    private String mapSource;
    private String timeStamp;
    private Integer laneId;
    private String laneName;
    private J2735BitString sharedWith;  // enum is of type J2735LaneSharing
    private Integer egressApproach;
    private Integer ingressApproach;
    private Boolean ingressPath;
    private Boolean egressPath;
    private J2735BitString maneuvers;  // enum is of type J2735AllowedManeuvers
    private List<MapConnection> connectsTo;

    public void setNodes(List<MapNode> nodes) {
        this.nodes = nodes;
    }

    public List<MapNode> getNodes() {
        return this.nodes;
    }

    public void setMessageType(String messageType) {
        this.messageType = messageType;
    }

    public String getMessageType() {
        return this.messageType;
    }

    public void setOdeReceivedAt(String odeReceivedAt) {
        this.odeReceivedAt = odeReceivedAt;
    }

    public String getOdeReceivedAt() {
        return this.odeReceivedAt;
    }

    public void setOriginIp(String originIp) {
        this.originIp = originIp;
    }

    public String getOriginIp() {
        return this.originIp;
    }

    public void setIntersectionName(String intersectionName) {
        this.intersectionName = intersectionName;
    }

    public String getIntersectionName() {
        return this.intersectionName;
    }

    public void setRegion(Integer region) {
        this.region = region;
    }

    public Integer getRegion() {
        return this.region;
    }

    public void setIntersectionId(Integer intersectionId) {
        this.intersectionId = intersectionId;
    }

    public Integer getIntersectionId() {
        return this.intersectionId;
    }

    public void setMsgIssueRevision(Integer msgIssueRevision) {
        this.msgIssueRevision = msgIssueRevision;
    }

    public Integer getMsgIssueRevision() {
        return this.msgIssueRevision;
    }

    public void setRevision(Integer revision) {
        this.revision = revision;
    }

    public Integer getRevision() {
        return this.revision;
    }

    public void setRefPoint(OdePosition3D refPoint) {
        this.refPoint = refPoint;
    }

    public OdePosition3D getRefPoint() {
        return this.refPoint;
    }

    public void setCti4501Conformant(Boolean cti4501Conformant) {
        this.cti4501Conformant = cti4501Conformant;
    }

    public Boolean getCti4501Conformant() {
        return this.cti4501Conformant;
    }

    public void setValidationMessages(List<ProcessedValidationMessage> validationMessages) {
        this.validationMessages = validationMessages;
    }

    public List<ProcessedValidationMessage> getValidationMessages() {
        return this.validationMessages;
    }

    public void setLaneWidth(Integer laneWidth) {
        this.laneWidth = laneWidth;
    }

    public Integer getLaneWidth() {
        return this.laneWidth;
    }

    public void setSpeedLimits(List<J2735RegulatorySpeedLimit> speedLimits) {
        this.speedLimits = speedLimits;
    }

    public List<J2735RegulatorySpeedLimit> getSpeedLimits() {
        return this.speedLimits;
    }

    public void setMapSource(String mapSource) {
        this.mapSource = mapSource;
    }

    public String getMapSource() {
        return this.mapSource;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }

    public String getTimeStamp() {
        return this.timeStamp;
    }

    public void setLaneId(Integer laneId) {
        this.laneId = laneId;
    }

    public Integer getLaneId() {
        return this.laneId;
    }

    public void setLaneName(String laneName) {
        this.laneName = laneName;
    }

    public String getLaneName() {
        return this.laneName;
    }

    public void setSharedWith(J2735BitString sharedWith) {
        this.sharedWith = sharedWith;
    }

    public J2735BitString getSharedWith() {
        return this.sharedWith;
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

    public void setManeuvers(J2735BitString maneuvers) {
        this.maneuvers = maneuvers;
    }

    public J2735BitString getManeuvers() {
        return this.maneuvers;
    }

    public void setConnectsTo(List<MapConnection> connectsTo) {
        this.connectsTo = connectsTo;
    }

    public List<MapConnection> getConnectsTo() {
        return this.connectsTo;
    }
}
