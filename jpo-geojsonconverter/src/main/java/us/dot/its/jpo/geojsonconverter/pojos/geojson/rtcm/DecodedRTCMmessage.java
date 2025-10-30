package us.dot.its.jpo.geojsonconverter.pojos.geojson.rtcm;


import com.fasterxml.jackson.databind.JsonNode;
import lombok.Data;

/**
 * A decoded RTCM message.  This class corresponds to the JSON format
 * produced by the
 * <a href="https://gpsd.gitlab.io/gpsd/gpsdecode.html">gpsdecode</a> tool.
 */
@Data
public class DecodedRTCMmessage {

    /**
     * The original raw RTCM message, hex encoded.
     */
    private String hex;

    /**
     * The JSON description produced by decoding the message with the
     * <a href="https://gpsd.gitlab.io/gpsd/gpsdecode.html">gpsdecode</a> tool.
     * Contents vary depending on message type.
     */
    private JsonNode decodedMessage;

}
