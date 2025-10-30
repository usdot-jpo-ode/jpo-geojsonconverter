package us.dot.its.jpo.geojsonconverter.pojos.common;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class ProcessedPosition3D {
    private Integer lat;
    @JsonProperty("long")
    private Integer long_;
    private Integer elevation;
}
