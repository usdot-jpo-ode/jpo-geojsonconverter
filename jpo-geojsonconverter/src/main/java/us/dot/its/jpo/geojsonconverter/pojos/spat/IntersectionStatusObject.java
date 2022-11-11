
package us.dot.its.jpo.geojsonconverter.pojos.spat;

import javax.annotation.Generated;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "manualControlIsEnabled",
    "stopTimeIsActivated",
    "failureFlash",
    "preemptIsActive",
    "signalPriorityIsActive",
    "fixedTimeOperation",
    "trafficDependentOperation",
    "standbyOperation",
    "failureMode",
    "off",
    "recentMAPmessageUpdate",
    "recentChangeInMAPassignedLanesIDsUsed",
    "noValidMAPisAvailableAtThisTime",
    "noValidSPATisAvailableAtThisTime"
})
@Generated("jsonschema2pojo")
public class IntersectionStatusObject {

    @JsonProperty("manualControlIsEnabled")
    private Boolean manualControlIsEnabled;
    @JsonProperty("stopTimeIsActivated")
    private Boolean stopTimeIsActivated;
    @JsonProperty("failureFlash")
    private Boolean failureFlash;
    @JsonProperty("preemptIsActive")
    private Boolean preemptIsActive;
    @JsonProperty("signalPriorityIsActive")
    private Boolean signalPriorityIsActive;
    @JsonProperty("fixedTimeOperation")
    private Boolean fixedTimeOperation;
    @JsonProperty("trafficDependentOperation")
    private Boolean trafficDependentOperation;
    @JsonProperty("standbyOperation")
    private Boolean standbyOperation;
    @JsonProperty("failureMode")
    private Boolean failureMode;
    @JsonProperty("off")
    private Boolean off;
    @JsonProperty("recentMAPmessageUpdate")
    private Boolean recentMAPmessageUpdate;
    @JsonProperty("recentChangeInMAPassignedLanesIDsUsed")
    private Boolean recentChangeInMAPassignedLanesIDsUsed;
    @JsonProperty("noValidMAPisAvailableAtThisTime")
    private Boolean noValidMAPisAvailableAtThisTime;
    @JsonProperty("noValidSPATisAvailableAtThisTime")
    private Boolean noValidSPATisAvailableAtThisTime;

    @JsonProperty("manualControlIsEnabled")
    public Boolean getManualControlIsEnabled() {
        return manualControlIsEnabled;
    }

    @JsonProperty("manualControlIsEnabled")
    public void setManualControlIsEnabled(Boolean manualControlIsEnabled) {
        this.manualControlIsEnabled = manualControlIsEnabled;
    }

    @JsonProperty("stopTimeIsActivated")
    public Boolean getStopTimeIsActivated() {
        return stopTimeIsActivated;
    }

    @JsonProperty("stopTimeIsActivated")
    public void setStopTimeIsActivated(Boolean stopTimeIsActivated) {
        this.stopTimeIsActivated = stopTimeIsActivated;
    }

    @JsonProperty("failureFlash")
    public Boolean getFailureFlash() {
        return failureFlash;
    }

    @JsonProperty("failureFlash")
    public void setFailureFlash(Boolean failureFlash) {
        this.failureFlash = failureFlash;
    }

    @JsonProperty("preemptIsActive")
    public Boolean getPreemptIsActive() {
        return preemptIsActive;
    }

    @JsonProperty("preemptIsActive")
    public void setPreemptIsActive(Boolean preemptIsActive) {
        this.preemptIsActive = preemptIsActive;
    }

    @JsonProperty("signalPriorityIsActive")
    public Boolean getSignalPriorityIsActive() {
        return signalPriorityIsActive;
    }

    @JsonProperty("signalPriorityIsActive")
    public void setSignalPriorityIsActive(Boolean signalPriorityIsActive) {
        this.signalPriorityIsActive = signalPriorityIsActive;
    }

    @JsonProperty("fixedTimeOperation")
    public Boolean getFixedTimeOperation() {
        return fixedTimeOperation;
    }

    @JsonProperty("fixedTimeOperation")
    public void setFixedTimeOperation(Boolean fixedTimeOperation) {
        this.fixedTimeOperation = fixedTimeOperation;
    }

    @JsonProperty("trafficDependentOperation")
    public Boolean getTrafficDependentOperation() {
        return trafficDependentOperation;
    }

    @JsonProperty("trafficDependentOperation")
    public void setTrafficDependentOperation(Boolean trafficDependentOperation) {
        this.trafficDependentOperation = trafficDependentOperation;
    }

    @JsonProperty("standbyOperation")
    public Boolean getStandbyOperation() {
        return standbyOperation;
    }

    @JsonProperty("standbyOperation")
    public void setStandbyOperation(Boolean standbyOperation) {
        this.standbyOperation = standbyOperation;
    }

    @JsonProperty("failureMode")
    public Boolean getFailureMode() {
        return failureMode;
    }

    @JsonProperty("failureMode")
    public void setFailureMode(Boolean failureMode) {
        this.failureMode = failureMode;
    }

    @JsonProperty("off")
    public Boolean getOff() {
        return off;
    }

    @JsonProperty("off")
    public void setOff(Boolean off) {
        this.off = off;
    }

    @JsonProperty("recentMAPmessageUpdate")
    public Boolean getRecentMAPmessageUpdate() {
        return recentMAPmessageUpdate;
    }

    @JsonProperty("recentMAPmessageUpdate")
    public void setRecentMAPmessageUpdate(Boolean recentMAPmessageUpdate) {
        this.recentMAPmessageUpdate = recentMAPmessageUpdate;
    }

    @JsonProperty("recentChangeInMAPassignedLanesIDsUsed")
    public Boolean getRecentChangeInMAPassignedLanesIDsUsed() {
        return recentChangeInMAPassignedLanesIDsUsed;
    }

    @JsonProperty("recentChangeInMAPassignedLanesIDsUsed")
    public void setRecentChangeInMAPassignedLanesIDsUsed(Boolean recentChangeInMAPassignedLanesIDsUsed) {
        this.recentChangeInMAPassignedLanesIDsUsed = recentChangeInMAPassignedLanesIDsUsed;
    }

    @JsonProperty("noValidMAPisAvailableAtThisTime")
    public Boolean getNoValidMAPisAvailableAtThisTime() {
        return noValidMAPisAvailableAtThisTime;
    }

    @JsonProperty("noValidMAPisAvailableAtThisTime")
    public void setNoValidMAPisAvailableAtThisTime(Boolean noValidMAPisAvailableAtThisTime) {
        this.noValidMAPisAvailableAtThisTime = noValidMAPisAvailableAtThisTime;
    }

    @JsonProperty("noValidSPATisAvailableAtThisTime")
    public Boolean getNoValidSPATisAvailableAtThisTime() {
        return noValidSPATisAvailableAtThisTime;
    }

    @JsonProperty("noValidSPATisAvailableAtThisTime")
    public void setNoValidSPATisAvailableAtThisTime(Boolean noValidSPATisAvailableAtThisTime) {
        this.noValidSPATisAvailableAtThisTime = noValidSPATisAvailableAtThisTime;
    }

}
