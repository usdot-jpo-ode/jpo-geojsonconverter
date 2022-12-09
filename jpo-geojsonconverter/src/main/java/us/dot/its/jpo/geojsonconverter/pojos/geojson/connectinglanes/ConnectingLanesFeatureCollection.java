package us.dot.its.jpo.geojsonconverter.pojos.geojson.connectinglanes;

import com.fasterxml.jackson.annotation.*;

import us.dot.its.jpo.geojsonconverter.pojos.geojson.BaseFeatureCollection;

public class ConnectingLanesFeatureCollection extends BaseFeatureCollection<ConnectingLanesFeature> {
    @JsonCreator
    public ConnectingLanesFeatureCollection(@JsonProperty("features") ConnectingLanesFeature[] features) {
        super(features);
    }
}
