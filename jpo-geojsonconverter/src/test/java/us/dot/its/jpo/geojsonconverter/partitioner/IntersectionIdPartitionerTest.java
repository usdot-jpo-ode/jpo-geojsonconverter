package us.dot.its.jpo.geojsonconverter.partitioner;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;


public class IntersectionIdPartitionerTest {

    @Test
    public void testPartition_IntersectionKey() {

        final String topic = "topic";
        final String ip1 = "1.1.1.1";
        final String ip2 = "2.2.2.2";
        final int intersection1 = 100000;
        final int intersection2 = 111111;
        final int region1 = 10;
        final int region2 = 22;

        var key111 = new RsuIntersectionKey(ip1, intersection1, region1);
        var key111_same = new RsuIntersectionKey(ip1, intersection1, region1);
        var key112 = new RsuIntersectionKey(ip1, intersection1, region2);
        var key121 = new RsuIntersectionKey(ip1, intersection2, region1);
        var key122 = new RsuIntersectionKey(ip1, intersection2, region2);
        var key211 = new RsuIntersectionKey(ip2, intersection1, region1);
        var key212 = new RsuIntersectionKey(ip2, intersection1, region2);
        var key221 = new RsuIntersectionKey(ip2, intersection2, region1);
        var key222 = new RsuIntersectionKey(ip2, intersection2, region2);

        final Object value = new Object();

        // Minimize possibility of collisions
        final int numPartitions = Integer.MAX_VALUE;

        var partitioner = new IntersectionIdPartitioner<RsuIntersectionKey, Object>();

        int partition111 = partitioner.partition(topic, key111, value, numPartitions);
        int partition111_same = partitioner.partition(topic, key111_same, value, numPartitions);
        int partition112 = partitioner.partition(topic, key112, value, numPartitions);
        int partition121 = partitioner.partition(topic, key121, value, numPartitions);
        int partition122 = partitioner.partition(topic, key122, value, numPartitions);
        int partition211 = partitioner.partition(topic, key211, value, numPartitions);
        int partition212 = partitioner.partition(topic, key212, value, numPartitions);
        int partition221 = partitioner.partition(topic, key221, value, numPartitions);
        int partition222 = partitioner.partition(topic, key222, value, numPartitions);

        final String equalMsg = "Keys with the same intersectionID should have the same partition, regardless of rsuIP and region.";
        assertEquals(equalMsg, partition111, partition111_same);
        assertEquals(equalMsg, partition111, partition112);
        assertEquals(equalMsg, partition111, partition211);
        assertEquals(equalMsg, partition111, partition212);

        final String notEqualMsg = "Keys with different intersectionIDs are unlikely to have the same partition number";
        assertNotEquals(notEqualMsg, partition111, partition121);
        assertNotEquals(notEqualMsg, partition111, partition122);
        assertNotEquals(notEqualMsg, partition111, partition221);
        assertNotEquals(notEqualMsg, partition111, partition222);

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

        var partitioner = new IntersectionIdPartitioner<String, Object>();

        int partitionKey = partitioner.partition(topic, key, obj, numPartitions);
        int partitionSame = partitioner.partition(topic, sameKey, obj, numPartitions);
        int partitionDifferent = partitioner.partition(topic, differentKey, obj, numPartitions);

        assertEquals("Same keys", partitionKey, partitionSame);
        assertNotEquals("Different keys", partitionKey, partitionDifferent);
    }
}
