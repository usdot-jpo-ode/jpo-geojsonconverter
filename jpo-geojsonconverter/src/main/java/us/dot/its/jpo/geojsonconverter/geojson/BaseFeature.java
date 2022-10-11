package us.dot.its.jpo.geojsonconverter.geojson;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;


/**
 * Base class for a feature class.
 * 
 * @param <TId> The type of the feature ID.
 * @param <TGeometry> The geometry type of the feature
 * @param <TProperties> A class containing GeoJSON properties that can be either a Map or a POJO class
 */
@JsonPropertyOrder({"type", "id", "geometry", "properties"})
public abstract class BaseFeature<TId, TGeometry extends Geometry, TProperties> 
    extends GeoJSON {

    @Override
    public String getGeoJSONType() {
        return "Feature";
    }

    @JsonInclude(Include.NON_EMPTY)
    protected final TId id;
    protected final TGeometry geometry;
    protected final TProperties properties;

    @JsonCreator
    public BaseFeature(
            @JsonProperty("geometry") TGeometry geometry,
            @JsonProperty("properties") TProperties properties) {
        super();
        this.id = null;
        this.geometry = geometry;
        this.properties = properties;
    }

    

    @JsonCreator
    public BaseFeature(
            @JsonProperty("id") TId id,
            @JsonProperty("geometry") TGeometry geometry,
            @JsonProperty("properties") TProperties properties) {
        super();
        this.id = id;
        this.geometry = geometry;
        this.properties = properties;
    }

    public TId getId() {
        return id;
    }

    public TGeometry getGeometry() {
        return geometry;
    }

    public TProperties getProperties() {
        return properties;
    }


 
 
}
