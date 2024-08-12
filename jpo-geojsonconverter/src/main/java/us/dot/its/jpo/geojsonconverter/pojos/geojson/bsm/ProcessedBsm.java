package us.dot.its.jpo.geojsonconverter.pojos.geojson.bsm;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Objects;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import us.dot.its.jpo.geojsonconverter.DateJsonMapper;
import us.dot.its.jpo.geojsonconverter.pojos.ProcessedValidationMessage;
import us.dot.its.jpo.geojsonconverter.pojos.geojson.BaseFeatureCollection;

@JsonPropertyOrder({"schemaVersion", "features", "messageType", "odeReceivedAt", "timeStamp", "originIp", "validationMessages"})
public class ProcessedBsm<Point> extends BaseFeatureCollection<BsmFeature<Point>> {
    private static Logger logger = LoggerFactory.getLogger(ProcessedBsm.class);

    // Default schemaVersion is -1 for older messages that lack a schemaVersion value
    private int schemaVersion = -1;
    private String messageType = "BSM";
    private String odeReceivedAt;
    private String originIp;
    private List<ProcessedValidationMessage> validationMessages = null;
    private ZonedDateTime timeStamp;

    @JsonCreator
    public ProcessedBsm(@JsonProperty("features") BsmFeature<Point>[] features) {
        super(features);
    }

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

    public String getOdeReceivedAt() {
        return odeReceivedAt;
    }

    public void setOdeReceivedAt(String odeReceivedAt) {
        this.odeReceivedAt = odeReceivedAt;
    }

    public void setOriginIp(String originIp) {
        this.originIp = originIp;
    }

    public String getOriginIp() {
        return this.originIp;
    }

    public List<ProcessedValidationMessage> getValidationMessages() {
        return validationMessages;
    }

    public void setValidationMessages(List<ProcessedValidationMessage> validationMessages) {
        this.validationMessages = validationMessages;
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
        if (!(o instanceof ProcessedBsm)) {
            return false;
        }

        @SuppressWarnings("unchecked")
        ProcessedBsm<Point> processedBsm = (ProcessedBsm<Point>) o;
        return (
            Objects.equals(getFeatures(), processedBsm.getFeatures()) && 
            Objects.equals(getSchemaVersion(), processedBsm.getSchemaVersion()) && 
            Objects.equals(getMessageType(), processedBsm.getMessageType()) && 
            Objects.equals(getOdeReceivedAt(), processedBsm.getOdeReceivedAt()) && 
            Objects.equals(getOriginIp(), processedBsm.getOriginIp()) && 
            Objects.equals(getValidationMessages(), processedBsm.getValidationMessages()) && 
            Objects.equals(getTimeStamp(), processedBsm.getTimeStamp()));
    }

    @Override
    public int hashCode() {
        return Objects.hash(messageType, odeReceivedAt, originIp, validationMessages, timeStamp, getFeatures());
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
