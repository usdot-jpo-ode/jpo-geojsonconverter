package us.dot.its.jpo.geojsonconverter.pojos.geojson.map;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import us.dot.its.jpo.geojsonconverter.DateJsonMapper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@JsonPropertyOrder({"type", "id", "geometry", "properties"})
public class WKTMapFeature extends MapFeature {
    private static Logger logger = LoggerFactory.getLogger(WKTMapFeature.class);

    @JsonInclude(Include.NON_EMPTY)
    protected final String geometry;

    @JsonCreator
    public WKTMapFeature(
        @JsonProperty("id") Integer id, 
        @JsonProperty("geometry") String geometry, 
        @JsonProperty("properties") MapProperties properties) {
        super(id, properties);
        this.geometry = geometry;
    }

    public String getGeometry() {
        return geometry;
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
