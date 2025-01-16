package us.dot.its.jpo.geojsonconverter.pojos.geojson.psm;

import com.fasterxml.jackson.annotation.*;

import us.dot.its.jpo.geojsonconverter.pojos.geojson.BaseFeature;

public class PsmFeature<TGeometry> extends BaseFeature<Integer, TGeometry, PsmProperties> {
    @JsonCreator
    public PsmFeature(
            @JsonProperty("id") Integer id,
            @JsonProperty("geometry") TGeometry geometry, 
            @JsonProperty("properties") PsmProperties properties) {
        super(id, geometry, properties);
    }
}
