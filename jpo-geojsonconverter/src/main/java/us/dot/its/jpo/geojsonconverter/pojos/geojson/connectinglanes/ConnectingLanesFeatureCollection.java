package us.dot.its.jpo.geojsonconverter.pojos.geojson.connectinglanes;

import com.fasterxml.jackson.annotation.*;

import us.dot.its.jpo.geojsonconverter.pojos.geojson.BaseFeatureCollection;

public class ConnectingLanesFeatureCollection<TGeometry> extends BaseFeatureCollection<ConnectingLanesFeature<TGeometry>> {
    @JsonCreator
    public ConnectingLanesFeatureCollection(@JsonProperty("features") ConnectingLanesFeature<TGeometry>[] features) {
        super(features);
    }
}
