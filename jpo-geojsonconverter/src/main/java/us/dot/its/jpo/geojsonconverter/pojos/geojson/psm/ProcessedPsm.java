package us.dot.its.jpo.geojsonconverter.pojos.geojson.psm;

import java.time.ZonedDateTime;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.Data;
import lombok.EqualsAndHashCode;

import us.dot.its.jpo.geojsonconverter.DateJsonMapper;
import us.dot.its.jpo.geojsonconverter.pojos.ProcessedValidationMessage;
import us.dot.its.jpo.geojsonconverter.pojos.geojson.BaseFeatureCollection;

@Data
@EqualsAndHashCode(callSuper = true)
@JsonPropertyOrder({"schemaVersion", "features", "messageType", "odeReceivedAt", "timeStamp", "originIp", "validationMessages"})
public class ProcessedPsm<Point> extends BaseFeatureCollection<PsmFeature<Point>> {
    private static Logger logger = LoggerFactory.getLogger(ProcessedPsm.class);

    private int schemaVersion = 1;
    private String messageType = "PSM";
    private String odeReceivedAt;
    private String originIp;
    private List<ProcessedValidationMessage> validationMessages = null;
    private ZonedDateTime timeStamp;

    @JsonCreator
    public ProcessedPsm(@JsonProperty("features") PsmFeature<Point>[] features) {
        super(features);
    }

    @Override
    public String toString() {
        ObjectMapper mapper = DateJsonMapper.getInstance();
        String testReturn = "";
        try {
            testReturn = (mapper.writeValueAsString(this));
        } catch (JsonProcessingException e) {
            logger.error(e.getMessage(), e);
        }
        return testReturn;
    }
}
