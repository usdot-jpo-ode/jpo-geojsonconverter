package us.dot.its.jpo.geojsonconverter.partitioner;

import org.apache.kafka.common.utils.Utils;
import org.apache.kafka.streams.processor.StreamPartitioner;
import us.dot.its.jpo.geojsonconverter.serialization.serializers.JsonSerializer;

import java.nio.ByteBuffer;

/**
 * Partitioner that partitions based only on Intersection ID for
 * objects that implement the {@link IntersectionKey} class.
 *
 * <p>Partitioning ignores the region, so that items with or without a region defined will be on the same partition.</p>
 *
 * <p>Objects that don't implement the {@link IntersectionKey} are partitioned on the entire serialized key, the same as the kafka default behavior.</p>
 */
public class IntersectionIdPartitioner<K, V> implements StreamPartitioner<K, V> {

    @Override
    public Integer partition(String topic, K key, V value, int numPartitions) {
        byte[] partitionBytes;

        if (key instanceof IntersectionKey) {
            var intersectionKey = (IntersectionKey)key;
            int intersectionId = intersectionKey.getIntersectionId();
            partitionBytes = ByteBuffer.allocate(Integer.SIZE/8).putInt(intersectionId).array();
        } else {
            try (var serializer = new JsonSerializer<K>()) {
                partitionBytes = serializer.serialize(topic, key);
            }
        }

        return Utils.toPositive(Utils.murmur2(partitionBytes)) % numPartitions;
    }

}
