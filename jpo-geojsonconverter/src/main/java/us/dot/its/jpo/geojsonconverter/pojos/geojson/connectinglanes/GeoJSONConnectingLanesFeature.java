package us.dot.its.jpo.geojsonconverter.pojos.geojson.connectinglanes;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import us.dot.its.jpo.geojsonconverter.pojos.geojson.LineString;

@JsonPropertyOrder({"type", "id", "geometry", "properties"})
public class GeoJSONConnectingLanesFeature extends ConnectingLanesFeature {
    @JsonInclude(Include.NON_EMPTY)
    protected final LineString geometry;

    @JsonCreator
    public GeoJSONConnectingLanesFeature(
        @JsonProperty("id") String id, 
        @JsonProperty("geometry") LineString geometry, 
        @JsonProperty("properties") ConnectingLanesProperties properties) {
        super(id, properties);
        this.geometry = geometry;
    }

    public LineString getGeometry() {
        return geometry;
    }
}
