
package us.dot.its.jpo.geojsonconverter.pojos.spat;

import javax.annotation.Generated;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "startTime",
    "minEndTime",
    "maxEndTime",
    "likelyTime",
    "confidence",
    "nextTime"
})
@Generated("jsonschema2pojo")
public class TimingChangeDetails {

    @JsonProperty("startTime")
    private String startTime;
    @JsonProperty("minEndTime")
    private String minEndTime;
    @JsonProperty("maxEndTime")
    private String maxEndTime;
    @JsonProperty("likelyTime")
    private Integer likelyTime;
    @JsonProperty("confidence")
    private Integer confidence;
    @JsonProperty("nextTime")
    private String nextTime;

    @JsonProperty("startTime")
    public String getStartTime() {
        return startTime;
    }

    @JsonProperty("startTime")
    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    @JsonProperty("minEndTime")
    public String getMinEndTime() {
        return minEndTime;
    }

    @JsonProperty("minEndTime")
    public void setMinEndTime(String minEndTime) {
        this.minEndTime = minEndTime;
    }

    @JsonProperty("maxEndTime")
    public String getMaxEndTime() {
        return maxEndTime;
    }

    @JsonProperty("maxEndTime")
    public void setMaxEndTime(String maxEndTime) {
        this.maxEndTime = maxEndTime;
    }

    @JsonProperty("likelyTime")
    public Integer getLikelyTime() {
        return likelyTime;
    }

    @JsonProperty("likelyTime")
    public void setLikelyTime(Integer likelyTime) {
        this.likelyTime = likelyTime;
    }

    @JsonProperty("confidence")
    public Integer getConfidence() {
        return confidence;
    }

    @JsonProperty("confidence")
    public void setConfidence(Integer confidence) {
        this.confidence = confidence;
    }

    @JsonProperty("nextTime")
    public String getNextTime() {
        return nextTime;
    }

    @JsonProperty("nextTime")
    public void setNextTime(String nextTime) {
        this.nextTime = nextTime;
    }

}
