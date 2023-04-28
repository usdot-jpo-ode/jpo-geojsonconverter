package us.dot.its.jpo.geojsonconverter.pojos.geojson.connectinglanes;

import com.fasterxml.jackson.annotation.*;

@JsonPropertyOrder({"type", "features"})
public class ConnectingLanesFeatureCollection {
    private final ConnectingLanesFeature[] features;

    @JsonCreator
    public ConnectingLanesFeatureCollection(@JsonProperty("features") ConnectingLanesFeature[] features) {
        this.features = features;
    }

    @JsonProperty("type")
    public String getType() {
        return "FeatureCollection";
    }

    public ConnectingLanesFeature[] getFeatures() {
        return features;
    }
}
