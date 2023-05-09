package us.dot.its.jpo.geojsonconverter.pojos.geojson.connectinglanes;

import com.fasterxml.jackson.annotation.*;

import us.dot.its.jpo.geojsonconverter.pojos.geojson.BaseFeature;

public class ConnectingLanesFeature<TGeometry> extends BaseFeature<String, TGeometry, ConnectingLanesProperties> {
    @JsonCreator
    public ConnectingLanesFeature(
            @JsonProperty("id") String id,
            @JsonProperty("geometry") TGeometry geometry, 
            @JsonProperty("properties") ConnectingLanesProperties properties) {
        super(id, geometry, properties);
    }
}
