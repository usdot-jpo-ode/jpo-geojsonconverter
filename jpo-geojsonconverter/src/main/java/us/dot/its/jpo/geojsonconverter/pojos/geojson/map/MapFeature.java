package us.dot.its.jpo.geojsonconverter.pojos.geojson.map;

import com.fasterxml.jackson.annotation.*;

import us.dot.its.jpo.geojsonconverter.pojos.geojson.BaseFeature;
import us.dot.its.jpo.geojsonconverter.pojos.geojson.LineString;

public class MapFeature extends BaseFeature<Integer, LineString, MapProperties> {
    @JsonCreator
    public MapFeature(
            @JsonProperty("id") Integer id, 
            @JsonProperty("geometry") LineString geometry, 
            @JsonProperty("properties") MapProperties properties) {
        super(id, geometry, properties);     
    }
}