package us.dot.its.jpo.geojsonconverter.pojos.geojson.connectinglanes;

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
public class ConnectingLanesFeature<TGeometry> extends BaseFeature<String, TGeometry, ConnectingLanesProperties> {
    @JsonCreator
    public ConnectingLanesFeature(@JsonProperty("id") String id, @JsonProperty("geometry") TGeometry geometry,
            @JsonProperty("properties") ConnectingLanesProperties properties) {
        super(id, geometry, properties);
    }
}
