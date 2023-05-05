package us.dot.its.jpo.geojsonconverter.pojos.geojson;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import us.dot.its.jpo.geojsonconverter.DateJsonMapper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@JsonIgnoreProperties(value={ "type" }, allowGetters=true)
@JsonPropertyOrder({"type", "id", "geometry", "properties"})
public abstract class BaseFeature<TId, TGeometry, TProperties> {
    private static Logger logger = LoggerFactory.getLogger(BaseFeature.class);

    @JsonInclude(Include.NON_EMPTY)
    protected final TId id;
    protected final TGeometry geometry;
    protected final TProperties properties;

    @JsonCreator
    public BaseFeature(
            @JsonProperty("id") TId id,
            @JsonProperty("geometry") TGeometry geometry, 
            @JsonProperty("properties") TProperties properties) {
        this.id = id;
        this.geometry = geometry;
        this.properties = properties;
    }

    @JsonProperty("type")
    public String getType() {
        return "Feature";
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

    @Override
    public String toString() {
        ObjectMapper mapper = DateJsonMapper.getInstance();
        String testReturn = "";
        try {
            testReturn = (mapper.writeValueAsString(this));
        } catch (JsonProcessingException e) {
            logger.error(e.getMessage(), e);
        }
        return testReturn;
    }
}
