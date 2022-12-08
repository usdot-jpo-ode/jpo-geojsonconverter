package us.dot.its.jpo.geojsonconverter.pojos.geojson;

import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class LineString extends Geometry {
    private final double[][] coordinates;
    private final double[] bbox;

    @JsonCreator
    public LineString(@JsonProperty("coordinates") double [][] coordinates) {
        super();
        this.coordinates = coordinates;
        this.bbox = null;
    }

    public double[][] getCoordinates() {
        return coordinates;
    }

    public double[] getBbox() {
        return bbox;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof LineString)) {
            return false;
        }
        LineString lineString = (LineString) o;
        return Objects.equals(coordinates, lineString.coordinates) && Objects.equals(bbox, lineString.bbox);
    }

    @Override
    public int hashCode() {
        return Objects.hash(coordinates, bbox);
    }

    @Override
    public String toString() {
        return "{" +
            " coordinates='" + getCoordinates() + "'" +
            ", bbox='" + getBbox() + "'" +
            "}";
    }
}
