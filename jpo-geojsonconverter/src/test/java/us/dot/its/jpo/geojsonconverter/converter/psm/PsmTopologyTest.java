package us.dot.its.jpo.geojsonconverter.converter.psm;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

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

import us.dot.its.jpo.geojsonconverter.partitioner.RsuLogKey;
import us.dot.its.jpo.geojsonconverter.partitioner.RsuTypeIdKey;
import us.dot.its.jpo.geojsonconverter.pojos.geojson.Point;
import us.dot.its.jpo.geojsonconverter.pojos.geojson.psm.ProcessedPsm;
import us.dot.its.jpo.geojsonconverter.serialization.JsonSerdes;
import us.dot.its.jpo.geojsonconverter.validator.PsmJsonValidator;
import us.dot.its.jpo.ode.plugin.j2735.J2735PersonalDeviceUserType;
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
            TestOutputTopic<RsuTypeIdKey, ProcessedPsm<Point>> outputTopic =
                    driver.createOutputTopic(kafkaTopicProcessedPsm, JsonSerdes.RsuTypeIdKey().deserializer(),
                            JsonSerdes.ProcessedPsm().deserializer());

            // Send serialized OdePsmJson to OdePsmJson topic
            inputTopic.pipeInput(odePsmJsonString);

            List<KeyValue<RsuTypeIdKey, ProcessedPsm<Point>>> processedPsmJsonResults =
                    outputTopic.readKeyValuesToList();
            assertEquals(processedPsmJsonResults.size(), 1);

            KeyValue<RsuTypeIdKey, ProcessedPsm<Point>> processedPsmJson = processedPsmJsonResults.get(0);
            assertNotNull(processedPsmJson.key);

            RsuTypeIdKey expectedKey = RsuTypeIdKey.builder().rsuId("172.23.0.1")
                    .pedestrianType(J2735PersonalDeviceUserType.aPEDESTRIAN).psmId("24779D7E").build();
            assertEquals(expectedKey, processedPsmJson.key);

            assertNotNull(processedPsmJson.value);
            assertEquals("24779D7E", processedPsmJson.value.getFeatures()[0].getProperties().getId());
        }
    }

    @Test
    public void testTopologyFailure() {
        Topology topology = PsmTopology.build(kafkaTopicOdePsmJson, kafkaTopicProcessedPsm, psmJsonValidator);
        try (TopologyTestDriver driver = new TopologyTestDriver(topology)) {
            TestInputTopic<Void, String> inputTopic = driver.createInputTopic(kafkaTopicOdePsmJson,
                    Serdes.Void().serializer(), Serdes.String().serializer());
            TestOutputTopic<RsuTypeIdKey, ProcessedPsm<Point>> outputTopic =
                    driver.createOutputTopic(kafkaTopicProcessedPsm, JsonSerdes.RsuTypeIdKey().deserializer(),
                            JsonSerdes.ProcessedPsm().deserializer());

            // Send invalid JSON to trigger failure case
            inputTopic.pipeInput("{");

            List<KeyValue<RsuTypeIdKey, ProcessedPsm<Point>>> processedPsmJsonResults =
                    outputTopic.readKeyValuesToList();
            assertEquals(processedPsmJsonResults.size(), 1);

            KeyValue<RsuTypeIdKey, ProcessedPsm<Point>> processedPsmJson = processedPsmJsonResults.get(0);
            assertNotNull(processedPsmJson.key);

            RsuTypeIdKey expectedKey = RsuTypeIdKey.builder().rsuId("null").pedestrianType(null).psmId("ERROR").build();
            assertEquals(expectedKey.getPedestrianType(), processedPsmJson.key.getPedestrianType());
            assertEquals(expectedKey.getPsmId(), processedPsmJson.key.getPsmId());
        }
    }
}

