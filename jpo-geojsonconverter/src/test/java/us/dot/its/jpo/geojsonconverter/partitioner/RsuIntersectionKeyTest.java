package us.dot.its.jpo.geojsonconverter.partitioner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;


import org.junit.Test;


public class RsuIntersectionKeyTest {

    final static String ipAddress = "127.0.0.1";
    final static int intersectionId = 10001; 
    
    @Test
    public void testEquality() {
        
        
        var key = new RsuIntersectionKey();
        key.setRsuId(ipAddress);
        key.setIntersectionId(intersectionId);

        var keyValue = new RsuIntersectionKey();
        keyValue.setRsuId(ipAddress);
        keyValue.setIntersectionId(intersectionId);

        var keyRef = key;

        Object obj = new Object();

        var otherValue1 = new RsuIntersectionKey();
        otherValue1.setRsuId(ipAddress);
        otherValue1.setIntersectionId(99);

        var otherValue2 = new RsuIntersectionKey();
        otherValue2.setRsuId("0.0.0.0");
        otherValue2.setIntersectionId(99);

        assertTrue("Value equality", key.equals(keyValue));
        assertFalse("Value inequality branch 1", key.equals(otherValue1));
        assertFalse("Value inequality branch 2", key.equals(otherValue2));
        assertTrue("Reference equality", key.equals(keyRef));
        assertFalse("Reference inequality", key.equals(obj));
        assertEquals("Hash code values equal", key.hashCode(), keyValue.hashCode());

        // Getter coverage
        assertEquals("getRsuId", key.getRsuId(), keyValue.getRsuId());
        assertEquals("getIntersectionId", key.getIntersectionId(), keyValue.getIntersectionId());

    }

    @Test
    public void testToString() {
        var key = new RsuIntersectionKey();
        key.setRsuId(ipAddress);
        key.setIntersectionId(intersectionId);

        String str = key.toString();
        assertThat(str, containsString(ipAddress));
        assertThat(str, containsString(Integer.toString(intersectionId)));
    }
}
