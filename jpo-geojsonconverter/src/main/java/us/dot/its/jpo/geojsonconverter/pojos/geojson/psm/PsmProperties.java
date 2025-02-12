package us.dot.its.jpo.geojsonconverter.pojos.geojson.psm;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import us.dot.its.jpo.geojsonconverter.DateJsonMapper;
import us.dot.its.jpo.geojsonconverter.pojos.ProcessedValidationMessage;
import us.dot.its.jpo.ode.plugin.j2735.J2735PersonalDeviceUserType;
import java.time.ZonedDateTime;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonPropertyOrder({"schemaVersion", "messageType", "odeReceivedAt", "timeStamp", "originIp", "validationMessages",
        "basicType", "id", "msgCnt", "secMark", "speed", "heading"})
public class PsmProperties {
    private static Logger logger = LoggerFactory.getLogger(PsmProperties.class);

    // Metadata properties
    private int schemaVersion = 1;
    private String messageType = "PSM";
    private String odeReceivedAt;
    private ZonedDateTime timeStamp;
    private String originIp;
    private List<ProcessedValidationMessage> validationMessages = null;

    // Payload properties
    private J2735PersonalDeviceUserType basicType;
    private String id;
    private Integer msgCnt;
    private Integer secMark;
    private Integer speed;
    private Integer heading;

    @Override
    public String toString() {
        ObjectMapper mapper = DateJsonMapper.getInstance();
        String testReturn = "";
        try {
            testReturn = mapper.writeValueAsString(this);
        } catch (JsonProcessingException e) {
            logger.error(e.getMessage(), e);
        }
        return testReturn;
    }
}
