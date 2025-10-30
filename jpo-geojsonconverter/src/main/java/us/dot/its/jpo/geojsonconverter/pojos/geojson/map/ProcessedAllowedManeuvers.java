package us.dot.its.jpo.geojsonconverter.pojos.geojson.map;

import us.dot.its.jpo.geojsonconverter.pojos.ProcessedBitstring;

public class ProcessedAllowedManeuvers extends ProcessedBitstring {
    public ProcessedAllowedManeuvers() {
        super("maneuverStraightAllowed", "maneuverLeftAllowed", "maneuverRightAllowed", "maneuverUTurnAllowed", "maneuverLeftTurnOnRedAllowed", "maneuverRightTurnOnRedAllowed", "maneuverLaneChangeAllowed", "maneuverNoStoppingAllowed", "yieldAllwaysRequired", "goWithHalt", "caution", "reserved1");
    }
}
