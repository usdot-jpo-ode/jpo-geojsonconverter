package us.dot.its.jpo.geojsonconverter.pojos.geojson.map;

import com.fasterxml.jackson.annotation.*;

import us.dot.its.jpo.geojsonconverter.pojos.geojson.BaseFeatureCollection;

public class MapFeatureCollection extends BaseFeatureCollection<MapFeature> {
    @JsonCreator
    public MapFeatureCollection(@JsonProperty("features") MapFeature[] features) {
        super(features);
    }
}
