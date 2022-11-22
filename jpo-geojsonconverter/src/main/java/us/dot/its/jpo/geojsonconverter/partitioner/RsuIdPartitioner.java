package us.dot.its.jpo.geojsonconverter.partitioner;

import java.util.List;
import java.util.Map;

import org.apache.kafka.clients.producer.Partitioner;
import org.apache.kafka.common.Cluster;
import org.apache.kafka.common.PartitionInfo;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.common.utils.Utils;

public class RsuIdPartitioner implements Partitioner {

    @Override
    public void configure(Map<String, ?> configs) {
        // Nothing to configure
     }

    @Override
    public int partition(String topic, Object key, byte[] keyBytes, Object value, byte[] valueBytes, Cluster cluster) {
        
        List<PartitionInfo> partitions = cluster.availablePartitionsForTopic(topic);
        int numPartitions = partitions.size();

        // If the key does not have an RSU ID, partition on the hashed key as normal.
        byte[] partitionBytes = keyBytes;
        
        // If the kay is an object with an RSU ID, partition on it
        if (key instanceof RsuIdKey) {
            var rsuIdKey = (RsuIdKey)key;
            String rsuId = rsuIdKey.getRsuId();
            partitionBytes = Serdes.String().serializer().serialize(topic, rsuId); 
        } 

        return Utils.toPositive(Utils.murmur2(partitionBytes)) % numPartitions;
    }

    @Override
    public void close() {
        // Nothing to close  
    }
    
}
