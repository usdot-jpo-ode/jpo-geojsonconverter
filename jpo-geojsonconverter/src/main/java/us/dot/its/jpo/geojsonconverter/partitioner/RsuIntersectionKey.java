package us.dot.its.jpo.geojsonconverter.partitioner;

import java.util.Objects;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;


/**
 * Kafka key for topics with an RSU ID and Intersection ID.
 */
public class RsuIntersectionKey implements RsuIdKey {
    
    private String rsuId;
    private int intersectionId;

    private static final Logger logger = LoggerFactory.getLogger(RsuIntersectionKey.class);

    @Override
    public String getRsuId() {
        return this.rsuId;
    }

    
    public void setRsuId(String rsuId) {
        this.rsuId = rsuId;
    }

    public int getIntersectionId() {
        return this.intersectionId;
    }

    public void setIntersectionId(int intersectionId) {
        this.intersectionId = intersectionId;
    }


    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof RsuIntersectionKey)) {
            return false;
        }
        RsuIntersectionKey processedSpatKey = (RsuIntersectionKey) o;
        return Objects.equals(rsuId, processedSpatKey.rsuId) && intersectionId == processedSpatKey.intersectionId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(rsuId, intersectionId);
    }


    @Override
    public String toString() {
        var mapper = new ObjectMapper();
        try {
            return mapper.writeValueAsString(this);
        } catch (JsonProcessingException e) {
            String errMsg = String.format("Exception serializing to JSON string: %s", e.getMessage());
            logger.error(errMsg, e);
            return String.format("{ \"Exception\": \"%s\" }", errMsg);
        }
    }



}
