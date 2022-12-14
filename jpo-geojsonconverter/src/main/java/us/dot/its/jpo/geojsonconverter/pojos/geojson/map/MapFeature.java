package us.dot.its.jpo.geojsonconverter.pojos.geojson.map;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import us.dot.its.jpo.geojsonconverter.DateJsonMapper;
import us.dot.its.jpo.geojsonconverter.pojos.geojson.BaseFeature;
import us.dot.its.jpo.geojsonconverter.pojos.geojson.LineString;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MapFeature extends BaseFeature<Integer, LineString, MapProperties> {
    private static Logger logger = LoggerFactory.getLogger(MapFeature.class);

    @JsonCreator
    public MapFeature(
            @JsonProperty("id") Integer id, 
            @JsonProperty("geometry") LineString geometry, 
            @JsonProperty("properties") MapProperties properties) {
        super(id, geometry, properties);     
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