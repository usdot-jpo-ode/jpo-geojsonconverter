package us.dot.its.jpo.geojsonconverter.pojos.geojson;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonTypeInfo(
    use = JsonTypeInfo.Id.NAME,
    include = JsonTypeInfo.As.EXISTING_PROPERTY,
    property = "type"
)
@JsonSubTypes( {
    @JsonSubTypes.Type(value=Point.class, name="Point"  ),
    @JsonSubTypes.Type(value=LineString.class, name="LineString"  )
} )

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonPropertyOrder({"type", "coordinates", "bbox"})
public abstract class Geometry extends GeoJSON {

    @Override
    public String getGeoJSONType(){
        return getClass().getSimpleName();
    }

    @JsonCreator
    public Geometry() {
        super();
    }
}
