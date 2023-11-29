package us.dot.its.jpo.geojsonconverter.partitioner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import org.junit.Test;

public class RsuIdPartitionerTest {

    
    
    @Test
    public void testPartition_RsuIdKey() {

        final String topic = "topic";

        final String ip1 = "1.1.1.1";
        final String ip2 = "2.2.2.2";

        final int intersection1 = 11111;
        final int intersection2 = 22222;
         
        var key11 = new RsuIntersectionKey(ip1, intersection1);
        var key12 = new RsuIntersectionKey(ip1, intersection2);
        var key21 = new RsuIntersectionKey(ip2, intersection1);
        var key22 = new RsuIntersectionKey(ip2, intersection2);

        final Object obj = new Object();

        // Minimize possibility of collisions
        final int numPartitions = Integer.MAX_VALUE;

        var partitioner = new RsuIdPartitioner<RsuIntersectionKey, Object>();
        
        int partition11 = partitioner.partition(topic, key11, obj, numPartitions);
        int partition12 = partitioner.partition(topic, key12, obj, numPartitions);
        int partition21 = partitioner.partition(topic, key21, obj, numPartitions);
        int partition22 = partitioner.partition(topic, key22, obj, numPartitions);

        String equalMsg =  "Keys with the same RSU ID should have the same partition number";
        assertEquals(equalMsg, partition11, partition12);
        assertEquals(equalMsg, partition21, partition22);

        String notEqualMsg = "Keys with different RSU IDs are unlikely to have the same partition number";
        assertNotEquals(notEqualMsg, partition11, partition21);
        assertNotEquals(notEqualMsg, partition11, partition22);
        assertNotEquals(notEqualMsg, partition12, partition21);
        assertNotEquals(notEqualMsg, partition12, partition22);
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

        var partitioner = new RsuIdPartitioner<String, Object>();

        int partitionKey = partitioner.partition(topic, key, obj, numPartitions);
        int partitionSame = partitioner.partition(topic, sameKey, obj, numPartitions);
        int partitionDifferent = partitioner.partition(topic, differentKey, obj, numPartitions);

        assertEquals("Same keys", partitionKey, partitionSame);
        assertNotEquals("Different keys", partitionKey, partitionDifferent);
    }

    
}
