package us.dot.its.jpo.geojsonconverter.pojos.geojson.map;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Objects;

import us.dot.its.jpo.ode.plugin.j2735.OdePosition3D;
import us.dot.its.jpo.geojsonconverter.DateJsonMapper;
import us.dot.its.jpo.geojsonconverter.pojos.ProcessedValidationMessage;
import us.dot.its.jpo.ode.plugin.j2735.J2735RegulatorySpeedLimit;
import us.dot.its.jpo.ode.model.OdeMapMetadata.MapSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class MapSharedProperties {
    private static Logger logger = LoggerFactory.getLogger(MapSharedProperties.class);

    // Default schemaVersion is -1 for older messages that lack a schemaVersion value
    private int schemaVersion = -1;
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

    public int getSchemaVersion() {
        return schemaVersion;
    }

    public void setSchemaVersion(int schemaVersion) {
        this.schemaVersion = schemaVersion;
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

    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof MapSharedProperties)) {
            return false;
        }
        MapSharedProperties mapSharedProperties = (MapSharedProperties) o;
        return Objects.equals(messageType, mapSharedProperties.messageType) && Objects.equals(odeReceivedAt, mapSharedProperties.odeReceivedAt) && Objects.equals(originIp, mapSharedProperties.originIp) && Objects.equals(intersectionName, mapSharedProperties.intersectionName) && Objects.equals(region, mapSharedProperties.region) && Objects.equals(intersectionId, mapSharedProperties.intersectionId) && Objects.equals(msgIssueRevision, mapSharedProperties.msgIssueRevision) && Objects.equals(revision, mapSharedProperties.revision) && Objects.equals(refPoint, mapSharedProperties.refPoint) && Objects.equals(cti4501Conformant, mapSharedProperties.cti4501Conformant) && Objects.equals(validationMessages, mapSharedProperties.validationMessages) && Objects.equals(laneWidth, mapSharedProperties.laneWidth) && Objects.equals(speedLimits, mapSharedProperties.speedLimits) && Objects.equals(mapSource, mapSharedProperties.mapSource) && Objects.equals(timeStamp, mapSharedProperties.timeStamp);
    }

    @Override
    public int hashCode() {
        return Objects.hash(messageType, odeReceivedAt, originIp, intersectionName, region, intersectionId, msgIssueRevision, revision, refPoint, cti4501Conformant, validationMessages, laneWidth, speedLimits, mapSource, timeStamp);
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
