package us.dot.its.jpo.geojsonconverter.pojos.geojson.connectinglanes;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Generated;
import lombok.extern.slf4j.Slf4j;
import us.dot.its.jpo.geojsonconverter.pojos.geojson.BaseFeatureCollection;

@Generated
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@Slf4j
public class ConnectingLanesFeatureCollection<TGeometry>
        extends BaseFeatureCollection<ConnectingLanesFeature<TGeometry>> {
    @JsonCreator
    public ConnectingLanesFeatureCollection(@JsonProperty("features") ConnectingLanesFeature<TGeometry>[] features) {
        super(features);
    }
}
