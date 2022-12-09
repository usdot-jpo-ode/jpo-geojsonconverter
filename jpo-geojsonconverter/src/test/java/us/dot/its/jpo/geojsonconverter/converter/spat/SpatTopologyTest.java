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
    String odeSpatJsonString = "{\"metadata\":{\"logFileName\":\"\",\"recordType\":\"spatTx\",\"securityResultCode\":\"success\",\"receivedMessageDetails\":{\"locationData\":null,\"rxSource\":\"NA\"},\"encodings\":null,\"payloadType\":\"us.dot.its.jpo.ode.model.OdeSpatPayload\",\"serialId\":{\"streamId\":\"7b68f216-4a47-4e7e-9376-59c1f8832a8e\",\"bundleSize\":1,\"bundleId\":0,\"recordId\":0,\"serialNumber\":0},\"odeReceivedAt\":\"2022-10-21T17:39:53.700315Z\",\"schemaVersion\":6,\"maxDurationTime\":0,\"recordGeneratedAt\":\"\",\"recordGeneratedBy\":null,\"sanitized\":false,\"odePacketID\":\"\",\"odeTimStartDateTime\":\"\",\"spatSource\":\"V2X\",\"originIp\":\"172.19.0.1\",\"isCertPresent\":false},\"payload\":{\"data\":{\"timeStamp\":null,\"name\":null,\"intersectionStateList\":{\"intersectionStatelist\":[{\"name\":null,\"id\":{\"region\":null,\"id\":12110},\"revision\":0,\"status\":\"MANUALCONTROLISENABLED\",\"moy\":null,\"timeStamp\":28744,\"enabledLanes\":null,\"states\":{\"movementList\":[{\"movementName\":null,\"signalGroup\":2,\"state_time_speed\":{\"movementEventList\":[{\"eventState\":\"STOP_AND_REMAIN\",\"timing\":{\"startTime\":null,\"minEndTime\":1851,\"maxEndTime\":1851,\"likelyTime\":null,\"confidence\":null,\"nextTime\":null},\"speeds\":null}]},\"maneuverAssistList\":null},{\"movementName\":null,\"signalGroup\":3,\"state_time_speed\":{\"movementEventList\":[{\"eventState\":\"STOP_AND_REMAIN\",\"timing\":{\"startTime\":null,\"minEndTime\":2281,\"maxEndTime\":2281,\"likelyTime\":null,\"confidence\":null,\"nextTime\":null},\"speeds\":null}]},\"maneuverAssistList\":null}]},\"maneuverAssistList\":null}]}},\"dataType\":\"us.dot.its.jpo.ode.plugin.j2735.J2735SPAT\"}}";

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
            assertEquals("172.19.0.1", processedSpatJson.key.getRsuId());
            assertEquals(12110, processedSpatJson.key.getIntersectionId());
            assertNotNull(processedSpatJson.value);
            assertEquals(2, processedSpatJson.value.getStates().size());
        }
        
    }
}
