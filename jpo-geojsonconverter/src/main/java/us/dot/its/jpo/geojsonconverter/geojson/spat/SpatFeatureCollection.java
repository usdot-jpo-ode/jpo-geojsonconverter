package us.dot.its.jpo.geojsonconverter.geojson.spat;

import com.fasterxml.jackson.annotation.*;

import us.dot.its.jpo.geojsonconverter.geojson.BaseFeatureCollection;

public class SpatFeatureCollection extends BaseFeatureCollection<SpatFeature> {
    @JsonCreator
    public SpatFeatureCollection(@JsonProperty("features") SpatFeature[] features) {
        super(features);
    }
}
