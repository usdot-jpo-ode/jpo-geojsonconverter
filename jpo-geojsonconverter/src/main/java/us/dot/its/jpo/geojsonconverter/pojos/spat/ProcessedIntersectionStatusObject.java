package us.dot.its.jpo.geojsonconverter.pojos.spat;

import us.dot.its.jpo.geojsonconverter.pojos.ProcessedBitstring;

public class ProcessedIntersectionStatusObject extends ProcessedBitstring {
    public ProcessedIntersectionStatusObject() {
        super("manualControlIsEnabled", "stopTimeIsActivated", "failureFlash", "preemptIsActive",
                "signalPriorityIsActive", "fixedTimeOperation", "trafficDependentOperation", "standbyOperation",
                "failureMode", "off", "recentMAPmessageUpdate", "recentChangeInMAPassignedLanesIDsUsed",
                "noValidMAPisAvailableAtThisTime", "noValidSPATisAvailableAtThisTime");
    }
}
