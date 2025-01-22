package us.dot.its.jpo.geojsonconverter.converter.psm;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

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

import us.dot.its.jpo.geojsonconverter.partitioner.RsuPsmIdKey;
import us.dot.its.jpo.geojsonconverter.pojos.geojson.Point;
import us.dot.its.jpo.geojsonconverter.pojos.geojson.psm.ProcessedPsm;
import us.dot.its.jpo.geojsonconverter.serialization.JsonSerdes;
import us.dot.its.jpo.geojsonconverter.validator.PsmJsonValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@RunWith(SpringRunner.class)
@ActiveProfiles("test")
public class PsmTopologyTest {
    String kafkaTopicOdePsmJson = "topic.OdePsmJson";
    String kafkaTopicProcessedPsm = "topic.ProcessedPsm";

    private String odePsmJsonString;

    @Autowired
    PsmJsonValidator psmJsonValidator;

    @Before
    public void setup() throws IOException {
        odePsmJsonString = new String(Files.readAllBytes(Paths.get("src/test/resources/json/valid.psm.json")));
    }

    @Test
    public void testTopology() {
        Topology topology = PsmTopology.build(kafkaTopicOdePsmJson, kafkaTopicProcessedPsm, psmJsonValidator);
        try (TopologyTestDriver driver = new TopologyTestDriver(topology)) {
            TestInputTopic<Void, String> inputTopic = driver.createInputTopic(kafkaTopicOdePsmJson,
                    Serdes.Void().serializer(), Serdes.String().serializer());
            TestOutputTopic<RsuPsmIdKey, ProcessedPsm<Point>> outputTopic =
                    driver.createOutputTopic(kafkaTopicProcessedPsm, JsonSerdes.RsuTypeIdKey().deserializer(),
                            JsonSerdes.ProcessedPsm().deserializer());

            // Send serialized OdePsmJson to OdePsmJson topic
            inputTopic.pipeInput(odePsmJsonString);

            List<KeyValue<RsuPsmIdKey, ProcessedPsm<Point>>> processedPsmJsonResults =
                    outputTopic.readKeyValuesToList();
            assertEquals(processedPsmJsonResults.size(), 1);

            KeyValue<RsuPsmIdKey, ProcessedPsm<Point>> processedPsmJson = processedPsmJsonResults.get(0);
            assertNotNull(processedPsmJson.key);

            RsuPsmIdKey expectedKey = RsuPsmIdKey.builder().rsuId("172.23.0.1").psmId("24779D7E").build();
            assertEquals(expectedKey, processedPsmJson.key);

            assertNotNull(processedPsmJson.value);
            assertEquals("24779D7E", processedPsmJson.value.getProperties().getId());
        }
    }

    @Test
    public void testTopologyFailure() {
        Topology topology = PsmTopology.build(kafkaTopicOdePsmJson, kafkaTopicProcessedPsm, psmJsonValidator);
        try (TopologyTestDriver driver = new TopologyTestDriver(topology)) {
            TestInputTopic<Void, String> inputTopic = driver.createInputTopic(kafkaTopicOdePsmJson,
                    Serdes.Void().serializer(), Serdes.String().serializer());
            TestOutputTopic<RsuPsmIdKey, ProcessedPsm<Point>> outputTopic =
                    driver.createOutputTopic(kafkaTopicProcessedPsm, JsonSerdes.RsuTypeIdKey().deserializer(),
                            JsonSerdes.ProcessedPsm().deserializer());

            // Send invalid JSON to trigger failure case
            inputTopic.pipeInput("{");

            List<KeyValue<RsuPsmIdKey, ProcessedPsm<Point>>> processedPsmJsonResults =
                    outputTopic.readKeyValuesToList();
            assertEquals(processedPsmJsonResults.size(), 1);

            KeyValue<RsuPsmIdKey, ProcessedPsm<Point>> processedPsmJson = processedPsmJsonResults.get(0);
            assertNotNull(processedPsmJson.key);

            RsuPsmIdKey expectedKey = RsuPsmIdKey.builder().rsuId("null").psmId("ERROR").build();
            assertEquals(expectedKey.getPsmId(), processedPsmJson.key.getPsmId());
        }
    }
}

