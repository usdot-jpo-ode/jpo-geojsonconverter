package us.dot.its.jpo.geojsonconverter.pojos.geojson.connectinglanes;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonIgnoreProperties(value={ "type" }, allowGetters=true)
public abstract class ConnectingLanesFeature {
    @JsonInclude(Include.NON_EMPTY)
    protected final String id;
    protected final ConnectingLanesProperties properties;

    @JsonCreator
    public ConnectingLanesFeature(
            @JsonProperty("id") String id,
            @JsonProperty("properties") ConnectingLanesProperties properties) {
        this.id = id;
        this.properties = properties;
    }

    @JsonProperty("type")
    public String getType() {
        return "Feature";
    }

    public String getId() {
        return id;
    }

    public ConnectingLanesProperties getProperties() {
        return properties;
    }
}
