package us.dot.its.jpo.geojsonconverter.geojson;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class Point extends Geometry {
    private final double[] coordinates;
    private final double[] bbox;

    public Point (double longitude, double latitude) {
        super();
        this.coordinates = new double[] {longitude, latitude};
        this.bbox = null;
    }

    @JsonCreator
    public Point(@JsonProperty("coordinates") double [] coordinates) {
        super();
        this.coordinates = coordinates;
        this.bbox = null;
    }

    public double[] getCoordinates() {
        return coordinates;
    }

    public double[] getBbox() {
        return bbox;
    }
}
