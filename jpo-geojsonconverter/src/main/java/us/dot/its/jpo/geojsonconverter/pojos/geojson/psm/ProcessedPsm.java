package us.dot.its.jpo.geojsonconverter.pojos.geojson.psm;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Generated;
import lombok.extern.slf4j.Slf4j;
import us.dot.its.jpo.geojsonconverter.pojos.geojson.BaseFeature;

@Generated
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@Slf4j
public class ProcessedPsm<Point> extends BaseFeature<Integer, Point, PsmProperties> {
    @JsonCreator
    public ProcessedPsm(@JsonProperty("id") Integer id, @JsonProperty("geometry") Point geometry,
            @JsonProperty("properties") PsmProperties properties) {
        super(id, geometry, properties);
    }
}
