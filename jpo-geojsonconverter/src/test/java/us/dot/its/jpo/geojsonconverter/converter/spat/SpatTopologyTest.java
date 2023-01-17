package us.dot.its.jpo.geojsonconverter.converter.spat;

import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.TestInputTopic;
import org.apache.kafka.streams.TestOutputTopic;
import org.apache.kafka.streams.Topology;
import org.apache.kafka.streams.TopologyTestDriver;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import us.dot.its.jpo.geojsonconverter.partitioner.RsuIntersectionKey;
import us.dot.its.jpo.geojsonconverter.pojos.spat.ProcessedSpat;
import us.dot.its.jpo.geojsonconverter.serialization.JsonSerdes;
import us.dot.its.jpo.geojsonconverter.validator.SpatJsonValidator;

@SpringBootTest
@RunWith(SpringRunner.class)
public class SpatTopologyTest {
    String kafkaTopicMapGeoJson = "topic.MapGeoJson";
    String mapGeoJsonString = "{\"type\":\"FeatureCollection\",\"features\":[{\"type\":\"Feature\",\"id\":2,\"geometry\":{\"type\":\"LineString\",\"coordinates\":[[-105.09115232580189,39.59533762007272],[-105.08992396633637,39.595323130058226],[-105.08960055408492,39.595333210068304],[-105.08888318379331,39.595317010052106],[-105.0881081157142,39.59531593005103],[-105.08766381810617,39.5953153000504]]},\"properties\":{\"lane_id\":2,\"ip\":\"172.19.0.1\",\"ode_received_at\":\"2022-10-21T17:06:13.446432Z\",\"egress_approach\":0,\"ingress_approach\":1,\"ingress_path\":true,\"egress_path\":false,\"connected_lanes\":[19]}},{\"type\":\"Feature\",\"id\":3,\"geometry\":{\"type\":\"LineString\",\"coordinates\":[[-105.09115267619406,39.59531125004634],[-105.0908101092129,39.59530387003896],[-105.09007907392252,39.59529622003131],[-105.08968009384965,39.595287400022485],[-105.08887804589544,39.595283890018976],[-105.08856771513874,39.595278940014026],[-105.08807027494363,39.59528497002005],[-105.08766370310285,39.59528497002005]]},\"properties\":{\"lane_id\":3,\"ip\":\"172.19.0.1\",\"ode_received_at\":\"2022-10-21T17:06:13.446432Z\",\"egress_approach\":0,\"ingress_approach\":1,\"ingress_path\":true,\"egress_path\":false,\"connected_lanes\":[18]}}]}";

    String kafkaTopicOdeSpatJson = "topic.OdeSpatJson";
    String kafkaTopicProcessedSpat = "topic.ProcessedSpat";
    String odeSpatJsonString = "{\"metadata\":{\"logFileName\":\"\",\"recordType\":\"spatTx\",\"securityResultCode\":\"success\",\"receivedMessageDetails\":{\"rxSource\":\"NA\"},\"payloadType\":\"us.dot.its.jpo.ode.model.OdeSpatPayload\",\"serialId\":{\"streamId\":\"9606fc7c-ca74-48e2-a2bb-bffb6edc718b\",\"bundleSize\":1,\"bundleId\":0,\"recordId\":0,\"serialNumber\":0},\"odeReceivedAt\":\"2022-12-19T19:24:08.843894Z\",\"schemaVersion\":6,\"maxDurationTime\":0,\"recordGeneratedAt\":\"\",\"sanitized\":false,\"odePacketID\":\"\",\"odeTimStartDateTime\":\"\",\"spatSource\":\"V2X\",\"originIp\":\"10.11.81.26\",\"isCertPresent\":false},\"payload\":{\"data\":{\"intersectionStateList\":{\"intersectionStatelist\":[{\"id\":{\"id\":12103},\"revision\":0,\"status\":{\"failureFlash\":false,\"noValidSPATisAvailableAtThisTime\":false,\"fixedTimeOperation\":false,\"standbyOperation\":false,\"trafficDependentOperation\":false,\"manualControlIsEnabled\":false,\"off\":false,\"stopTimeIsActivated\":false,\"recentChangeInMAPassignedLanesIDsUsed\":false,\"recentMAPmessageUpdate\":false,\"failureMode\":false,\"noValidMAPisAvailableAtThisTime\":false,\"signalPriorityIsActive\":false,\"preemptIsActive\":false},\"timeStamp\":8687,\"states\":{\"movementList\":[{\"signalGroup\":1,\"state_time_speed\":{\"movementEventList\":[{\"eventState\":\"STOP_AND_REMAIN\",\"timing\":{\"minEndTime\":14497,\"maxEndTime\":14497}}]}},{\"signalGroup\":2,\"state_time_speed\":{\"movementEventList\":[{\"eventState\":\"STOP_AND_REMAIN\",\"timing\":{\"minEndTime\":14957,\"maxEndTime\":14957}}]}},{\"signalGroup\":3,\"state_time_speed\":{\"movementEventList\":[{\"eventState\":\"STOP_AND_REMAIN\",\"timing\":{\"minEndTime\":14486,\"maxEndTime\":14486}}]}},{\"signalGroup\":4,\"state_time_speed\":{\"movementEventList\":[{\"eventState\":\"STOP_AND_REMAIN\",\"timing\":{\"minEndTime\":14737,\"maxEndTime\":14737}}]}},{\"signalGroup\":5,\"state_time_speed\":{\"movementEventList\":[{\"eventState\":\"STOP_AND_REMAIN\",\"timing\":{\"minEndTime\":14957,\"maxEndTime\":14957}}]}},{\"signalGroup\":6,\"state_time_speed\":{\"movementEventList\":[{\"eventState\":\"PROTECTED_MOVEMENT_ALLOWED\",\"timing\":{\"minEndTime\":14556,\"maxEndTime\":14556}}]}},{\"signalGroup\":7,\"state_time_speed\":{\"movementEventList\":[{\"eventState\":\"STOP_AND_REMAIN\",\"timing\":{\"minEndTime\":14617,\"maxEndTime\":14617}}]}},{\"signalGroup\":8,\"state_time_speed\":{\"movementEventList\":[{\"eventState\":\"STOP_AND_REMAIN\",\"timing\":{\"minEndTime\":14486,\"maxEndTime\":14486}}]}}]}}]}},\"dataType\":\"us.dot.its.jpo.ode.plugin.j2735.J2735SPAT\"}}";

    @Autowired
    SpatJsonValidator spatJsonValidator;

    @Test
    public void testTopology() {
        Topology topology = SpatTopology.build(kafkaTopicOdeSpatJson, kafkaTopicProcessedSpat, spatJsonValidator);
        try (TopologyTestDriver driver = new TopologyTestDriver(topology)) {
            TestInputTopic<Void, String> inputOdeSpatJsonTopic = driver.createInputTopic(
                kafkaTopicOdeSpatJson, 
                Serdes.Void().serializer(), 
                Serdes.String().serializer());
            TestOutputTopic<RsuIntersectionKey, ProcessedSpat> outputTopic = driver.createOutputTopic(
                kafkaTopicProcessedSpat, 
                JsonSerdes.RsuIntersectionKey().deserializer(), 
                JsonSerdes.ProcessedSpat().deserializer());
            
            // Send serialized OdeSpatJson to OdeSpatJson topic
            inputOdeSpatJsonTopic.pipeInput(odeSpatJsonString);

            // Check SpatGeoJson topic for properly converted message data
            List<KeyValue<RsuIntersectionKey, ProcessedSpat>> processedSpatJsonResults = outputTopic.readKeyValuesToList();
            assertEquals(processedSpatJsonResults.size(), 1);

            KeyValue<RsuIntersectionKey, ProcessedSpat> processedSpatJson = processedSpatJsonResults.get(0);
            assertNotNull(processedSpatJson.key);
            assertEquals("10.11.81.26", processedSpatJson.key.getRsuId());
            assertEquals(12103, processedSpatJson.key.getIntersectionId());
            assertNotNull(processedSpatJson.value);
            assertEquals(8, processedSpatJson.value.getStates().size());
        }
        
    }
}
