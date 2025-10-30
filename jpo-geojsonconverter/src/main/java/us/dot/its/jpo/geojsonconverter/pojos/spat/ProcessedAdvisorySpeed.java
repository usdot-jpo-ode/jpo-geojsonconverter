package us.dot.its.jpo.geojsonconverter.pojos.spat;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.Generated;
import lombok.extern.slf4j.Slf4j;
import us.dot.its.jpo.geojsonconverter.pojos.common.ProcessedSpeedConfidence;

@Data
@Slf4j
@Generated
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class ProcessedAdvisorySpeed {
    private ProcessedAdvisorySpeedType type;
    private Integer speed;
    private ProcessedSpeedConfidence confidence;
    private Integer distance;
    @JsonProperty("class")
    private Integer class_;
}
