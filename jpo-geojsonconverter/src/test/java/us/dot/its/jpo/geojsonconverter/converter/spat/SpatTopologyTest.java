package us.dot.its.jpo.geojsonconverter.converter.spat;

import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.TestInputTopic;
import org.apache.kafka.streams.TestOutputTopic;
import org.apache.kafka.streams.Topology;
import org.apache.kafka.streams.TopologyTestDriver;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import us.dot.its.jpo.geojsonconverter.partitioner.RsuIntersectionKey;
import us.dot.its.jpo.geojsonconverter.pojos.spat.ProcessedSpat;
import us.dot.its.jpo.geojsonconverter.serialization.JsonSerdes;
import us.dot.its.jpo.geojsonconverter.validator.SpatJsonValidator;

@SpringBootTest
@RunWith(SpringRunner.class)
@ActiveProfiles("test")
public class SpatTopologyTest {
    private String kafkaTopicOdeSpatJson = "topic.OdeSpatJson";
    private String kafkaTopicProcessedSpat = "topic.ProcessedSpat";
    private String odeSpatJsonString;

    @Autowired
    SpatJsonValidator spatJsonValidator;

    @Before
    public void setup() throws IOException {
        odeSpatJsonString = new String(Files.readAllBytes(Paths.get("src/test/resources/json/valid.spat.json")));
    }

    @Test
    public void testTopology() {
        Topology topology = SpatTopology.build(kafkaTopicOdeSpatJson, kafkaTopicProcessedSpat, spatJsonValidator);
        try (TopologyTestDriver driver = new TopologyTestDriver(topology)) {
            TestInputTopic<Void, String> inputOdeSpatJsonTopic = driver.createInputTopic(kafkaTopicOdeSpatJson,
                    Serdes.Void().serializer(), Serdes.String().serializer());
            TestOutputTopic<RsuIntersectionKey, ProcessedSpat> outputTopic =
                    driver.createOutputTopic(kafkaTopicProcessedSpat, JsonSerdes.RsuIntersectionKey().deserializer(),
                            JsonSerdes.ProcessedSpat().deserializer());

            // Send serialized OdeSpatJson to OdeSpatJson topic
            inputOdeSpatJsonTopic.pipeInput(odeSpatJsonString);

            // Check SpatGeoJson topic for properly converted message data
            List<KeyValue<RsuIntersectionKey, ProcessedSpat>> processedSpatJsonResults =
                    outputTopic.readKeyValuesToList();
            assertEquals(processedSpatJsonResults.size(), 1);

            KeyValue<RsuIntersectionKey, ProcessedSpat> processedSpatJson = processedSpatJsonResults.get(0);
            assertNotNull(processedSpatJson.key);
            assertEquals("172.18.0.1", processedSpatJson.key.getRsuId());
            assertEquals(8804, processedSpatJson.key.getIntersectionId());
            assertNotNull(processedSpatJson.value);
            assertEquals(false, processedSpatJson.value.isCti4501Conformant());
            assertEquals(4, processedSpatJson.value.getValidationMessages().size());
            assertEquals(8, processedSpatJson.value.getStates().size());
        }
    }

    @Test
    public void testTopologyFailure() {
        Topology topology = SpatTopology.build(kafkaTopicOdeSpatJson, kafkaTopicProcessedSpat, spatJsonValidator);
        try (TopologyTestDriver driver = new TopologyTestDriver(topology)) {
            TestInputTopic<Void, String> inputOdeSpatJsonTopic = driver.createInputTopic(kafkaTopicOdeSpatJson,
                    Serdes.Void().serializer(), Serdes.String().serializer());
            TestOutputTopic<RsuIntersectionKey, ProcessedSpat> outputTopic =
                    driver.createOutputTopic(kafkaTopicProcessedSpat, JsonSerdes.RsuIntersectionKey().deserializer(),
                            JsonSerdes.ProcessedSpat().deserializer());

            // Send serialized OdeSpatJson to OdeSpatJson topic
            inputOdeSpatJsonTopic.pipeInput("{");

            // Check SpatGeoJson topic for properly converted message data
            List<KeyValue<RsuIntersectionKey, ProcessedSpat>> processedSpatJsonResults =
                    outputTopic.readKeyValuesToList();
            assertEquals(processedSpatJsonResults.size(), 1);

            KeyValue<RsuIntersectionKey, ProcessedSpat> processedSpatJson = processedSpatJsonResults.get(0);
            assertNotNull(processedSpatJson.key);
            assertEquals("ERROR", processedSpatJson.key.getRsuId());
            assertEquals(0, processedSpatJson.key.getIntersectionId());
            assertNotNull(processedSpatJson.value);
            assertEquals(1, processedSpatJson.value.getValidationMessages().size());
        }

    }
}
