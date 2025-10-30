package us.dot.its.jpo.geojsonconverter.pojos.geojson.bsm;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Generated;
import lombok.extern.slf4j.Slf4j;
import us.dot.its.jpo.geojsonconverter.pojos.geojson.BaseFeatureCollection;

// This is a helper class to allow for a ProcessedBsmCollection to be created with a list of ProcessedBsm Features
@Generated
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@Slf4j
public class ProcessedBsmCollection<TGeometry> extends BaseFeatureCollection<ProcessedBsm<TGeometry>> {
    @JsonCreator
    public ProcessedBsmCollection(@JsonProperty("features") ProcessedBsm<TGeometry>[] features) {
        super(features);
    }
}
