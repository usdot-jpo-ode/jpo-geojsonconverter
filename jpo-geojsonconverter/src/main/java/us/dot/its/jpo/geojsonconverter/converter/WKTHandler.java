package us.dot.its.jpo.geojsonconverter.converter;

import java.util.ArrayList;
import java.util.List;

import org.locationtech.jts.io.WKTWriter;

import us.dot.its.jpo.geojsonconverter.pojos.geojson.connectinglanes.*;
import us.dot.its.jpo.geojsonconverter.pojos.geojson.map.*;

import org.locationtech.jts.geom.Coordinate;

public class WKTHandler {
    public static ProcessedMap<WKTMapFeature, WKTConnectingLanesFeature> processedMapGeoJSON2WKT(ProcessedMap<GeoJsonMapFeature, GeoJsonConnectingLanesFeature> mapGeoJSON) {
        List<WKTMapFeature> wktMapFeatures = new ArrayList<>();
        for (GeoJsonMapFeature feature : mapGeoJSON.getMapFeatureCollection().getFeatures()) 
            wktMapFeatures.add(mapFeatureGeoJSON2WKT(feature));

        List<WKTConnectingLanesFeature> wktConnectingLanesFeatures = new ArrayList<>();
        for (GeoJsonConnectingLanesFeature feature : mapGeoJSON.getConnectingLanesFeatureCollection().getFeatures()) 
            wktConnectingLanesFeatures.add(clFeatureGeoJSON2WKT(feature));

        ProcessedMap<WKTMapFeature, WKTConnectingLanesFeature> wktProcessedMap = new ProcessedMap<WKTMapFeature, WKTConnectingLanesFeature>();
        wktProcessedMap.setMapFeatureCollection(new MapFeatureCollection<WKTMapFeature>(wktMapFeatures.toArray(new WKTMapFeature[0])));
        wktProcessedMap.setConnectingLanesFeatureCollection(new ConnectingLanesFeatureCollection<WKTConnectingLanesFeature>(wktConnectingLanesFeatures.toArray(new WKTConnectingLanesFeature[0])));
        wktProcessedMap.setProperties(mapGeoJSON.getProperties());
        return wktProcessedMap;
    }

    private static WKTMapFeature mapFeatureGeoJSON2WKT(GeoJsonMapFeature geojsonFeature) {
        String wktGeometry = coordinates2WKTLineString(geojsonFeature.getGeometry().getCoordinates());
        return new WKTMapFeature(geojsonFeature.getId(), wktGeometry, geojsonFeature.getProperties());
    }

    private static WKTConnectingLanesFeature clFeatureGeoJSON2WKT(GeoJsonConnectingLanesFeature geojsonFeature) {
        String wktGeometry = coordinates2WKTLineString(geojsonFeature.getGeometry().getCoordinates());
        return new WKTConnectingLanesFeature(geojsonFeature.getId(), wktGeometry, geojsonFeature.getProperties());
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
