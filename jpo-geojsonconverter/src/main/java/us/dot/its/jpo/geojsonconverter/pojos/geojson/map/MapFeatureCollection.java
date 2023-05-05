package us.dot.its.jpo.geojsonconverter.pojos.geojson.map;

import com.fasterxml.jackson.annotation.*;

import us.dot.its.jpo.geojsonconverter.pojos.geojson.BaseFeatureCollection;

public class MapFeatureCollection<TGeometry> extends BaseFeatureCollection<MapFeature<TGeometry>> {
    @JsonCreator
    public MapFeatureCollection(@JsonProperty("features") MapFeature<TGeometry>[] features) {
        super(features);
    }
}
