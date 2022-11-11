package us.dot.its.jpo.geojsonconverter.pojos.geojson.spat;

import com.fasterxml.jackson.annotation.*;

import us.dot.its.jpo.geojsonconverter.pojos.geojson.BaseFeatureCollection;

public class SpatFeatureCollection extends BaseFeatureCollection<SpatFeature> {
    @JsonCreator
    public SpatFeatureCollection(@JsonProperty("features") SpatFeature[] features) {
        super(features);
    }
}
