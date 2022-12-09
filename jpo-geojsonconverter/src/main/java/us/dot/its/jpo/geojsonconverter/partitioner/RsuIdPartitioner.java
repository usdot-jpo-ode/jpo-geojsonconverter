package us.dot.its.jpo.geojsonconverter.partitioner;

import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.common.utils.Utils;
import org.apache.kafka.streams.processor.StreamPartitioner;

import us.dot.its.jpo.geojsonconverter.serialization.serializers.JsonSerializer;

public class RsuIdPartitioner<K, V> implements StreamPartitioner<K, V> {


    @Override
    public Integer partition(String topic, K key, V vlaue, int numPartitions) {
        
        byte[] partitionBytes;
        
        if (key instanceof RsuIdKey) {
            // If the key is an object with an RSU ID, partition on it
            var rsuIdKey = (RsuIdKey)key;
            String rsuId = rsuIdKey.getRsuId();
            try (var serializer = Serdes.String().serializer()) {
                partitionBytes = Serdes.String().serializer().serialize(topic, rsuId); 
            }          
        } else {
            // If the key does not have an RSU ID, partition on the hashed key as usual.
            try (var serializer = new JsonSerializer<K>()) {
                partitionBytes = serializer.serialize(topic, key);
            }
        }

        return Utils.toPositive(Utils.murmur2(partitionBytes)) % numPartitions;
    }


    
}
