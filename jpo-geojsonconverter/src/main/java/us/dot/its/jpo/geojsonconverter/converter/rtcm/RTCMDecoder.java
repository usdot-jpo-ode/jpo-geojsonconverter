package us.dot.its.jpo.geojsonconverter.converter.rtcm;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.SystemUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import us.dot.its.jpo.geojsonconverter.DateJsonMapper;
import us.dot.its.jpo.geojsonconverter.GeoJsonConverterProperties;

import java.io.File;
import java.io.IOException;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.HexFormat;

/**
 * Methods for decoding the RTCM payloads
 */
@Component
@Slf4j
public class RTCMDecoder {

    private final boolean fullDecode;

    public RTCMDecoder(@Value("${rtcm.full.decode}") boolean fullDecode) {
        this.fullDecode = fullDecode;
        File executable = new File(EXECUTABLE);
        this.executableExists = executable.exists();
    }

    private static final HexFormat hexFormat = HexFormat.of();

    private static final String EXECUTABLE = "/usr/bin/gpsdecode";

    private final boolean executableExists;

    public JsonNode decodeRtcm(String hex) {
        log.debug("Decode RTCM hex: {}", hex);
        byte[] bytes = hexFormat.parseHex(hex);

        if (!fullDecode) {
            log.debug("Full decode is disabled by config setting");
            return partialDecode(bytes);
        }

        if (executableExists) {
            return fullDecode(bytes);
        } else {
            log.warn("Executable {} does not exist, or running on Windows. Partially decoding. " +
                    "Install gpsdecode, or configure rtcm.full.decode=false to suppress this warning", EXECUTABLE);
            return partialDecode(bytes);
        }

    }

    /**
     * Partially decode the RTCM message.  Used when gpsdecode library is not available.
     * Ref. <a href="https://gitlab.com/gpsd/gpsd/-/blob/master/drivers/driver_rtcm3.c">gpsd/driver_rtcm.c</a>
     * @param bytes byte array
     * @return JSON formatted partially decoded message.
     */
    public static JsonNode partialDecode(byte[] bytes) {
        ObjectMapper mapper = DateJsonMapper.getInstance();
        ObjectNode node = mapper.createObjectNode();

        // Need first 6 bytes to get preamble, length, type, station id.
        if (bytes.length < 6) {
            log.error("Not enough bytes to decode RTCM.");
            return node;
        }

        // Preamble: 8 bits
        int preamble = unsigned(bytes[0]);
        if (preamble != 0xD3) {
            log.error(String.format("Invalid RTCM preamble: %02X, should be %20X", preamble, 0xD3));
            return node;
        }
        node.put("class", "RTCM3");

        // Next 6 bits should be zero
        int zeroBits = unsigned(bytes[1]) >>> 2;
        if (zeroBits != 0) {
            log.error(String.format("Invalid zero bits: %X", zeroBits));
            return node;
        }

        // Length: 10 bits
        int length = ((unsigned(bytes[1]) & 0x03) << 8) | unsigned(bytes[2]);
        node.put("length", length);

        //  Type: 12 bits
        int type = (unsigned(bytes[3]) << 4) | (unsigned(bytes[4]) >>> 4);
        node.put("type", type);

        // Station ID: 12 bits
        // Get station ID for types known or guessed to have them per gpsd/driver_rtcm3.c
        if (type <= 1013 || type == 1029 || type == 1033 || (type >= 1071 && type <= 1230)) {
            int stationId = ((unsigned(bytes[4]) & 0x0F) << 8) | unsigned(bytes[5]);
            node.put("station_id", stationId);
        }

        return node;
    }

    /**
     * Call the native gpsdecode command line tool to fully decode RTCMs.
     * <p>Full decode requires gpsd-client to be installed on Linux.  Will not work on Windows.</p>
     */
    public static JsonNode fullDecode(byte[] bytes) {
        var pb = new ProcessBuilder(EXECUTABLE);

        String json = null;
        Process process = null;
        try {
            process = pb.start();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        try (var is = process.getInputStream()) {
            try (var out = process.getOutputStream()) {
                out.write(bytes);
                out.flush();
            }
            byte[] outBytes = is.readAllBytes();
            json = new String(outBytes, StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        try {
            int exitCode = process.waitFor();
            log.debug("RTCM process exited with code {}", exitCode);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        log.debug("decode RTCM json: {}", json);
        ObjectMapper mapper = DateJsonMapper.getInstance();
        try {
            return mapper.readValue(json, JsonNode.class);
        } catch (JsonProcessingException e) {
            log.error("Decode RTCM json failed", e);
            var errNode = mapper.createObjectNode();
            errNode.put("error", e.getMessage());
            return errNode;
        }
    }



    private static int unsigned(byte b) {
        return b & 0xFF;
    }

}
