package us.dot.its.jpo.geojsonconverter.pojos.geojson.connectinglanes;

import com.fasterxml.jackson.annotation.*;

import us.dot.its.jpo.geojsonconverter.pojos.geojson.BaseFeature;
import us.dot.its.jpo.geojsonconverter.pojos.geojson.LineString;

public class ConnectingLanesFeature extends BaseFeature<String, LineString, ConnectingLanesProperties> {
    @JsonCreator
    public ConnectingLanesFeature(
            @JsonProperty("id") String id, 
            @JsonProperty("geometry") LineString geometry, 
            @JsonProperty("properties") ConnectingLanesProperties properties) {
        super(id, geometry, properties);     
    }
}
