package us.dot.its.jpo.geojsonconverter.converter;

import java.util.ArrayList;
import java.util.List;

import org.locationtech.jts.io.WKTWriter;
import org.locationtech.jts.io.ParseException;
import org.locationtech.jts.io.WKTReader;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.Geometry;

public class WKTHandler {
    private static WKTReader wktReader = new WKTReader();

    public static String coordinates2WKTLineString(double[][] coordinates) {
        Coordinate[] jtsCoordinates = convert2JTSCoordinates(coordinates);
        return WKTWriter.toLineString(jtsCoordinates);
    }

    public static double[][] wktLineString2Coordinates(String wkt) throws ParseException {
        Geometry geom = wktReader.read(wkt);
        return convert2DoubleArray(geom.getCoordinates());
    }

    private static Coordinate[] convert2JTSCoordinates(double[][] coordinates) {
        List<Coordinate> jtsCoordinates = new ArrayList<>();
        for (double[] coord : coordinates) {
            jtsCoordinates.add(new Coordinate(coord[0], coord[1]));
        }
        return jtsCoordinates.toArray(Coordinate[]::new);
    }

    private static double[][] convert2DoubleArray(Coordinate[] coordinates) {
        List<List<Double>> doubleCoordinates = new ArrayList<>();
        for (Coordinate coord : coordinates) {
            List<Double> coordinate = new ArrayList<>();
            coordinate.add(coord.x);
            coordinate.add(coord.y);
            doubleCoordinates.add(coordinate);
        }
        double[][] coordinatesArray = doubleCoordinates.stream()
                        .map(l -> l.stream().mapToDouble(Double::doubleValue).toArray())
                        .toArray(double[][]::new);
        return coordinatesArray;
    }
}
