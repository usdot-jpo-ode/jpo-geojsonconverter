package us.dot.its.jpo.geojsonconverter.pojos.geojson;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;

public abstract class GeoJSON {
    private static final ObjectMapper mapper = new ObjectMapper();

    @JsonCreator
    public GeoJSON() {
    }
    
    public String toString() {
        try {
            return mapper.writeValueAsString(this);
        } catch (Exception e) {
            //return String.format("Unhandled exception occured when serializing this instance: %s", e.getMessage());
            throw new RuntimeException(e);
        } 
    }

    /**
     * Override with the name to be used as the type attribute in 
     * the generated GeoJSON for the element.
     * 
     */
    @JsonIgnore
    protected abstract String getGeoJSONType();


    @JsonProperty("type")
    public String getType() {
        return getGeoJSONType();
    }

    @JsonIgnore
    public void setType(String type) {
        // Does nothing, to make type not deserializable
    }

}
