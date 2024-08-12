package us.dot.its.jpo.geojsonconverter.partitioner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import org.junit.Test;

public class GenericPartitionerTest {
    @Test
    public void testPartition_RsuLogKey() {

        // Test that the custom partition function still behaves normally if the key is a RsuLogKey
        
        final String topic = "topic";
        RsuLogKey key = new RsuLogKey("127.0.0.1", null, "ABCDEFG");
        RsuLogKey sameKey = new RsuLogKey("127.0.0.1", null, "ABCDEFG");
        RsuLogKey differentKey = new RsuLogKey(null, "bsmTx.gz", "ABCDEFG");
        final Object obj = new Object();
        final int numPartitions = Integer.MAX_VALUE;

        var partitioner = new GenericPartitioner<RsuLogKey, Object>();

        int partitionKey = partitioner.partition(topic, key, obj, numPartitions);
        int partitionSame = partitioner.partition(topic, sameKey, obj, numPartitions);
        int partitionDifferent = partitioner.partition(topic, differentKey, obj, numPartitions);

        assertEquals("Same keys", partitionKey, partitionSame);
        assertNotEquals("Different keys", partitionKey, partitionDifferent);
    }

    @Test
    public void testPartition_String() {

        // Test that the custom partition function still behaves normally if the key is a string

        final String topic = "topic";
        final String key = "AAA";
        final String sameKey = "AAA";
        final String differentKey = "BBB";
        final Object obj = new Object();
        final int numPartitions = Integer.MAX_VALUE;

        var partitioner = new GenericPartitioner<String, Object>();

        int partitionKey = partitioner.partition(topic, key, obj, numPartitions);
        int partitionSame = partitioner.partition(topic, sameKey, obj, numPartitions);
        int partitionDifferent = partitioner.partition(topic, differentKey, obj, numPartitions);

        assertEquals("Same keys", partitionKey, partitionSame);
        assertNotEquals("Different keys", partitionKey, partitionDifferent);
    }
}
