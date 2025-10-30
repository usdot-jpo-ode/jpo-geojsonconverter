package us.dot.its.jpo.geojsonconverter.pojos.geojson.psm;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Generated;
import lombok.extern.slf4j.Slf4j;
import us.dot.its.jpo.geojsonconverter.pojos.geojson.BaseFeatureCollection;

// This is a helper class to allow for a ProcessedPsmCollection to be created with a list of ProcessedPsm Features
@Generated
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@Slf4j
public class ProcessedPsmCollection<TGeometry> extends BaseFeatureCollection<ProcessedPsm<TGeometry>> {
    @JsonCreator
    public ProcessedPsmCollection(@JsonProperty("features") ProcessedPsm<TGeometry>[] features) {
        super(features);
    }
}
