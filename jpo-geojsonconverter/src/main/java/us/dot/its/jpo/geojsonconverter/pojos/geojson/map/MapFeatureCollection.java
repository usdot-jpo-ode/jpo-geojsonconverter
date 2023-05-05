package us.dot.its.jpo.geojsonconverter.pojos.geojson.map;

import com.fasterxml.jackson.annotation.*;

import us.dot.its.jpo.geojsonconverter.DateJsonMapper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@JsonIgnoreProperties(value={ "type" }, allowGetters=true)
@JsonPropertyOrder({"type", "features"})
public class MapFeatureCollection<TFeature> {
    private static Logger logger = LoggerFactory.getLogger(MapFeatureCollection.class);

    private final TFeature[] features;

    @JsonCreator
    public MapFeatureCollection(@JsonProperty("features") TFeature[] features) {
        this.features = features;
    }

    @JsonProperty("type")
    public String getType() {
        return "FeatureCollection";
    }

    public TFeature[] getFeatures() {
        return features;
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
