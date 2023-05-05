package us.dot.its.jpo.geojsonconverter.converter;

import java.util.ArrayList;
import java.util.List;

import org.locationtech.jts.io.WKTWriter;

import us.dot.its.jpo.geojsonconverter.pojos.geojson.LineString;
import us.dot.its.jpo.geojsonconverter.pojos.geojson.connectinglanes.*;
import us.dot.its.jpo.geojsonconverter.pojos.geojson.map.*;

import org.locationtech.jts.geom.Coordinate;

public class WKTHandler {
    @SuppressWarnings("unchecked")
    public static ProcessedMap<String> processedMapGeoJSON2WKT(ProcessedMap<LineString> mapGeoJSON) {
        List<MapFeature<String>> wktMapFeatures = new ArrayList<>();
        for (MapFeature<LineString> feature : mapGeoJSON.getMapFeatureCollection().getFeatures()) 
            wktMapFeatures.add(mapFeatureGeoJSON2WKT(feature));

        List<ConnectingLanesFeature<String>> wktConnectingLanesFeatures = new ArrayList<>();
        for (ConnectingLanesFeature<LineString> feature : mapGeoJSON.getConnectingLanesFeatureCollection().getFeatures()) 
            wktConnectingLanesFeatures.add(clFeatureGeoJSON2WKT(feature));

        ProcessedMap<String> wktProcessedMap = new ProcessedMap<String>();
        wktProcessedMap.setMapFeatureCollection(new MapFeatureCollection<String>(wktMapFeatures.toArray(new MapFeature[0])));
        wktProcessedMap.setConnectingLanesFeatureCollection(new ConnectingLanesFeatureCollection<String>(wktConnectingLanesFeatures.toArray(new ConnectingLanesFeature[0])));
        wktProcessedMap.setProperties(mapGeoJSON.getProperties());
        return wktProcessedMap;
    }

    private static MapFeature<String> mapFeatureGeoJSON2WKT(MapFeature<LineString> geojsonFeature) {
        String wktGeometry = coordinates2WKTLineString(geojsonFeature.getGeometry().getCoordinates());
        return new MapFeature<String>(geojsonFeature.getId(), wktGeometry, geojsonFeature.getProperties());
    }

    private static ConnectingLanesFeature<String> clFeatureGeoJSON2WKT(ConnectingLanesFeature<LineString> geojsonFeature) {
        String wktGeometry = coordinates2WKTLineString(geojsonFeature.getGeometry().getCoordinates());
        return new ConnectingLanesFeature<String>(geojsonFeature.getId(), wktGeometry, geojsonFeature.getProperties());
    }

    public static String coordinates2WKTLineString(double[][] coordinates) {
        Coordinate[] jtsCoordinates = convert2JTSCoordinates(coordinates);
        return WKTWriter.toLineString(jtsCoordinates);
    }

    private static Coordinate[] convert2JTSCoordinates(double[][] coordinates) {
        List<Coordinate> jtsCoordinates = new ArrayList<>();
        for (double[] coord : coordinates) {
            jtsCoordinates.add(new Coordinate(coord[0], coord[1]));
        }
        return jtsCoordinates.toArray(Coordinate[]::new);
    }
}
