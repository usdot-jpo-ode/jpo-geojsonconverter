package us.dot.its.jpo.geojsonconverter.pojos.geojson;

import java.util.Arrays;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import lombok.Getter;

@Getter
@JsonInclude(Include.NON_NULL)
public class Point extends Geometry {
    private final double[] coordinates;
    private final double[] bbox;

    public Point(Double longitude, Double latitude) {
        super();
        if (longitude != null && latitude != null) {
            this.coordinates = new double[]{longitude, latitude};
        } else {
            coordinates = null;
        }
        this.bbox = null;
    }

    @JsonCreator
    public Point(@JsonProperty("coordinates") double[] coordinates) {
        super();
        this.coordinates = coordinates;
        this.bbox = null;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof Point point)) {
            return false;
        }
        return Arrays.equals(coordinates, point.coordinates) && Arrays.equals(bbox, point.bbox);
    }

    @Override
    public int hashCode() {
        return Objects.hash(Arrays.hashCode(coordinates), Arrays.hashCode(bbox));
    }

    @Override
    public String toString() {
        return "{" + " coordinates='" + Arrays.toString(getCoordinates()) + "'" + ", bbox='" + Arrays.toString(getBbox()) + "'" + "}";
    }

}
