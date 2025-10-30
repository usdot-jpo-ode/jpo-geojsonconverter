package us.dot.its.jpo.geojsonconverter.pojos.common;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;


@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class ProcessedRegulatorySpeedLimit {
    private ProcessedSpeedLimitType type;
    private Integer speed;
}
