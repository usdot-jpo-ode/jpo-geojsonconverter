package us.dot.its.jpo.geojsonconverter.partitioner;

import us.dot.its.jpo.ode.plugin.j2735.J2735IntersectionReferenceID;

import java.util.Objects;


/**
 * Kafka key for topics with an RSU ID and Intersection ID.
 */
public class RsuIntersectionKey implements RsuIdKey, IntersectionKey {
    
    private String rsuId;
    private int intersectionId;
    private int region;

    public RsuIntersectionKey() {}
 
    public RsuIntersectionKey(String rsuId, int intersectionId) {
        this.rsuId = rsuId;
        this.intersectionId = intersectionId;
        this.region = -1;
    }

    public RsuIntersectionKey(String rsuId, int intersectionId, int region) {
        this.rsuId = rsuId;
        this.intersectionId = intersectionId;
        this.region = region;
    }

    @Override
    public String getRsuId() {
        return this.rsuId;
    }

    
    public void setRsuId(String rsuId) {
        this.rsuId = rsuId;
    }

    @Override
    public int getIntersectionId() {
        return this.intersectionId;
    }

    public void setIntersectionId(int intersectionId) {
        this.intersectionId = intersectionId;
    }
    public void setIntersectionId(Integer intersectionId) {
        if (intersectionId != null) {
            setIntersectionId(intersectionId.intValue());
        } else {
            setIntersectionId(-1);
        }
    }

    /**
     * Sets both intersection ID and region with null checks
     * @param referenceID J2735IntersectionReferenceID
     */
    public void setIntersectionReferenceID(J2735IntersectionReferenceID referenceID) {
        if (referenceID != null) {
            setIntersectionId(referenceID.getId());
            setRegion(referenceID.getRegion());
        }
    }

    @Override
    public int getRegion() {
        return region;
    }
    public void setRegion(int region) {
        this.region = region;
    }
    public void setRegion(Integer region) {
        if (region != null) {
            setRegion(region.intValue());
        } else {
            // Use -1 to indicate region is missing
            setRegion(-1);
        }
    }


    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof RsuIntersectionKey)) {
            return false;
        }
        RsuIntersectionKey rsuIntersectionKey = (RsuIntersectionKey) o;
        return Objects.equals(rsuId, rsuIntersectionKey.rsuId) && intersectionId == rsuIntersectionKey.intersectionId
                && region == rsuIntersectionKey.region;
    }

    @Override
    public int hashCode() {
        return Objects.hash(rsuId, intersectionId, region);
    }
    



    @Override
    public String toString() {
        return "{" +
            " rsuId='" + getRsuId() + "'" +
            ", intersectionId='" + getIntersectionId() + "'" +
            ", region='" + getRegion() + "'" +
            "}";
    }
    



}
