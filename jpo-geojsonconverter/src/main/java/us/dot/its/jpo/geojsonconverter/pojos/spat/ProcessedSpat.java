
package us.dot.its.jpo.geojsonconverter.pojos.spat;

import java.util.List;
import com.fasterxml.jackson.annotation.JsonProperty;

public class ProcessedSpat {

    @JsonProperty("messageType")
    private String messageType = "SPAT";
    @JsonProperty("odeReceivedAt")
    private String odeReceivedAt;
    @JsonProperty("originIp")
    private String originIp;
    @JsonProperty("name")
    private String name;
    @JsonProperty("region")
    private Integer region;
    @JsonProperty("intersectionId")
    private Integer intersectionId;
    @JsonProperty("cti4501Conformant")
    private Boolean cti4501Conformant = true;
    @JsonProperty("validationMessages")
    private List<ProcessedSpatValidationMessage> validationMessages = null;
    @JsonProperty("revision")
    private Integer revision;
    @JsonProperty("status")
    private IntersectionStatusObject status;
    @JsonProperty("utcTimeStamp")
    private String utcTimeStamp;
    @JsonProperty("enabledLanes")
    private List<Integer> enabledLanes = null;
    @JsonProperty("states")
    private List<MovementState> states = null;

    @JsonProperty("messageType")
    public String getMessageType() {
        return messageType;
    }

    @JsonProperty("messageType")
    public void setMessageType(String messageType) {
        this.messageType = messageType;
    }

    @JsonProperty("odeReceivedAt")
    public String getOdeReceivedAt() {
        return odeReceivedAt;
    }

    @JsonProperty("odeReceivedAt")
    public void setOdeReceivedAt(String odeReceivedAt) {
        this.odeReceivedAt = odeReceivedAt;
    }

    @JsonProperty("originIp")
    public String getOriginIp() {
        return originIp;
    }

    @JsonProperty("originIp")
    public void setOriginIp(String originIp) {
        this.originIp = originIp;
    }

    @JsonProperty("name")
    public String getName() {
        return name;
    }

    @JsonProperty("name")
    public void setName(String name) {
        this.name = name;
    }

    @JsonProperty("region")
    public Integer getRegion() {
        return region;
    }

    @JsonProperty("region")
    public void setRegion(Integer region) {
        this.region = region;
    }

    @JsonProperty("intersectionId")
    public Integer getIntersectionId() {
        return intersectionId;
    }

    @JsonProperty("intersectionId")
    public void setIntersectionId(Integer intersectionId) {
        this.intersectionId = intersectionId;
    }

    @JsonProperty("cti4501Conformant")
    public Boolean getCti4501Conformant() {
        return cti4501Conformant;
    }

    @JsonProperty("cti4501Conformant")
    public void setCti4501Conformant(Boolean cti4501Conformant) {
        this.cti4501Conformant = cti4501Conformant;
    }

    @JsonProperty("validationMessages")
    public List<ProcessedSpatValidationMessage> getValidationMessages() {
        return validationMessages;
    }

    @JsonProperty("validationMessages")
    public void setValidationMessages(List<ProcessedSpatValidationMessage> validationMessages) {
        this.validationMessages = validationMessages;
    }

    @JsonProperty("revision")
    public Integer getRevision() {
        return revision;
    }

    @JsonProperty("revision")
    public void setRevision(Integer revision) {
        this.revision = revision;
    }

    @JsonProperty("status")
    public IntersectionStatusObject getStatus() {
        return status;
    }

    @JsonProperty("status")
    public void setStatus(IntersectionStatusObject status) {
        this.status = status;
    }

    @JsonProperty("utcTimeStamp")
    public String getUtcTimeStamp() {
        return utcTimeStamp;
    }

    @JsonProperty("utcTimeStamp")
    public void setUtcTimeStamp(String utcTimeStamp) {
        this.utcTimeStamp = utcTimeStamp;
    }

    @JsonProperty("enabledLanes")
    public List<Integer> getEnabledLanes() {
        return enabledLanes;
    }

    @JsonProperty("enabledLanes")
    public void setEnabledLanes(List<Integer> enabledLanes) {
        this.enabledLanes = enabledLanes;
    }

    @JsonProperty("states")
    public List<MovementState> getStates() {
        return states;
    }

    @JsonProperty("states")
    public void setStates(List<MovementState> states) {
        this.states = states;
    }

}
