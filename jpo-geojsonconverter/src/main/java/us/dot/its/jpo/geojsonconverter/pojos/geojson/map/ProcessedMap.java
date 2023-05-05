package us.dot.its.jpo.geojsonconverter.pojos.geojson.map;

import com.fasterxml.jackson.annotation.*;

import java.util.Objects;

import us.dot.its.jpo.geojsonconverter.DateJsonMapper;
import us.dot.its.jpo.geojsonconverter.pojos.geojson.connectinglanes.ConnectingLanesFeatureCollection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@JsonPropertyOrder({"mapFeatureCollection", "connectingLanesFeatureCollection", "properties"})
public class ProcessedMap<TMapFeature, TConnectingLanesFeature> {
    private static Logger logger = LoggerFactory.getLogger(MapProperties.class);

    MapFeatureCollection<TMapFeature> mapFeatureCollection;
    ConnectingLanesFeatureCollection<TConnectingLanesFeature> connectingLanesFeatureCollection;
    MapSharedProperties properties;

    public void setProperties(MapSharedProperties properties) {
        this.properties = properties;
    }

    public MapSharedProperties getProperties() {
        return this.properties;
    }

    public void setMapFeatureCollection(MapFeatureCollection<TMapFeature> mapFeatureCollection) {
        this.mapFeatureCollection = mapFeatureCollection;
    }

    public MapFeatureCollection<TMapFeature> getMapFeatureCollection() {
        return this.mapFeatureCollection;
    }

    public void setConnectingLanesFeatureCollection(ConnectingLanesFeatureCollection<TConnectingLanesFeature> connectingLanesFeatureCollection) {
        this.connectingLanesFeatureCollection = connectingLanesFeatureCollection;
    }

    public ConnectingLanesFeatureCollection<TConnectingLanesFeature> getConnectingLanesFeatureCollection() {
        return this.connectingLanesFeatureCollection;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof ProcessedMap)) {
            return false;
        }

        @SuppressWarnings("unchecked")
        ProcessedMap<TMapFeature, TConnectingLanesFeature> processedMap = (ProcessedMap<TMapFeature, TConnectingLanesFeature>) o;
        return Objects.equals(properties, processedMap.properties) && Objects.equals(mapFeatureCollection, processedMap.mapFeatureCollection) && Objects.equals(connectingLanesFeatureCollection, processedMap.connectingLanesFeatureCollection);
    }

    @Override
    public int hashCode() {
        return Objects.hash(properties, mapFeatureCollection, connectingLanesFeatureCollection);
    }

    @Override
    public String toString() {
        ObjectMapper mapper = DateJsonMapper.getInstance();
        String testReturn = "";
        try {
            testReturn = (mapper.writeValueAsString(this));
        } catch (JsonProcessingException e) {
            logger.error(e.getMessage(), e);
        }
        return testReturn;
    }
}
