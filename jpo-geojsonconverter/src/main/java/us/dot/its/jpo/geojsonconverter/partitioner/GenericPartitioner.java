package us.dot.its.jpo.geojsonconverter.partitioner;

import org.apache.kafka.common.utils.Utils;
import org.apache.kafka.streams.processor.StreamPartitioner;

import us.dot.its.jpo.geojsonconverter.serialization.serializers.JsonSerializer;

public class GenericPartitioner<K, V> implements StreamPartitioner<K, V> {
    @Override
    public Integer partition(String topic, K key, V value, int numPartitions) {
        byte[] partitionBytes;
        try (var serializer = new JsonSerializer<K>()) {
            partitionBytes = serializer.serialize(topic, key);
        }
        return Utils.toPositive(Utils.murmur2(partitionBytes)) % numPartitions;
    }
}
