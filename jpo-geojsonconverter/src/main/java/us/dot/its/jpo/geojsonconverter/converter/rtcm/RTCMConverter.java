package us.dot.its.jpo.geojsonconverter.converter.rtcm;


import com.fasterxml.jackson.databind.JsonNode;
import com.networknt.schema.ValidationMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import us.dot.its.jpo.asn.j2735.r2024.Common.*;
import us.dot.its.jpo.asn.j2735.r2024.RTCMcorrections.RTCM_Revision;
import us.dot.its.jpo.asn.j2735.r2024.RTCMcorrections.RTCMcorrections;
import us.dot.its.jpo.asn.j2735.r2024.RTCMcorrections.RTCMcorrectionsMessageFrame;
import us.dot.its.jpo.geojsonconverter.pojos.ProcessedValidationMessage;
import us.dot.its.jpo.geojsonconverter.pojos.geojson.Point;
import us.dot.its.jpo.geojsonconverter.pojos.geojson.rtcm.DecodedRTCMmessage;
import us.dot.its.jpo.geojsonconverter.pojos.geojson.rtcm.ProcessedRTCM;
import us.dot.its.jpo.geojsonconverter.pojos.geojson.rtcm.RTCMProperties;
import us.dot.its.jpo.geojsonconverter.validator.JsonValidatorResult;

import java.util.*;

import static us.dot.its.jpo.geojsonconverter.converter.FieldConversions.*;


/**
 * Encapsulate methods for converting, decoding, and validating RTCM MessageFrames.
 */
@Component
@Slf4j
public class RTCMConverter {

    private final RTCMDecoder decoder;

    @Autowired
    public RTCMConverter(RTCMDecoder decoder) {
        this.decoder = decoder;
    }

    /**
     * Converts a J2735 RTCMcorrections Message Frame to a ProcessedRTCM
     * @param rtcmFrame The RTCM message frame
     * @return The processed RTCM
     */
    public ProcessedRTCM processRTCM(final RTCMcorrectionsMessageFrame rtcmFrame) {
        var properties = new RTCMProperties();

        if (rtcmFrame == null) {
            log.error("RTCM frame is null");
            return new ProcessedRTCM(null, properties);
        }

        RTCMcorrections rtcm = rtcmFrame.getValue();

        if (rtcm == null) {
            log.error("RTCM corrections is null");
            return new ProcessedRTCM(null, properties);
        }

        properties.setMsgCnt((int)rtcm.getMsgCnt().getValue());

        final String rev = rtcm.getRev().getName();
        properties.setRev(rev);
        if (!RTCM_Revision.RTCMREV3.getName().equals(rev)) {
            // CTI 4501 v01.01, Sec. 4.3.3.5.1: Revision 3 is required
            log.debug("Invalid rev: {}", rev);
            properties.addValidationMessage(
                    "CTI-4501 conformance issue: The RTCMcorrections 'rev' (DE_RTCM_Revision) is not 'rtcmRev3'.");
        }

        // CTI 4501 v01.01, Sec. 4.3.3.5.1: optional timestamp is forbidden
        MinuteOfTheYear timestamp = rtcm.getTimeStamp();
        if (timestamp != null) {

            properties.addValidationMessage(
              "CTI-4501 conformance issue: The RTCMcorrections optional field 'timestamp' (DE_MinuteOfTheYear) is " +
                      "present.  It is forbidden by CTI-4501.");
        }

        // FullPositionVector is mandatory in CTI 4501
        // See CTI 4501 v01.01, Sec. 4.3.3.1.1.11, Table 11
        FullPositionVector anchor = rtcm.getAnchorPoint();
        if (anchor != null) {
            processFullPosition(properties, anchor);
        } else {
            properties.addValidationMessage(
                "CTI-4501 conformance issue: The RTCMcorrections 'anchorPoint' (DF_FullPositionVector) is missing.");
        }

        // CTI 4501 v01.01, Sec. 4.3.3.5.1: optional RTCMheader is forbidden
        RTCMheader header = rtcm.getRtcmHeader();
        if (header != null) {
            properties.addValidationMessage(
                    "CTI-4501 conformance issue: The RTCMcorrections optional field 'rtcmHeader' (DF_RTCMheader) is" +
                            " present.  It is forbidden by CTI-4501.");
        }

        final RTCMmessageList messageList = rtcm.getMsgs();
        if (messageList != null) {
            decodeMessages(properties, messageList);
        } else {
            log.info("RTCM messageList is null");
        }

        // CTI 4501 v01.01, Sec. 4.3.3.5.1: optional regional extension is forbidden
        RTCMcorrections.SequenceOfRegional regionalSeq = rtcm.getRegional();
        if (regionalSeq != null && !regionalSeq.isEmpty()) {
            properties.addValidationMessage(
                    "CTI-4501 conformance issue: The RTCMcorrections has regional extensions present which are " +
                            "forbidden by CTI-4501.");
        }

        if (!properties.getValidationMessages().isEmpty()) {
            properties.setCti4501Conformant(false);
        }

        Point geometry = new Point(properties.getLongitude(), properties.getLatitude());
        return new ProcessedRTCM(geometry, properties);
    }


    /**
     * CTI 4501 v01.01, Sec. 4.3.3.5.1:
     * DF_FullPositionVector shall include utcTime, latitude, longitude, and elevation.
     * It shall not include any other fields.
     * @param properties The ProcessedRTCM
     * @param anchor The anchorPoint
     */
    private void processFullPosition(RTCMProperties properties, FullPositionVector anchor) {

        log.info("FullPositionVector: {}", anchor);

        DDateTime utcTime = anchor.getUtcTime();
        List<String> utcValidations = new ArrayList<>();
        Long timestamp = convertDDateTime(utcValidations, utcTime);
        properties.setUtcTime(timestamp);
        for (String utcValidation : utcValidations) {
            properties.addValidationMessage("CTI-4501 conformance issue: anchorPoint (DF_FullPositionVector) " +
                    "'utcTime' field: " + utcValidation);
        }

        Longitude lon = anchor.getLong_();
        if (lon != null) {
            properties.setLongitude(convertLong(lon.getValue()));
        }
        if (properties.getLongitude() == null){
            properties.addValidationMessage(
                    "CTI-4501 conformance issue: The anchorPoint (DF_FullPositionVector) 'long' field (DE_Longitude) is missing.");
        }

        Latitude lat = anchor.getLat();
        if (lat != null) {
            properties.setLatitude(convertLat(lat.getValue()));
        }
        if (properties.getLatitude() == null){
            properties.addValidationMessage(
                    "CTI-4501 conformance issue: The anchorPoint (DF_FullPositionVector) 'lat' field (DE_Latitude) is missing.");
        }

        Elevation elevation = anchor.getElevation();
        if (elevation != null) {
            properties.setElevation(convertElevation(elevation.getValue()));
        }
        if (properties.getElevation() == null) {
            properties.addValidationMessage(
                    "CTI-4501 conformance issue: The anchorPoint (DF_FullPositionVector) 'elevation' field (DE_Elevation) is missing.");
        }

        // Check for extras that should not be present in full position vector
        if (anchor.getHeading() != null) {
            properties.addValidationMessage(
                    "CTI-4501 conformance issue: The anchorPoint (DF_FullPositionVector) 'heading' field is present " +
                            "but should not included.");
        }

        if (anchor.getSpeed() != null) {
            properties.addValidationMessage(
                    "CTI-4501 conformance issue: The anchorPoint (DF_FullPositionVector) 'speed' field is present " +
                            "but should not included.");
        }

        if (anchor.getPosAccuracy() != null) {
            properties.addValidationMessage(
                    "CTI-4501 conformance issue: The anchorPoint (DF_FullPositionVector) 'posAccuracy' field is " +
                            "present but should not included.");
        }

        if (anchor.getTimeConfidence() != null) {
            properties.addValidationMessage(
                    "CTI-4501 conformance issue: The anchorPoint (DF_FullPositionVector) 'timeConfidence' field is " +
                            "present but should not included.");
        }

        if (anchor.getPosConfidence() != null) {
            properties.addValidationMessage(
                    "CTI-4501 conformance issue: The anchorPoint (DF_FullPositionVector) 'posConfidence' field is " +
                            "present but should not included.");
        }

        if (anchor.getSpeedConfidence() != null) {
            properties.addValidationMessage(
                    "CTI-4501 conformance issue: The anchorPoint (DF_FullPositionVector) 'speedConfidence' field is " +
                            "present but should not included.");
        }

    }

    private void decodeMessages(RTCMProperties properties, RTCMmessageList messageList) {
        List<DecodedRTCMmessage> decodedMessages = new ArrayList<>();
        Set<Integer> types = new LinkedHashSet<>();
        for (RTCMmessage message : messageList) {
            var decodedMessage = new DecodedRTCMmessage();
            decodedMessage.setHex(message.getValue());
            decodedMessages.add(decodedMessage);
            JsonNode node = decoder.decodeRtcm(decodedMessage.getHex());
            decodedMessage.setDecodedMessage(node);

            if (node.has("type")) {
                types.add(node.get("type").asInt());
            }

            if (node.has("station_id")) {
                properties.setStationId(node.get("station_id").asInt());
            }
        }

        properties.setMessageTypes(types);
        if (types.isEmpty()) {
            properties.addValidationMessage("No RTCM message types found.");
        }
        if (properties.getStationId() == null) {
            properties.addValidationMessage("RTCM station ID not found.");
        }

        properties.setMessages(decodedMessages);

        // CTI 4501 v01.01, Sec. 4.3.3.5.1. Rules for grouping message types.
        // See also https://www.use-snip.com/kb/knowledge-base/an-rtcm-message-cheat-sheet/
        //
        // Descriptors and system parameters 1005, 1006, 1013, 1033 should be grouped together.
        //
        // MSM messages should be grouped together.
        // MSM 4 support is mandatory, MSM5,6,7 are allowed.
        // Must support GPS and one other constellation (GLONASS, Galileo, or BeiDou)
        //
        // MSM4 - 1074 (GPS), 1084 (GLONASS), 1094 (Galileo), 1104 (SBAS), 1114 (QZSS), 1124 (BeiDou)
        // MSM5 - 1075, 1085, 1095, 1195, 1115, 1125
        // MSM6 - 1076, 1086, 1096, 1196, 1116, 1126
        // MSM7 - 1077, 1087, 1097, 1197, 1117, 1127
        //
        // Groups should not be mixed.
        //
        LinkedHashSet<String> categories = new LinkedHashSet<>();
        boolean isDescriptors = categorize(DESCRIPTOR_TYPES, types);
        if (isDescriptors) {
            categories.add("Descriptor types: 1005, 1006, 1013, 1033");
        }
        boolean isMsm4 = categorize(MSM4_TYPES, types);
        if (isMsm4) {
            categories.add("MSM4");
        }
        boolean isMsm5 = categorize(MSM5_TYPES, types);
        if (isMsm5) {
            categories.add("MSM5");
        }
        boolean isMsm6 = categorize(MSM6_TYPES, types);
        if (isMsm6) {
            categories.add("MSM6");
        }
        boolean isMsm7 = categorize(MSM7_TYPES, types);
        if (isMsm7) {
            categories.add("MSM7");
        }

        log.debug("Categories: {}", categories);

        if (categories.isEmpty()) {
            log.debug("No CTI 4501 categories found.");
            properties.addValidationMessage(
                    "CTI-4501 conformance issue: None of the message types are in categories mentioned in CTI-4501");
        }
        if (categories.size() > 1) {
            log.debug("Multiple CTI 4501 categories found.");
            properties.addValidationMessage(
                    String.format(
                            "CTI-4501 conformance issue: The message list contains message types from more than" +
                                    " one category: %s", categories));
        }
        if (categories.size() == 1) {
            String category = categories.iterator().next();
            checkMsmTypes(category, types, "MSM4", MSM4_GPS, properties);
            checkMsmTypes(category, types, "MSM5", MSM5_GPS, properties);
            checkMsmTypes(category, types, "MSM6", MSM6_GPS, properties);
            checkMsmTypes(category, types, "MSM7", MSM7_GPS, properties);
        }
    }

    private void checkMsmTypes(String category, Set<Integer> types, String msmVersion, int gpsType, RTCMProperties properties) {
        if (msmVersion.equals(category)) {
            if (!types.contains(gpsType)) {
                properties.addValidationMessage(
                        String.format("CTI-4501 conformance issue: The message list contains %s messages but does " +
                                "not contain an %s GPS message: %s", msmVersion, msmVersion, gpsType));
            } else if (types.size() == 1) {
                properties.addValidationMessage(
                        String.format("CTI-4501 conformance issue: The message list contains an %s GPS message, " +
                                "but no %s messages for other constellations", msmVersion, msmVersion));
            }
        }
    }

    private final static Set<Integer> DESCRIPTOR_TYPES = Set.of(1005, 1006, 1013, 1033);
    private final static int MSM4_GPS = 1074;
    private final static int MSM5_GPS = 1075;
    private final static int MSM6_GPS = 1076;
    private final static int MSM7_GPS = 1077;
    private final static Set<Integer> MSM4_TYPES = Set.of(MSM4_GPS, 1084, 1094, 1104, 1114, 1124);
    private final static Set<Integer> MSM5_TYPES = Set.of(MSM5_GPS, 1085, 1095, 1195, 1115, 1125);
    private final static Set<Integer> MSM6_TYPES = Set.of(MSM6_GPS, 1086, 1096, 1196, 1116, 1126);
    private final static Set<Integer> MSM7_TYPES = Set.of(MSM7_GPS, 1087, 1097, 1197, 1117, 1127);

    private boolean categorize(Set<Integer> category, Set<Integer> types) {
        Set<Integer> categorized = new LinkedHashSet<>(types);
        categorized.retainAll(category);
        return !categorized.isEmpty();
    }


    /**
     * Add JSON schema validation results for J2735 and Metadata validation.
     * @param properties The ProcessedRTCM to add validation messages to
     * @param validatorResult the schema validator result
     */
    public void jsonValidation(RTCMProperties properties, JsonValidatorResult validatorResult) {
        var messages = new ArrayList<ProcessedValidationMessage>();
        for (Exception exception : validatorResult.getExceptions()) {
            var msg = new ProcessedValidationMessage();
            msg.setMessage(exception.getMessage());
            msg.setException(Arrays.toString(exception.getStackTrace()));
            messages.add(msg);
        }
        for (ValidationMessage vm : validatorResult.getValidationMessages()) {
            var msg = new ProcessedValidationMessage();
            msg.setMessage(vm.getMessage());
            msg.setSchemaPath(vm.getSchemaPath());
            msg.setJsonPath(vm.getPath());
            messages.add(msg);
        }
        properties.addValidationMessages(messages);
    }
}
