package us.dot.its.jpo.geojsonconverter.validator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import us.dot.its.jpo.asn.j2735.r2024.Common.LaneDataAttribute;
import us.dot.its.jpo.asn.j2735.r2024.Common.NodeXY;
import us.dot.its.jpo.asn.j2735.r2024.Common.RegulatorySpeedLimit;
import us.dot.its.jpo.asn.j2735.r2024.MapData.Connection;
import us.dot.its.jpo.asn.j2735.r2024.MapData.GenericLane;
import us.dot.its.jpo.asn.j2735.r2024.MapData.IntersectionGeometry;
import us.dot.its.jpo.asn.j2735.r2024.MapData.MapData;
import us.dot.its.jpo.asn.j2735.r2024.SPAT.IntersectionState;
import us.dot.its.jpo.asn.j2735.r2024.SPAT.MovementEvent;
import us.dot.its.jpo.asn.j2735.r2024.SPAT.MovementState;
import us.dot.its.jpo.asn.j2735.r2024.SPAT.SPAT;
import us.dot.its.jpo.geojsonconverter.pojos.ProcessedValidationMessage;

public class CTI4501Validator {
    /**
     * Checks if the provided SPAT (Signal Phase and Timing) object conforms to the CTI-4501 specification. This method
     * validates the presence of CTI-4501 required fields in the SPAT object and assumes only 1 intersection is defined.
     * <p>
     * Checks for the following fields:
     * <p>
     * SPAT.timeStamp
     * <p>
     * SPAT.intersections[0].id.region
     * <p>
     * SPAT.intersections[0].timeStamp
     * <p>
     * SPAT.intersections[0].states[*].state_time_speed[*].timing
     * <p>
     * SPAT.intersections[0].states[*].state_time_speed[*].timing.startTime
     * <p>
     * SPAT.intersections[0].states[*].state_time_speed[*].timing.maxEndTime
     * <p>
     * SPAT.intersections[0].states[*].state_time_speed[*].timing.nextTime
     *
     * @param spat The SPAT object to be validated for CTI-4501 conformance.
     * @return a list of validation messages describing CTI-4501 conformance issues, or an empty list if conformant.
     */
    public static List<ProcessedValidationMessage> spatValidation(SPAT spat) {
        HashMap<String, ProcessedValidationMessage> validationMap = new HashMap<>();

        // Check SPAT timestamp
        if (spat.getTimeStamp() == null) {
            validationMap.put("spat.timeStamp",
                    createValidationMessage("The SPAT 'timeStamp' DE_MinuteOfTheYear is missing"));
        }

        // Get the first intersection from the SPAT object
        IntersectionState intersection = spat.getIntersections().get(0);

        // Check intersection fields
        if (intersection.getId().getRegion() == null) {
            validationMap.put("intersection.id.region",
                    createValidationMessage("The intersections 'id.region' DE_RoadRegulatorID is missing"));
        }
        if (intersection.getTimeStamp() == null) {
            validationMap.put("intersection.timeStamp",
                    createValidationMessage("The intersections 'timeStamp' DE_Dsecond is missing"));
        }

        // Iterate through each movement state and check for CTI-4501 required fields
        for (MovementState mState : intersection.getStates()) {
            for (MovementEvent mEvent : mState.getState_time_speed()) {
                if (mEvent.getTiming() != null) {
                    if (mEvent.getTiming().getStartTime() == null && !validationMap.containsKey("timing.startTime")) {
                        validationMap.put("timing.startTime", createValidationMessage(
                                "The state-time-speed 'timing.startTime' DE_TimeMark is missing"));
                    }
                    if (mEvent.getTiming().getMaxEndTime() == null && !validationMap.containsKey("timing.maxEndTime")) {
                        validationMap.put("timing.maxEndTime", createValidationMessage(
                                "The state-time-speed 'timing.maxEndTime' DE_TimeMark is missing"));
                    }
                    if (mEvent.getTiming().getNextTime() == null && !validationMap.containsKey("timing.nextTime")) {
                        validationMap.put("timing.nextTime", createValidationMessage(
                                "The state-time-speed 'timing.nextTime' DE_TimeMark is missing"));
                    }
                } else if (!validationMap.containsKey("timing")) {
                    validationMap.put("timing",
                            createValidationMessage("The state-time-speed 'timing' DF_TimeChangeDetails is missing"));
                }
            }
        }

        // Convert HashMap values to List
        List<ProcessedValidationMessage> validationMessages = new ArrayList<>(validationMap.values());
        return validationMessages;
    }

    /**
     * Checks if the provided MapData object conforms to the CTI-4501 specification. This method validates the presence
     * of CTI-4501 required fields in the MapData object and assumes only 1 intersection is defined.
     * <p>
     * Checks for all strictly and conditionally mandatory fields defined in CTI-4501 (page 132)
     * <p>
     * CTI-4501 Document: https://www.ite.org/ITEORG/assets/File/Standards/CTI%204501v0101.pdf
     *
     * @param mapData The MapData object to be validated for CTI-4501 conformance.
     * @return a list of validation messages describing CTI-4501 conformance issues, or an empty list if conformant.
     */
    public static List<ProcessedValidationMessage> mapValidation(MapData mapData) {
        HashMap<String, ProcessedValidationMessage> validationMap = new HashMap<>();

        // Get the first intersection from the Map object
        IntersectionGeometry intersection = mapData.getIntersections().get(0);

        // Check intersection fields
        if (intersection.getId().getRegion() == null) {
            validationMap.put("intersection.id.region",
                    createValidationMessage("The intersections 'id.region' DE_RoadRegulatorID is missing"));
        }
        if (intersection.getRefPoint().getElevation() == null) {
            validationMap.put("intersection.refPoint.elevation",
                    createValidationMessage("The intersections 'refPoint.elevation' DE_Elevation is missing"));
        }
        if (intersection.getLaneWidth() == null) {
            validationMap.put("intersection.laneWidth",
                    createValidationMessage("The intersections 'laneWidth' DE_LaneWidth is missing"));
        }

        // Check speedLimits
        if (intersection.getSpeedLimits() != null) {
            for (RegulatorySpeedLimit speedLimit : intersection.getSpeedLimits()) {
                if (speedLimit.getType() == null && !validationMap.containsKey("speedLimits.type")) {
                    validationMap.put("speedLimits.type",
                            createValidationMessage("The speedLimits 'type' DE_SpeedLimitType is missing"));
                }
                if (speedLimit.getSpeed() == null && !validationMap.containsKey("speedLimits.speed")) {
                    validationMap.put("speedLimits.speed",
                            createValidationMessage("The speedLimits 'speed' DE_Velocity is missing"));
                }
            }
        } else {
            validationMap.put("intersection.speedLimits",
                    createValidationMessage("The intersections 'speedLimits' DF_SpeedLimitList is missing"));
        }

        // Check GenericLane fields
        for (GenericLane lane : intersection.getLaneSet()) {
            if (lane.getManeuvers() == null && !validationMap.containsKey("laneSet.maneuvers")) {
                validationMap.put("laneSet.maneuvers",
                        createValidationMessage("The laneSet 'maneuvers' DE_AllowedManeuvers is missing"));
            }

            // Check for conditional nodes field requirements
            if (lane.getNodeList().getNodes() != null) {
                for (NodeXY nodeXY : lane.getNodeList().getNodes()) {
                    if (nodeXY.getDelta().getNode_XY1() != null) {
                        if (nodeXY.getDelta().getNode_XY1().getX() == null
                                && !validationMap.containsKey("nodeXY.delta.node-XY1.x")) {
                            validationMap.put("nodeXY.delta.node-XY1.x", createValidationMessage(
                                    "The nodeXY 'delta.node-XY1.x' DE_Offset_B10 is missing but 'delta.node-XY1' DF_Node_XY_20b is present"));
                        }
                        if (nodeXY.getDelta().getNode_XY1().getY() == null
                                && !validationMap.containsKey("nodeXY.delta.node-XY1.y")) {
                            validationMap.put("nodeXY.delta.node-XY1.y", createValidationMessage(
                                    "The nodeXY 'delta.node-XY1.y' DE_Offset_B10 is missing but 'delta.node-XY1' DF_Node_XY_20b is present"));
                        }
                    } else if (nodeXY.getDelta().getNode_XY2() != null) {
                        if (nodeXY.getDelta().getNode_XY2().getX() == null
                                && !validationMap.containsKey("nodeXY.delta.node-XY2.x")) {
                            validationMap.put("nodeXY.delta.node-XY2.x", createValidationMessage(
                                    "The nodeXY 'delta.node-XY2.x' DE_Offset_B11 is missing but 'delta.node-XY2' DF_Node_XY_22b is present"));
                        }
                        if (nodeXY.getDelta().getNode_XY2().getY() == null
                                && !validationMap.containsKey("nodeXY.delta.node-XY2.y")) {
                            validationMap.put("nodeXY.delta.node-XY2.y", createValidationMessage(
                                    "The nodeXY 'delta.node-XY2.y' DE_Offset_B11 is missing but 'delta.node-XY2' DF_Node_XY_22b is present"));
                        }
                    } else if (nodeXY.getDelta().getNode_XY3() != null) {
                        if (nodeXY.getDelta().getNode_XY3().getX() == null
                                && !validationMap.containsKey("nodeXY.delta.node-XY3.x")) {
                            validationMap.put("nodeXY.delta.node-XY3.x", createValidationMessage(
                                    "The nodeXY 'delta.node-XY3.x' DE_Offset_B12 is missing but 'delta.node-XY3' DF_Node_XY_24b is present"));
                        }
                        if (nodeXY.getDelta().getNode_XY3().getY() == null
                                && !validationMap.containsKey("nodeXY.delta.node-XY3.y")) {
                            validationMap.put("nodeXY.delta.node-XY3.y", createValidationMessage(
                                    "The nodeXY 'delta.node-XY3.y' DE_Offset_B12 is missing but 'delta.node-XY3' DF_Node_XY_24b is present"));
                        }
                    } else if (nodeXY.getDelta().getNode_XY4() != null) {
                        if (nodeXY.getDelta().getNode_XY4().getX() == null
                                && !validationMap.containsKey("nodeXY.delta.node-XY4.x")) {
                            validationMap.put("nodeXY.delta.node-XY4.x", createValidationMessage(
                                    "The nodeXY 'delta.node-XY4.x' DE_Offset_B13 is missing but 'delta.node-XY4' DF_Node_XY_26b is present"));
                        }
                        if (nodeXY.getDelta().getNode_XY4().getY() == null
                                && !validationMap.containsKey("nodeXY.delta.node-XY4.y")) {
                            validationMap.put("nodeXY.delta.node-XY4.y", createValidationMessage(
                                    "The nodeXY 'delta.node-XY4.y' DE_Offset_B13 is missing but 'delta.node-XY4' DF_Node_XY_26b is present"));
                        }
                    } else if (nodeXY.getDelta().getNode_XY5() != null) {
                        if (nodeXY.getDelta().getNode_XY5().getX() == null
                                && !validationMap.containsKey("nodeXY.delta.node-XY5.x")) {
                            validationMap.put("nodeXY.delta.node-XY5.x", createValidationMessage(
                                    "The nodeXY 'delta.node-XY5.x' DE_Offset_B14 is missing but 'delta.node-XY5' DF_Node_XY_28b is present"));
                        }
                        if (nodeXY.getDelta().getNode_XY5().getY() == null
                                && !validationMap.containsKey("nodeXY.delta.node-XY5.y")) {
                            validationMap.put("nodeXY.delta.node-XY5.y", createValidationMessage(
                                    "The nodeXY 'delta.node-XY5.y' DE_Offset_B14 is missing but 'delta.node-XY5' DF_Node_XY_28b is present"));
                        }
                    } else if (nodeXY.getDelta().getNode_XY6() != null) {
                        if (nodeXY.getDelta().getNode_XY6().getX() == null
                                && !validationMap.containsKey("nodeXY.delta.node-XY6.x")) {
                            validationMap.put("nodeXY.delta.node-XY6.x", createValidationMessage(
                                    "The nodeXY 'delta.node-XY6.x' DE_Offset_B16 is missing but 'delta.node-XY6' DF_Node_XY_32b is present"));
                        }
                        if (nodeXY.getDelta().getNode_XY6().getY() == null
                                && !validationMap.containsKey("nodeXY.delta.node-XY6.y")) {
                            validationMap.put("nodeXY.delta.node-XY6.y", createValidationMessage(
                                    "The nodeXY 'delta.node-XY6.y' DE_Offset_B16 is missing but 'delta.node-XY6' DF_Node_XY_32b is present"));
                        }
                    }

                    // Check for conditional node attributes
                    if (nodeXY.getAttributes() != null) {
                        if (nodeXY.getAttributes().getData() != null) {
                            for (LaneDataAttribute data : nodeXY.getAttributes().getData()) {
                                if (data.getSpeedLimits() != null) {
                                    for (RegulatorySpeedLimit speedLimit : data.getSpeedLimits()) {
                                        if (speedLimit.getType() == null
                                                && !validationMap.containsKey("attributes.data.speedLimits.type")) {
                                            validationMap.put("attributes.data.speedLimits.type",
                                                    createValidationMessage(
                                                            "The attributes 'data.speedLimits.type' DE_SpeedLimitType is missing"));
                                        }
                                        if (speedLimit.getSpeed() == null
                                                && !validationMap.containsKey("attributes.data.speedLimits.speed")) {
                                            validationMap.put("attributes.data.speedLimits.speed",
                                                    createValidationMessage(
                                                            "The attributes 'data.speedLimits.speed' DE_Velocity is missing"));
                                        }
                                    }
                                } else if (!validationMap.containsKey("attributes.data.speedLimits")) {
                                    validationMap.put("attributes.data.speedLimits", createValidationMessage(
                                            "The attributes 'data.speedLimits' DF_SpeedLimitList is missing but 'attributes.data' DF_LaneDataAttributeList is present"));
                                }
                            }
                        }
                    }
                }
            } else if (lane.getNodeList().getComputed() != null) {
                if (lane.getNodeList().getComputed().getReferenceLaneId() == null
                        && !validationMap.containsKey("computed.referenceLaneId")) {
                    validationMap.put("computed.referenceLaneId",
                            createValidationMessage("The computed 'referenceLaneId' DE_LaneID is missing"));
                }
                if (lane.getNodeList().getComputed().getOffsetXaxis() == null
                        && !validationMap.containsKey("computed.offsetXaxis")) {
                    validationMap.put("computed.offsetXaxis", createValidationMessage(
                            "The computed 'offsetXaxis' DE_DrivenLineOffsetSmall or DE_DrivenLineOffsetLarge is missing"));
                }
                if (lane.getNodeList().getComputed().getOffsetYaxis() == null
                        && !validationMap.containsKey("computed.offsetYaxis")) {
                    validationMap.put("computed.offsetYaxis", createValidationMessage(
                            "The computed 'offsetYaxis' DE_DrivenLineOffsetSmall or DE_DrivenLineOffsetLarge is missing"));
                }
            }

            // Check for connectsTo field and its nested fields
            if (lane.getConnectsTo() != null) {
                for (Connection connection : lane.getConnectsTo()) {
                    if (connection.getConnectingLane() != null) {
                        if (connection.getConnectingLane().getLane() == null
                                && !validationMap.containsKey("connectsTo.connectingLane.lane")) {
                            validationMap.put("connectsTo.connectingLane.lane", createValidationMessage(
                                    "The connectsTo 'connectingLane.lane' DE_LaneID is missing"));
                        }
                        if (connection.getConnectingLane().getManeuver() == null
                                && !validationMap.containsKey("connectsTo.connectingLane.maneuver")) {
                            validationMap.put("connectsTo.connectingLane.maneuver", createValidationMessage(
                                    "The connectsTo 'connectingLane.maneuver' DE_AllowedManeuver is missing"));
                        }
                    }
                    if (connection.getSignalGroup() == null && !validationMap.containsKey("connectsTo.signalGroup")) {
                        validationMap.put("connectsTo.signalGroup",
                                createValidationMessage("The connectsTo 'signalGroup' DE_SignalGroupID is missing"));
                    }
                }
            } else if (!validationMap.containsKey("laneSet.connectsTo")) {
                validationMap.put("laneSet.connectsTo",
                        createValidationMessage("The laneSet 'connectsTo' DF_ConnectsToList is missing"));
            }
        }

        // Convert HashMap values to List
        List<ProcessedValidationMessage> validationMessages = new ArrayList<>(validationMap.values());
        return validationMessages;
    }

    // Helper method to create a validation message
    private static ProcessedValidationMessage createValidationMessage(String message) {
        ProcessedValidationMessage validationMessage = new ProcessedValidationMessage();
        validationMessage.setMessage("CTI-4501 conformance issue: " + message);
        return validationMessage;
    }
}
