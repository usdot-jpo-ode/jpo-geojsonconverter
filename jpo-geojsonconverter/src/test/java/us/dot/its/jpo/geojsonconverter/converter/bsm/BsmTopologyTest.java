package us.dot.its.jpo.geojsonconverter.converter.bsm;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.List;

import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.TestInputTopic;
import org.apache.kafka.streams.TestOutputTopic;
import org.apache.kafka.streams.Topology;
import org.apache.kafka.streams.TopologyTestDriver;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import us.dot.its.jpo.geojsonconverter.partitioner.RsuLogKey;
import us.dot.its.jpo.geojsonconverter.pojos.geojson.Point;
import us.dot.its.jpo.geojsonconverter.pojos.geojson.bsm.ProcessedBsm;
import us.dot.its.jpo.geojsonconverter.serialization.JsonSerdes;
import us.dot.its.jpo.geojsonconverter.validator.BsmJsonValidator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@RunWith(SpringRunner.class)
@ActiveProfiles("test")
public class BsmTopologyTest {
    String kafkaTopicOdeBsmJson = "topic.OdeBsmJson";
    String kafkaTopicProcessedBsm = "topic.ProcessedBsm";
    String odeBsmJsonString = "{\"metadata\":{\"bsmSource\":\"EV\",\"logFileName\":\"\",\"recordType\":\"bsmTx\",\"securityResultCode\":\"success\",\"receivedMessageDetails\":{\"locationData\":{\"latitude\":\"unavailable\",\"longitude\":\"unavailable\",\"elevation\":\"unavailable\",\"speed\":\"unavailable\",\"heading\":\"unavailable\"},\"rxSource\":\"RSU\"},\"payloadType\":\"us.dot.its.jpo.ode.model.OdeBsmPayload\",\"serialId\":{\"streamId\":\"d3a49df1-983a-4ab9-ab61-dcac5dacaed7\",\"bundleSize\":1,\"bundleId\":0,\"recordId\":0,\"serialNumber\":0},\"odeReceivedAt\":\"2024-08-12T12:32:03.811Z\",\"schemaVersion\":6,\"maxDurationTime\":0,\"recordGeneratedAt\":\"\",\"recordGeneratedBy\":\"OBU\",\"sanitized\":false,\"odePacketID\":\"\",\"odeTimStartDateTime\":\"\",\"originIp\":\"10.11.81.26\"},\"payload\":{\"data\":{\"coreData\":{\"msgCnt\":25,\"id\":\"12A7A951\",\"secMark\":2800,\"position\":{\"latitude\":40.5671913,\"longitude\":-105.0342901,\"elevation\":1505.9},\"accelSet\":{\"accelLat\":2001,\"accelLong\":0.00,\"accelVert\":-127,\"accelYaw\":0.00},\"accuracy\":{\"semiMajor\":5.00,\"semiMinor\":2.00,\"orientation\":0E-10},\"transmission\":\"UNAVAILABLE\",\"speed\":0.00,\"heading\":359.4000,\"brakes\":{\"wheelBrakes\":{\"leftFront\":false,\"rightFront\":false,\"unavailable\":true,\"leftRear\":false,\"rightRear\":false},\"traction\":\"unavailable\",\"abs\":\"unavailable\",\"scs\":\"unavailable\",\"brakeBoost\":\"unavailable\",\"auxBrakes\":\"unavailable\"},\"size\":{\"width\":208,\"length\":586}},\"partII\":[]},\"dataType\":\"us.dot.its.jpo.ode.plugin.j2735.J2735Bsm\"}}";

    @Autowired
    BsmJsonValidator bsmJsonValidator;

    @Test
    public void testTopology() {
        Topology topology = BsmTopology.build(kafkaTopicOdeBsmJson, kafkaTopicProcessedBsm, bsmJsonValidator);
        try (TopologyTestDriver driver = new TopologyTestDriver(topology)) {
            TestInputTopic<Void, String> inputTopic = driver.createInputTopic(
                kafkaTopicOdeBsmJson, 
                Serdes.Void().serializer(), 
                Serdes.String().serializer());
            TestOutputTopic<RsuLogKey, ProcessedBsm<Point>> outputTopic = driver.createOutputTopic(
                kafkaTopicProcessedBsm, 
                JsonSerdes.RsuLogKey().deserializer(), 
                JsonSerdes.ProcessedBsm().deserializer());
            
            // Send serialized OdeBsmJson to OdeBsmJson topic
            inputTopic.pipeInput(odeBsmJsonString);

            // Check ProcessedBsm topic for properly converted message data
            List<KeyValue<RsuLogKey, ProcessedBsm<Point>>> processedBsmJsonResults = outputTopic.readKeyValuesToList();
            assertEquals(processedBsmJsonResults.size(), 1);

            KeyValue<RsuLogKey, ProcessedBsm<Point>> processedBsmJson = processedBsmJsonResults.get(0);
            assertNotNull(processedBsmJson.key);
            assertEquals(new RsuLogKey("10.11.81.26", null), processedBsmJson.key);
            assertNotNull(processedBsmJson.value);
            assertEquals("12A7A951", processedBsmJson.value.getFeatures()[0].getProperties().getId());
        }
        
    }

    @Test
    public void testTopologyFailure() {
        Topology topology = BsmTopology.build(kafkaTopicOdeBsmJson, kafkaTopicProcessedBsm, bsmJsonValidator);
        try (TopologyTestDriver driver = new TopologyTestDriver(topology)) {
            TestInputTopic<Void, String> inputTopic = driver.createInputTopic(
                kafkaTopicOdeBsmJson, 
                Serdes.Void().serializer(), 
                Serdes.String().serializer());
            TestOutputTopic<RsuLogKey, ProcessedBsm<Point>> outputTopic = driver.createOutputTopic(
                kafkaTopicProcessedBsm, 
                JsonSerdes.RsuLogKey().deserializer(), 
                JsonSerdes.ProcessedBsm().deserializer());
            
            // Send serialized OdeSpatJson to OdeSpatJson topic
            inputTopic.pipeInput("{");

            // Check ProcessedBsm topic for properly converted message data
            List<KeyValue<RsuLogKey, ProcessedBsm<Point>>> processedBsmJsonResults = outputTopic.readKeyValuesToList();
            assertEquals(processedBsmJsonResults.size(), 1);

            KeyValue<RsuLogKey, ProcessedBsm<Point>> processedBsmJson = processedBsmJsonResults.get(0);
            assertNotNull(processedBsmJson.key);
            assertEquals(new RsuLogKey("ERROR", null), processedBsmJson.key);
        }
        
    }
}
