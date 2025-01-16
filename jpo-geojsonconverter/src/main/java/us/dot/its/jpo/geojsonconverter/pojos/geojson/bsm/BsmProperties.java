package us.dot.its.jpo.geojsonconverter.pojos.geojson.bsm;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import us.dot.its.jpo.geojsonconverter.DateJsonMapper;
import us.dot.its.jpo.geojsonconverter.pojos.ProcessedValidationMessage;
import us.dot.its.jpo.ode.plugin.j2735.J2735AccelerationSet4Way;
import us.dot.its.jpo.ode.plugin.j2735.J2735PositionalAccuracy;
import us.dot.its.jpo.ode.plugin.j2735.J2735BrakeSystemStatus;
import us.dot.its.jpo.ode.plugin.j2735.J2735VehicleSize;
import us.dot.its.jpo.ode.plugin.j2735.J2735TransmissionState;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonPropertyOrder({"schemaVersion", "messageType", "odeReceivedAt", "timeStamp", "originIp", "logName", "validationMessages",
"accelSet", "accuracy", "angle", "brakes", "heading", "id", "msgCnt", "secMark", "size", "speed", "transmission"})
public class BsmProperties {
    private static Logger logger = LoggerFactory.getLogger(BsmProperties.class);

    // Metadata properties
    // Default schemaVersion is -1 for older messages that lack a schemaVersion value
    private int schemaVersion = -1;
    private String messageType = "BSM";
    private String odeReceivedAt;
    private String originIp;
    private String logName;
    private List<ProcessedValidationMessage> validationMessages = null;
    private ZonedDateTime timeStamp;

    // Payload properties
    private J2735AccelerationSet4Way accelSet;
    private J2735PositionalAccuracy accuracy;
    private BigDecimal angle;
    private J2735BrakeSystemStatus brakes;
    private BigDecimal heading;
    private String id;
    private Integer msgCnt;
    private Integer secMark;
    private J2735VehicleSize size;
    private BigDecimal speed;
    private J2735TransmissionState transmission;

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

    public void setLogName(String logName) {
        this.logName = logName;
    }

    public String getLogName() {
        return this.logName;
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

    public void setAccelSet(J2735AccelerationSet4Way accelSet) {
        this.accelSet = accelSet;
    }

    public J2735AccelerationSet4Way getAccelSet() {
        return this.accelSet;
    }

    public void setAccuracy(J2735PositionalAccuracy accuracy) {
        this.accuracy = accuracy;
    }

    public J2735PositionalAccuracy getAccuracy() {
        return this.accuracy;
    }

    public void setAngle(BigDecimal angle) {
        this.angle = angle;
    }

    public BigDecimal getAngle() {
        return this.angle;
    }

    public void setBrakes(J2735BrakeSystemStatus brakes) {
        this.brakes = brakes;
    }

    public J2735BrakeSystemStatus getBrakes() {
        return this.brakes;
    }

    public void setHeading(BigDecimal heading) {
        this.heading = heading;
    }

    public BigDecimal getHeading() {
        return this.heading;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return this.id;
    }

    public void setMsgCnt(Integer msgCnt) {
        this.msgCnt = msgCnt;
    }

    public Integer getMsgCnt() {
        return this.msgCnt;
    }

    public void setSecMark(Integer secMark) {
        this.secMark = secMark;
    }

    public Integer getSecMark() {
        return this.secMark;
    }

    public void setSize(J2735VehicleSize size) {
        this.size = size;
    }

    public J2735VehicleSize getSize() {
        return this.size;
    }

    public void setSpeed(BigDecimal speed) {
        this.speed = speed;
    }

    public BigDecimal getSpeed() {
        return this.speed;
    }

    public void setTransmission(J2735TransmissionState transmission) {
        this.transmission = transmission;
    }

    public J2735TransmissionState getTransmission() {
        return this.transmission;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof BsmProperties)) {
            return false;
        }
        BsmProperties bsmProperties = (BsmProperties) o;
        return Objects.equals(accelSet, bsmProperties.getAccelSet()) && Objects.equals(accuracy, bsmProperties.getAccuracy()) && Objects.equals(angle, bsmProperties.getAngle()) && Objects.equals(brakes, bsmProperties.getBrakes()) && Objects.equals(heading, bsmProperties.getHeading()) && Objects.equals(id, bsmProperties.getId()) && Objects.equals(msgCnt, bsmProperties.getMsgCnt()) && Objects.equals(secMark, bsmProperties.getSecMark()) && Objects.equals(size, bsmProperties.getSize()) && Objects.equals(speed, bsmProperties.getSpeed()) && Objects.equals(transmission, bsmProperties.getTransmission());
    }

    @Override
    public int hashCode() {
        return Objects.hash(accelSet, accuracy, angle, brakes, heading, id, msgCnt, secMark, size, speed, transmission);
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
