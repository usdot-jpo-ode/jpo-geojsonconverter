
package us.dot.its.jpo.geojsonconverter.pojos.spat;

import com.fasterxml.jackson.annotation.JsonProperty;
import us.dot.its.jpo.ode.plugin.j2735.J2735IntersectionStatusObject;

public class IntersectionStatusObject {

    @JsonProperty("manualControlIsEnabled")
    private Boolean manualControlIsEnabled = false;
    @JsonProperty("stopTimeIsActivated")
    private Boolean stopTimeIsActivated = false;
    @JsonProperty("failureFlash")
    private Boolean failureFlash = false;
    @JsonProperty("preemptIsActive")
    private Boolean preemptIsActive = false;
    @JsonProperty("signalPriorityIsActive")
    private Boolean signalPriorityIsActive = false;
    @JsonProperty("fixedTimeOperation")
    private Boolean fixedTimeOperation = false;
    @JsonProperty("trafficDependentOperation")
    private Boolean trafficDependentOperation = false;
    @JsonProperty("standbyOperation")
    private Boolean standbyOperation = false;
    @JsonProperty("failureMode")
    private Boolean failureMode = false;
    @JsonProperty("off")
    private Boolean off = false;
    @JsonProperty("recentMAPmessageUpdate")
    private Boolean recentMAPmessageUpdate = false;
    @JsonProperty("recentChangeInMAPassignedLanesIDsUsed")
    private Boolean recentChangeInMAPassignedLanesIDsUsed = false;
    @JsonProperty("noValidMAPisAvailableAtThisTime")
    private Boolean noValidMAPisAvailableAtThisTime = false;
    @JsonProperty("noValidSPATisAvailableAtThisTime")
    private Boolean noValidSPATisAvailableAtThisTime = false;

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

    public void setStatus(J2735IntersectionStatusObject status){
        switch(status){
            case MANUALCONTROLISENABLED:
                setManualControlIsEnabled(true);
                break;
            case STOPTIMEISACTIVATED:
                setStopTimeIsActivated(true);
                break;
            case FAILUREFLASH:
                setFailureFlash(true);
                break;
            case PREEMPTISACTIVE:
                setPreemptIsActive(true);
                break;
            case SIGNALPRIORITYISACTIVE:
                setSignalPriorityIsActive(true);
                break;
            case FIXEDTIMEOPERATION:
                setFixedTimeOperation(true);
                break;
            case TRAFFICDEPENDENTOPERATION:
                setTrafficDependentOperation(true);
                break;
            case STANDBYOPERATION:
                setStandbyOperation(true);
                break;
            case FAILUREMODE:
                setFailureMode(true);
                break;
            case OFF:
                setOff(true);
                break;
            case RECENTMAPMESSAGEUPDATE:
                setRecentMAPmessageUpdate(true);
                break;
            case RECENTCHANGEINMAPASSIGNEDLANESIDSUSED:
                setRecentChangeInMAPassignedLanesIDsUsed(true);
                break;
            case NOVALIDMAPISAVAILABLEATTHISTIME:
                setNoValidMAPisAvailableAtThisTime(true);
                break;
            case NOVALIDSPATISAVAILABLEATTHISTIME:
                setNoValidSPATisAvailableAtThisTime(true);
                break;
        }
    }

}
