package us.dot.its.jpo.geojsonconverter;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class SystemConfigTest {

    @Test
    public void testDoConfig() {

        SystemConfig testSystemConfig = new SystemConfig(14, "testSchemaName");

        testSystemConfig.doConfig();
    }

    @Test
    public void testSettersAndGetters() {

        String testSchemaName = "testSchemaName12356";
        int testThreadCount = 5;

        SystemConfig testSystemConfig = new SystemConfig(123, "originalSchemaName");

        testSystemConfig.setSchemaName(testSchemaName);
        testSystemConfig.setThreadCount(testThreadCount);

        assertEquals("Incorrect schemaName", testSchemaName, testSystemConfig.getSchemaName());
        assertEquals("Incorrect threadCount", testThreadCount, testSystemConfig.getThreadCount());
    }
}
