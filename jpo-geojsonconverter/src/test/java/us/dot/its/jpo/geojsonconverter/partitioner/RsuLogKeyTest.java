package us.dot.its.jpo.geojsonconverter.partitioner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

import org.junit.Test;

public class RsuLogKeyTest {
    final static String ipAddress = "127.0.0.1";
    final static String logFileName = "bsmLogDuringEvent_commsignia.gz";
    
    @Test
    public void testEquality() {        
        
        var key = new RsuLogKey();
        key.setRsuId(ipAddress);
        key.setLogId(logFileName);

        var keyValue = new RsuLogKey(ipAddress, logFileName);
        var keyRef = key;
        Object otherObject = new Object();
        var otherValue1 = new RsuLogKey(ipAddress, null);
        var otherValue2 = new RsuLogKey("0.0.0.0", "");
        var otherValue3 = new RsuLogKey(ipAddress, "bsmTx.gz");

        assertTrue("Value equality", key.equals(keyValue));
        assertFalse("Value inequality branch 1", key.equals(otherValue1));
        assertFalse("Value inequality branch 2", key.equals(otherValue2));
        assertFalse("Value inequality branch 3", key.equals(otherValue3));
        assertTrue("Reference equality", key.equals(keyRef));
        assertFalse("Reference inequality", key.equals(otherObject));
        assertEquals("Hash code values equal", key.hashCode(), keyValue.hashCode());

        // Getter coverage
        assertEquals("getRsuId", key.getRsuId(), keyValue.getRsuId());
        assertEquals("getLogId", key.getLogId(), keyValue.getLogId());
    }

    @Test
    public void testToString() {
        var key = new RsuLogKey();
        key.setRsuId(ipAddress);
        key.setLogId(logFileName);

        String str = key.toString();
        assertThat(str, containsString(ipAddress));
        assertThat(str, containsString(logFileName));
    }
}
