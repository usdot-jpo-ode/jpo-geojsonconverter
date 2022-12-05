package us.dot.its.jpo.geojsonconverter.pojos.spat;

import java.time.ZonedDateTime;
import java.util.Objects;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import us.dot.its.jpo.geojsonconverter.DateJsonMapper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TimingChangeDetails {
    private static Logger logger = LoggerFactory.getLogger(TimingChangeDetails.class);

    private ZonedDateTime startTime;
    private ZonedDateTime minEndTime;
    private ZonedDateTime maxEndTime;
    private ZonedDateTime likelyTime;
    private Integer confidence;
    private ZonedDateTime nextTime;

    public ZonedDateTime getStartTime() {
        return this.startTime;
    }

    public void setStartTime(ZonedDateTime startTime) {
        this.startTime = startTime;
    }

    public ZonedDateTime getMinEndTime() {
        return this.minEndTime;
    }

    public void setMinEndTime(ZonedDateTime minEndTime) {
        this.minEndTime = minEndTime;
    }

    public ZonedDateTime getMaxEndTime() {
        return this.maxEndTime;
    }

    public void setMaxEndTime(ZonedDateTime maxEndTime) {
        this.maxEndTime = maxEndTime;
    }

    public ZonedDateTime getLikelyTime() {
        return this.likelyTime;
    }

    public void setLikelyTime(ZonedDateTime likelyTime) {
        this.likelyTime = likelyTime;
    }

    public Integer getConfidence() {
        return this.confidence;
    }

    public void setConfidence(Integer confidence) {
        this.confidence = confidence;
    }

    public ZonedDateTime getNextTime() {
        return this.nextTime;
    }

    public void setNextTime(ZonedDateTime nextTime) {
        this.nextTime = nextTime;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof TimingChangeDetails)) {
            return false;
        }
        TimingChangeDetails timingChangeDetails = (TimingChangeDetails) o;
        return Objects.equals(startTime, timingChangeDetails.startTime) && Objects.equals(minEndTime, timingChangeDetails.minEndTime) && Objects.equals(maxEndTime, timingChangeDetails.maxEndTime) && Objects.equals(likelyTime, timingChangeDetails.likelyTime) && Objects.equals(confidence, timingChangeDetails.confidence) && Objects.equals(nextTime, timingChangeDetails.nextTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(startTime, minEndTime, maxEndTime, likelyTime, confidence, nextTime);
    }

    @Override
    public String toString() {
        ObjectMapper mapper = DateJsonMapper.getInstance();
        mapper.registerModule(new JavaTimeModule());
        String testReturn = "";
        try {
            testReturn = (mapper.writeValueAsString(this));
        } catch (JsonProcessingException e) {
            logger.error(e.getMessage(), e);
        }
        return testReturn;
    }
}
