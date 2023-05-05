package us.dot.its.jpo.geojsonconverter.pojos.geojson.map;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonIgnoreProperties(value={ "type" }, allowGetters=true)
public abstract class MapFeature {
    @JsonInclude(Include.NON_EMPTY)
    protected final Integer id;
    protected final MapProperties properties;

    @JsonCreator
    public MapFeature(
            @JsonProperty("id") Integer id,
            @JsonProperty("properties") MapProperties properties) {
        this.id = id;
        this.properties = properties;
    }

    @JsonProperty("type")
    public String getType() {
        return "Feature";
    }

    public Integer getId() {
        return id;
    }

    public MapProperties getProperties() {
        return properties;
    }
}