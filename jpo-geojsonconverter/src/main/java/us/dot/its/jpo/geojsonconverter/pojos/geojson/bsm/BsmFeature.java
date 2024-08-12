package us.dot.its.jpo.geojsonconverter.pojos.geojson.bsm;

import com.fasterxml.jackson.annotation.*;

import us.dot.its.jpo.geojsonconverter.pojos.geojson.BaseFeature;

public class BsmFeature<TGeometry> extends BaseFeature<Integer, TGeometry, BsmProperties> {
    @JsonCreator
    public BsmFeature(
            @JsonProperty("id") Integer id,
            @JsonProperty("geometry") TGeometry geometry, 
            @JsonProperty("properties") BsmProperties properties) {
        super(id, geometry, properties);
    }
}
