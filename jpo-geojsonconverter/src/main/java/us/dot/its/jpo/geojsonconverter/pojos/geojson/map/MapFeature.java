package us.dot.its.jpo.geojsonconverter.pojos.geojson.map;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Generated;
import lombok.extern.slf4j.Slf4j;
import us.dot.its.jpo.geojsonconverter.pojos.geojson.BaseFeature;

@Generated
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@Slf4j
public class MapFeature<TGeometry> extends BaseFeature<Integer, TGeometry, MapProperties> {
    @JsonCreator
    public MapFeature(@JsonProperty("id") Integer id, @JsonProperty("geometry") TGeometry geometry,
            @JsonProperty("properties") MapProperties properties) {
        super(id, geometry, properties);
    }
}
