package us.dot.its.jpo.geojsonconverter;

import org.junit.Test;
import org.junit.runner.RunWith;

import static org.hamcrest.Matchers.*;
import static org.hamcrest.MatcherAssert.assertThat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest
@RunWith(SpringRunner.class)
public class KafkaConfigurationTest {

    @Autowired
    private KafkaConfiguration kafkaConfig;

    @Test
    public void testKafkaConfigurationInjected() {
        assertThat(kafkaConfig, notNullValue());
    }

    @Test
    public void testConfigurationsLoaded() {
        assertThat(kafkaConfig.getNumPartitions(), greaterThan(0));
        assertThat(kafkaConfig.getNumReplicas(), greaterThan(0));
        assertThat(kafkaConfig.getCreateTopics(), notNullValue());
    }
    
}