package us.dot.its.jpo.geojsonconverter.pojos.geojson.map;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Objects;

import us.dot.its.jpo.ode.plugin.j2735.OdePosition3D;
import us.dot.its.jpo.geojsonconverter.DateJsonMapper;
import us.dot.its.jpo.geojsonconverter.pojos.ProcessedValidationMessage;
import us.dot.its.jpo.ode.plugin.j2735.J2735RegulatorySpeedLimit;
import us.dot.its.jpo.ode.model.OdeMapMetadata.MapSource;
import us.dot.its.jpo.ode.plugin.j2735.J2735BitString;
import us.dot.its.jpo.ode.plugin.j2735.J2735Connection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class MapProperties {
    private static Logger logger = LoggerFactory.getLogger(MapProperties.class);

    private List<MapNode> nodes;
    private String messageType = "MAP";
    private ZonedDateTime odeReceivedAt;
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
    private MapSource mapSource;
    private ZonedDateTime timeStamp;
    private Integer laneId;
    private String laneName;
    private J2735BitString sharedWith;  // enum is of type J2735LaneSharing
    private Integer egressApproach;
    private Integer ingressApproach;
    private Boolean ingressPath;
    private Boolean egressPath;
    private J2735BitString maneuvers;  // enum is of type J2735AllowedManeuvers
    private List<J2735Connection> connectsTo;

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

    public void setOdeReceivedAt(ZonedDateTime odeReceivedAt) {
        this.odeReceivedAt = odeReceivedAt;
    }

    public ZonedDateTime getOdeReceivedAt() {
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

    public void setMapSource(MapSource mapSource) {
        this.mapSource = mapSource;
    }

    public MapSource getMapSource() {
        return this.mapSource;
    }

    public void setTimeStamp(ZonedDateTime timeStamp) {
        this.timeStamp = timeStamp;
    }

    public ZonedDateTime getTimeStamp() {
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

    public void setConnectsTo(List<J2735Connection> connectsTo) {
        this.connectsTo = connectsTo;
    }

    public List<J2735Connection> getConnectsTo() {
        return this.connectsTo;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof MapProperties)) {
            return false;
        }
        MapProperties mapProperties = (MapProperties) o;
        return Objects.equals(nodes, mapProperties.nodes) && Objects.equals(messageType, mapProperties.messageType) && Objects.equals(odeReceivedAt, mapProperties.odeReceivedAt) && Objects.equals(originIp, mapProperties.originIp) && Objects.equals(intersectionName, mapProperties.intersectionName) && Objects.equals(region, mapProperties.region) && Objects.equals(intersectionId, mapProperties.intersectionId) && Objects.equals(msgIssueRevision, mapProperties.msgIssueRevision) && Objects.equals(revision, mapProperties.revision) && Objects.equals(refPoint, mapProperties.refPoint) && Objects.equals(cti4501Conformant, mapProperties.cti4501Conformant) && Objects.equals(validationMessages, mapProperties.validationMessages) && Objects.equals(laneWidth, mapProperties.laneWidth) && Objects.equals(speedLimits, mapProperties.speedLimits) && Objects.equals(mapSource, mapProperties.mapSource) && Objects.equals(timeStamp, mapProperties.timeStamp) && Objects.equals(laneId, mapProperties.laneId) && Objects.equals(laneName, mapProperties.laneName) && Objects.equals(sharedWith, mapProperties.sharedWith) && Objects.equals(egressApproach, mapProperties.egressApproach) && Objects.equals(ingressApproach, mapProperties.ingressApproach) && Objects.equals(ingressPath, mapProperties.ingressPath) && Objects.equals(egressPath, mapProperties.egressPath) && Objects.equals(maneuvers, mapProperties.maneuvers) && Objects.equals(connectsTo, mapProperties.connectsTo);
    }

    @Override
    public int hashCode() {
        return Objects.hash(nodes, messageType, odeReceivedAt, originIp, intersectionName, region, intersectionId, msgIssueRevision, revision, refPoint, cti4501Conformant, validationMessages, laneWidth, speedLimits, mapSource, timeStamp, laneId, laneName, sharedWith, egressApproach, ingressApproach, ingressPath, egressPath, maneuvers, connectsTo);
    }

    @Override
    public String toString() {
        ObjectMapper mapper = DateJsonMapper.getInstance();
        String testReturn = "";
        try {
            testReturn = (mapper.writeValueAsString(this));
        } catch (JsonProcessingException e) {
            logger.error(e.getMessage(), e);
        }
        return testReturn;
    }
}
