package us.dot.its.jpo.geojsonconverter.partitioner;

import lombok.Data;

/**
 * Kafka key for RTCM messages.
 * Partition on RSU ID.
 * May include RTCM Station ID.
 */
@Data
public class RsuStationIdKey implements RsuIdKey {

    private String rsuId;
    private Integer stationId;

    @Override
    public String getRsuId() {
        return rsuId;
    }
}
