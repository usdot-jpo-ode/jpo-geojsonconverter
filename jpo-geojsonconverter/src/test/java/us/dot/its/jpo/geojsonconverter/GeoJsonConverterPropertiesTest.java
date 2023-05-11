package us.dot.its.jpo.geojsonconverter;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.Assert.fail;

import java.util.Properties;

import org.junit.Before;
import org.junit.Test;
import org.springframework.boot.info.BuildProperties;

import us.dot.its.jpo.ode.util.CommonUtils;


public class GeoJsonConverterPropertiesTest {

    
    GeoJsonConverterProperties testGeoJsonConverterProperties;
    BuildProperties mockBuildProperties;
    CommonUtils capturingCommonUtils;

    @Before
    public void setup() {
        testGeoJsonConverterProperties = new GeoJsonConverterProperties();
        testGeoJsonConverterProperties.initialize();
    }

    @Test
    public void testInit() {
        try {
                new GeoJsonConverterProperties();
        } catch (Exception e) {
                fail("Unexpected exception: " + e);
        }
    }

    @Test
    public void testInitShouldCatchUnknownHostException() {
        String expectedBroker = "localhost:9092";
        assertEquals("Incorrect KafkaBrokers", expectedBroker, testGeoJsonConverterProperties.getKafkaBrokers());
    }

    @Test
    public void testKafkaBrokersSetterAndGetter() {
        String testKafkaBrokers = "testKafkaBrokers";
        testGeoJsonConverterProperties.setKafkaBrokers(testKafkaBrokers);
        assertEquals("Incorrect KafkaBrokers", testKafkaBrokers, testGeoJsonConverterProperties.getKafkaBrokers());
    }

    @Test
    public void testEnvSetterAndGetter() {
        testGeoJsonConverterProperties.setEnv(null);
        assertNull(testGeoJsonConverterProperties.getEnv());
    }

    @Test
    public void testKafkaTopicOdeSpatJsonSetterAndGetter() {
        String testKafkaTopicOdeSpatJson = "testKafkaTopicOdeSpatJson";
        testGeoJsonConverterProperties.setKafkaTopicOdeSpatJson(testKafkaTopicOdeSpatJson);
        assertEquals("Incorrect KafkaTopicOdeSpatJson", testKafkaTopicOdeSpatJson, testGeoJsonConverterProperties.getKafkaTopicOdeSpatJson());
    }

    @Test
    public void testKafkaTopicSpatGeoJsonSetterAndGetter() {
        String testKafkaTopicSpatGeoJson = "testKafkaTopicSpatGeoJson";
        testGeoJsonConverterProperties.setKafkaTopicSpatGeoJson(testKafkaTopicSpatGeoJson);
        assertEquals("Incorrect KafkaTopicSpatGeoJson", testKafkaTopicSpatGeoJson, testGeoJsonConverterProperties.getKafkaTopicSpatGeoJson());
    }

    @Test
    public void testKafkaTopicOdeMapJsonSetterAndGetter() {
        String testKafkaTopicOdeMapJson = "testKafkaTopicOdeMapJson";
        testGeoJsonConverterProperties.setKafkaTopicOdeMapJson(testKafkaTopicOdeMapJson);
        assertEquals("Incorrect KafkaTopicOdeMapJson", testKafkaTopicOdeMapJson, testGeoJsonConverterProperties.getKafkaTopicOdeMapJson());
    }

    @Test
    public void testKafkaTopicMapGeoJsonSetterAndGetter() {
        String testKafkaTopicMapGeoJson = "testKafkaTopicMapGeoJson";
        testGeoJsonConverterProperties.setKafkaTopicProcessedMap(testKafkaTopicMapGeoJson);
        assertEquals("Incorrect KafkaTopicMapGeoJson", testKafkaTopicMapGeoJson, testGeoJsonConverterProperties.getKafkaTopicProcessedMap());
    }

    @Test
    public void testStreamProperties() {
        Properties streamProps = testGeoJsonConverterProperties.createStreamProperties("test-props");
        assertNotNull(streamProps);
    }
}
