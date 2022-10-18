package us.dot.its.jpo.geojsonconverter.geojson.map;

import com.fasterxml.jackson.annotation.*;

import us.dot.its.jpo.geojsonconverter.geojson.BaseFeatureCollection;

public class MapFeatureCollection extends BaseFeatureCollection<MapFeature> {
    @JsonCreator
    public MapFeatureCollection(@JsonProperty("features") MapFeature[] features) {
        super(features);
    }
}
