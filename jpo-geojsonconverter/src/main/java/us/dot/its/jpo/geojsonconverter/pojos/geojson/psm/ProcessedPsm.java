package us.dot.its.jpo.geojsonconverter.pojos.geojson.psm;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import us.dot.its.jpo.geojsonconverter.pojos.geojson.BaseFeature;

public class ProcessedPsm<Point> extends BaseFeature<Integer, Point, PsmProperties> {
    @JsonCreator
    public ProcessedPsm(@JsonProperty("id") Integer id, @JsonProperty("geometry") Point geometry,
            @JsonProperty("properties") PsmProperties properties) {
        super(id, geometry, properties);
    }
}
