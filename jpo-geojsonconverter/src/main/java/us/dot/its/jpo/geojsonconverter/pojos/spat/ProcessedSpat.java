package us.dot.its.jpo.geojsonconverter.pojos.spat;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import us.dot.its.jpo.geojsonconverter.DateJsonMapper;
import us.dot.its.jpo.geojsonconverter.pojos.ProcessedValidationMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


@JsonInclude(JsonInclude.Include.NON_NULL)
public class ProcessedSpat {
    private static Logger logger = LoggerFactory.getLogger(ProcessedSpat.class);

    private final Integer schemaVersion = 1;
    private String messageType = "SPAT";
    private String odeReceivedAt;
    private String originIp;
    private String name;
    private Integer region;
    private Integer intersectionId;
    private boolean cti4501Conformant;
    private List<ProcessedValidationMessage> validationMessages = null;
    private Integer revision;
    private IntersectionStatusObject status;
    private ZonedDateTime utcTimeStamp;
    private List<Integer> enabledLanes = new ArrayList<>();
    private List<MovementState> states = null;

    public Integer getSchemaVersion() {
        return schemaVersion;
    }

    public String getMessageType() {
        return messageType;
    }

    public void setMessageType(String messageType) {
        this.messageType = messageType;
    }

    public String getOdeReceivedAt() {
        return odeReceivedAt;
    }

    public void setOdeReceivedAt(String odeReceivedAt) {
        this.odeReceivedAt = odeReceivedAt;
    }

    public String getOriginIp() {
        return originIp;
    }

    public void setOriginIp(String originIp) {
        this.originIp = originIp;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getRegion() {
        return region;
    }

    public void setRegion(Integer region) {
        this.region = region;
    }

    public Integer getIntersectionId() {
        return intersectionId;
    }

    public void setIntersectionId(Integer intersectionId) {
        this.intersectionId = intersectionId;
    }

    public boolean getCti4501Conformant() {
        return cti4501Conformant;
    }

    public void setCti4501Conformant(boolean cti4501Conformant) {
        this.cti4501Conformant = cti4501Conformant;
    }

    public List<ProcessedValidationMessage> getValidationMessages() {
        return validationMessages;
    }

    public void setValidationMessages(List<ProcessedValidationMessage> validationMessages) {
        this.validationMessages = validationMessages;
    }

    public Integer getRevision() {
        return revision;
    }

    public void setRevision(Integer revision) {
        this.revision = revision;
    }

    public IntersectionStatusObject getStatus() {
        return status;
    }

    public void setStatus(IntersectionStatusObject status) {
        this.status = status;
    }

    public ZonedDateTime getUtcTimeStamp() {
        return utcTimeStamp;
    }

    public void setUtcTimeStamp(ZonedDateTime utcTimeStamp) {
        this.utcTimeStamp = utcTimeStamp;
    }

    public List<Integer> getEnabledLanes() {
        return enabledLanes;
    }

    public void setEnabledLanes(List<Integer> enabledLanes) {
        this.enabledLanes = enabledLanes;
    }

    public List<MovementState> getStates() {
        return states;
    }

    public void setStates(List<MovementState> states) {
        this.states = states;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof ProcessedSpat)) {
            return false;
        }
        ProcessedSpat processedSpat = (ProcessedSpat) o;
        return Objects.equals(messageType, processedSpat.messageType) && Objects.equals(odeReceivedAt, processedSpat.odeReceivedAt) && Objects.equals(originIp, processedSpat.originIp) && Objects.equals(name, processedSpat.name) && region == processedSpat.region && intersectionId == processedSpat.intersectionId && cti4501Conformant == processedSpat.cti4501Conformant && Objects.equals(validationMessages, processedSpat.validationMessages) && revision == processedSpat.revision && Objects.equals(status, processedSpat.status) && Objects.equals(utcTimeStamp, processedSpat.utcTimeStamp) && Objects.equals(enabledLanes, processedSpat.enabledLanes) && Objects.equals(states, processedSpat.states);
    }

    @Override
    public int hashCode() {
        return Objects.hash(messageType, odeReceivedAt, originIp, name, region, intersectionId, cti4501Conformant, validationMessages, revision, status, utcTimeStamp, enabledLanes, states);
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
