package us.dot.its.jpo.geojsonconverter.partitioner;

/**
 * Interface for keys that act as an RSU ID based partitioning scheme.
 */
public interface RsuIdKey {
    
    /**
     * @return The RSU ID (An IP address or other unique identifier for the RSU)
     */
    String getRsuId();

}
