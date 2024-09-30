package us.dot.its.jpo.geojsonconverter.partitioner;

import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.common.utils.Utils;
import org.apache.kafka.streams.processor.StreamPartitioner;

import us.dot.its.jpo.geojsonconverter.serialization.serializers.JsonSerializer;

public class RsuLogKeyPartitioner<K, V> implements StreamPartitioner<K, V> {
    @Override
    public Integer partition(String topic, K key, V value, int numPartitions) {
        byte[] partitionBytes;

        if (key instanceof RsuLogKey) {
            // If the key is an object with an RSU ID, partition on it
            var rsuIdKey = (RsuLogKey)key;
            if (rsuIdKey.getRsuId() != null && !rsuIdKey.getRsuId().isEmpty())
                partitionBytes = serializeString(topic, rsuIdKey.getRsuId());
            else if (rsuIdKey.getLogId() != null && !rsuIdKey.getLogId().isEmpty())
                partitionBytes = serializeString(topic, rsuIdKey.getLogId());
            else if (rsuIdKey.getBsmId() != null && !rsuIdKey.getBsmId().isEmpty())
                partitionBytes = serializeString(topic, rsuIdKey.getBsmId());
            else
                partitionBytes = serializeObj(topic, key);
        } else {
            // If the key does not have an RSU ID, partition on the hashed key as usual.
            partitionBytes = serializeObj(topic, key);
        }

        return Utils.toPositive(Utils.murmur2(partitionBytes)) % numPartitions;
    }

    private byte[] serializeString(String topic, String str) {
        byte[] partitionBytes;
        try (var serializer = Serdes.String().serializer()) {
            partitionBytes = serializer.serialize(topic, str); 
        }
        return partitionBytes;
    }

    private byte[] serializeObj(String topic, K obj) {
        byte[] partitionBytes;
        try (var serializer = new JsonSerializer<K>()) {
            partitionBytes = serializer.serialize(topic, obj);
        }
        return partitionBytes;
    }
}
