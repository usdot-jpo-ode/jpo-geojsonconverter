package us.dot.its.jpo.geojsonconverter.pojos.geojson.map;

import java.util.Objects;

import us.dot.its.jpo.geojsonconverter.DateJsonMapper;
import us.dot.its.jpo.geojsonconverter.pojos.geojson.connectinglanes.ConnectingLanesFeatureCollection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class ProcessedMapPojo {
    private static Logger logger = LoggerFactory.getLogger(MapProperties.class);

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

    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof ProcessedMapPojo)) {
            return false;
        }
        ProcessedMapPojo processedMapPojo = (ProcessedMapPojo) o;
        return Objects.equals(mapFeatureCollection, processedMapPojo.mapFeatureCollection) && Objects.equals(connectingLanesFeatureCollection, processedMapPojo.connectingLanesFeatureCollection);
    }

    @Override
    public int hashCode() {
        return Objects.hash(mapFeatureCollection, connectingLanesFeatureCollection);
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
