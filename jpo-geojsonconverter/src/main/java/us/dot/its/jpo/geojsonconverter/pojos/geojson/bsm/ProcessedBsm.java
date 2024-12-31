package us.dot.its.jpo.geojsonconverter.pojos.geojson.bsm;

import com.fasterxml.jackson.annotation.*;

import us.dot.its.jpo.geojsonconverter.pojos.ProcessedValidationMessage;
import us.dot.its.jpo.geojsonconverter.pojos.geojson.BaseFeature;

import java.time.ZonedDateTime;
import java.util.List;

public class ProcessedBsm<TGeometry> extends BaseFeature<Integer, TGeometry, BsmProperties> {
    @JsonCreator
    public ProcessedBsm(
            @JsonProperty("id") Integer id,
            @JsonProperty("geometry") TGeometry geometry, 
            @JsonProperty("properties") BsmProperties properties) {
        super(id, geometry, properties);
    }


}
