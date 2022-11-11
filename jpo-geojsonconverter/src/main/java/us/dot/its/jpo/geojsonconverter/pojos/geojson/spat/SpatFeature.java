package us.dot.its.jpo.geojsonconverter.pojos.geojson.spat;

import com.fasterxml.jackson.annotation.*;

import us.dot.its.jpo.geojsonconverter.pojos.geojson.BaseFeature;
import us.dot.its.jpo.geojsonconverter.pojos.geojson.Point;

public class SpatFeature extends BaseFeature<Integer, Point, SpatProperties> {
    @JsonCreator
    public SpatFeature(
            @JsonProperty("id") Integer id, 
            @JsonProperty("geometry") Point geometry, 
            @JsonProperty("properties") SpatProperties properties) {
        super(id, geometry, properties);     
    }
}