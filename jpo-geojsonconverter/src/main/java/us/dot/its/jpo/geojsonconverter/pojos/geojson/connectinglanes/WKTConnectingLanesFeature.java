package us.dot.its.jpo.geojsonconverter.pojos.geojson.connectinglanes;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonPropertyOrder({"type", "id", "geometry", "properties"})
public class WKTConnectingLanesFeature extends ConnectingLanesFeature {
    @JsonInclude(Include.NON_EMPTY)
    protected final String geometry;

    @JsonCreator
    public WKTConnectingLanesFeature(
        @JsonProperty("id") String id, 
        @JsonProperty("geometry") String geometry, 
        @JsonProperty("properties") ConnectingLanesProperties properties) {
        super(id, properties);
        this.geometry = geometry;
    }

    public String getGeometry() {
        return geometry;
    }
}
