package us.dot.its.jpo.geojsonconverter;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.hamcrest.Matchers.*;
import static org.hamcrest.MatcherAssert.assertThat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.context.junit4.SpringRunner;

import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

// excluded from sonar testing
@SpringBootTest
@RunWith(SpringRunner.class)
@EmbeddedKafka(partitions = 1, brokerProperties = { "listeners=PLAINTEXT://localhost:9092", "port=9092"})
@DirtiesContext
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
