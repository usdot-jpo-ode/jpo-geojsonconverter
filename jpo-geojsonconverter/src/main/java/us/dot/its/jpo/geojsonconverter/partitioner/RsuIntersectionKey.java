package us.dot.its.jpo.geojsonconverter.partitioner;

import java.util.Objects;


/**
 * Kafka key for topics with an RSU ID and Intersection ID.
 */
public class RsuIntersectionKey implements RsuIdKey {
    
    private String rsuId;
    private int intersectionId;

    public RsuIntersectionKey() {}
 
    public RsuIntersectionKey(String rsuId, int intersectionId) {
        this.rsuId = rsuId;
        this.intersectionId = intersectionId;
    }

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
        RsuIntersectionKey rsuIntersectionKey = (RsuIntersectionKey) o;
        return Objects.equals(rsuId, rsuIntersectionKey.rsuId) && intersectionId == rsuIntersectionKey.intersectionId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(rsuId, intersectionId);
    }
    



    @Override
    public String toString() {
        return "{" +
            " rsuId='" + getRsuId() + "'" +
            ", intersectionId='" + getIntersectionId() + "'" +
            "}";
    }
    



}
