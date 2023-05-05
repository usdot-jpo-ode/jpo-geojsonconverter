package us.dot.its.jpo.geojsonconverter.pojos.geojson.map;

import com.fasterxml.jackson.annotation.*;

import us.dot.its.jpo.geojsonconverter.pojos.geojson.BaseFeature;

public class MapFeature<TGeometry> extends BaseFeature<Integer, TGeometry, MapProperties> {
    @JsonCreator
    public MapFeature(
            @JsonProperty("id") Integer id,
            @JsonProperty("geometry") TGeometry geometry, 
            @JsonProperty("properties") MapProperties properties) {
        super(id, geometry, properties);
    }
}