package us.dot.its.jpo.geojsonconverter.partitioner;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import us.dot.its.jpo.asn.j2735.r2024.Common.IntersectionID;
import us.dot.its.jpo.asn.j2735.r2024.Common.IntersectionReferenceID;
import us.dot.its.jpo.asn.j2735.r2024.Common.RoadRegulatorID;

/**
 * Kafka key for topics with an RSU ID and Intersection ID.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RsuIntersectionKey implements RsuIdKey, IntersectionKey, Comparable<RsuIntersectionKey> {

    private String rsuId;
    private int intersectionId;
    private int region;

    public RsuIntersectionKey(String rsuId, int intersectionId) {
        this.rsuId = rsuId;
        this.intersectionId = intersectionId;
        this.region = -1;
    }

    public void setIntersectionId(int intersectionId) {
        this.intersectionId = intersectionId;
    }

    public void setIntersectionId(IntersectionID intersectionId) {
        if (intersectionId != null) {
            setIntersectionId((int) intersectionId.getValue());
        } else {
            // Use -1 to indicate intersection id is missing
            setIntersectionId(-1);
        }
    }

    public void setRegion(int region) {
        this.region = region;
    }

    public void setRegion(RoadRegulatorID region) {
        if (region != null) {
            setRegion((int) region.getValue());
        } else {
            // Use -1 to indicate region is missing
            setRegion(-1);
        }
    }

    /**
     * Sets both intersection ID and region with null checks
     * 
     * @param referenceID IntersectionReferenceID
     */
    public void setIntersectionReferenceID(IntersectionReferenceID referenceID) {
        if (referenceID != null) {
            setIntersectionId(referenceID.getId());
            setRegion(referenceID.getRegion());
        }
    }

    @Override
    public String toString() {
        return "{" + " rsuId='" + getRsuId() + "'" + ", intersectionId='" + getIntersectionId() + "'" + ", region='"
                + getRegion() + "'" + "}";
    }

    // Implement comparable interface to allow easily sorting the in a predictable order
    // e.g. in SortedSet.
    @Override
    public int compareTo(RsuIntersectionKey o) {
        int compareRsuId = StringUtils.compare(getRsuId(), o.getRsuId());
        if (compareRsuId != 0) {
            return compareRsuId;
        }
        int compareRegion = Integer.compare(getRegion(), o.getRegion());
        if (compareRegion != 0) {
            return compareRegion;
        }
        return Integer.compare(getIntersectionId(), o.getIntersectionId());
    }
}
