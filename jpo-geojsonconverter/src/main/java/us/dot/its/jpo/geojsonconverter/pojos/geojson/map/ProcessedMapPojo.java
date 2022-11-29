package us.dot.its.jpo.geojsonconverter.pojos.geojson.map;

import us.dot.its.jpo.geojsonconverter.pojos.geojson.connectinglanes.ConnectingLanesFeatureCollection;

public class ProcessedMapPojo {
    MapFeatureCollection mapFeatureCollection;
    ConnectingLanesFeatureCollection connectingLanesFeatureCollection;

    public void setMapFeatureCollection(MapFeatureCollection mapFeatureCollection) {
        this.mapFeatureCollection = mapFeatureCollection;
    }

    public MapFeatureCollection getMapFeatureCollection() {
        return this.mapFeatureCollection;
    }

    public void setConnectingLanesFeatureCollection(ConnectingLanesFeatureCollection connectingLanesFeatureCollection) {
        this.connectingLanesFeatureCollection = connectingLanesFeatureCollection;
    }

    public ConnectingLanesFeatureCollection getConnectingLanesFeatureCollection() {
        return this.connectingLanesFeatureCollection;
    }
}
