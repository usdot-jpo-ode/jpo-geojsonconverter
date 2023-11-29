package us.dot.its.jpo.geojsonconverter.partitioner;

/**
 * Interface for keys that act as an intersection based partitioning scheme.
 */
public interface IntersectionKey {

    /**
     * @return The J2735 Intersection ID
     */
    int getIntersectionId();

    /**
     * @return The J2735 Region (Road Regulator ID)
     */
    int getRegion();
}
