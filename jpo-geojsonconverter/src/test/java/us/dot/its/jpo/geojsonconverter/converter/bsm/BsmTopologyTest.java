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
    String odeBsmJsonString =
            "{\"metadata\":{\"logFileName\":\"\",\"recordType\":\"bsmTx\",\"securityResultCode\":\"success\",\"receivedMessageDetails\":{\"locationData\":{\"latitude\":\"unavailable\",\"longitude\":\"unavailable\",\"elevation\":\"unavailable\",\"speed\":\"unavailable\",\"heading\":\"unavailable\"},\"rxSource\":\"RSU\"},\"payloadType\":\"us.dot.its.jpo.ode.model.OdeMessageFramePayload\",\"serialId\":{\"streamId\":\"d4c3d057-2cb0-4bac-8b08-bac8abccdab4\",\"bundleSize\":1,\"bundleId\":0,\"recordId\":0,\"serialNumber\":0},\"odeReceivedAt\":\"2025-06-26T22:30:25.014Z\",\"schemaVersion\":9,\"maxDurationTime\":0,\"recordGeneratedAt\":\"\",\"recordGeneratedBy\":\"OBU\",\"sanitized\":false,\"odePacketID\":\"\",\"odeTimStartDateTime\":\"\",\"asn1\":\"001480B8494C4C950CD8CDE6E9651116579F22A424DD78FFFFF00761E4FD7EB7D07F7FFF80005F11D1020214C1C0FFC7C016AFF4017A0FF65403B0FD204C20FFCCC04F8FE40C420FFE6404CEFE60E9A10133408FCFDE1438103AB4138F00E1EEC1048EC160103E237410445C171104E26BC103DC4154305C2C84103B1C1C8F0A82F42103F34262D1123198103DAC25FB12034CE10381C259F12038CA103574251B10E3B2210324C23AD0F23D8EFFFE0000209340D10000004264BF00\",\"source\":\"EV\",\"originIp\":\"172.18.0.1\",\"isCertPresent\":false},\"payload\":{\"data\":{\"messageId\":20,\"value\":{\"BasicSafetyMessage\":{\"coreData\":{\"msgCnt\":37,\"id\":\"31325433\",\"secMark\":25399,\"lat\":405659938,\"long\":-1050317754,\"elev\":14409,\"accuracy\":{\"semiMajor\":186,\"semiMinor\":241,\"orientation\":65535},\"transmission\":\"unavailable\",\"speed\":14,\"heading\":25060,\"angle\":127,\"accelSet\":{\"long\":27,\"lat\":0,\"vert\":0,\"yaw\":0},\"brakes\":{\"wheelBrakes\":\"80\",\"traction\":\"unavailable\",\"abs\":\"unavailable\",\"scs\":\"unavailable\",\"brakeBoost\":\"unavailable\",\"auxBrakes\":\"unavailable\"},\"size\":{\"width\":190,\"length\":570}},\"partII\":[{\"partII-Id\":0,\"partII-Value\":{\"VehicleSafetyExtensions\":{\"pathHistory\":{\"crumbData\":[{\"latOffset\":-113,\"lonOffset\":181,\"elevationOffset\":-6,\"timeOffset\":190},{\"latOffset\":-310,\"lonOffset\":472,\"elevationOffset\":-23,\"timeOffset\":610},{\"latOffset\":-103,\"lonOffset\":636,\"elevationOffset\":-14,\"timeOffset\":1570},{\"latOffset\":-52,\"lonOffset\":615,\"elevationOffset\":-13,\"timeOffset\":1870},{\"latOffset\":614,\"lonOffset\":1150,\"elevationOffset\":-17,\"timeOffset\":2589},{\"latOffset\":1878,\"lonOffset\":2503,\"elevationOffset\":7,\"timeOffset\":3959},{\"latOffset\":2333,\"lonOffset\":2816,\"elevationOffset\":31,\"timeOffset\":4539},{\"latOffset\":2187,\"lonOffset\":2952,\"elevationOffset\":39,\"timeOffset\":4959},{\"latOffset\":1976,\"lonOffset\":2721,\"elevationOffset\":46,\"timeOffset\":5699},{\"latOffset\":1891,\"lonOffset\":3655,\"elevationOffset\":84,\"timeOffset\":6050},{\"latOffset\":2022,\"lonOffset\":4886,\"elevationOffset\":137,\"timeOffset\":6349},{\"latOffset\":1973,\"lonOffset\":4861,\"elevationOffset\":144,\"timeOffset\":6760},{\"latOffset\":1795,\"lonOffset\":4815,\"elevationOffset\":144,\"timeOffset\":7270},{\"latOffset\":1710,\"lonOffset\":4749,\"elevationOffset\":135,\"timeOffset\":7570},{\"latOffset\":1609,\"lonOffset\":4566,\"elevationOffset\":121,\"timeOffset\":7880}]},\"pathPrediction\":{\"radiusOfCurve\":32767,\"confidence\":0}}}},{\"partII-Id\":2,\"partII-Value\":{\"SupplementalVehicleExtensions\":{\"classDetails\":{\"keyType\":0,\"role\":\"basicVehicle\",\"hpmsType\":\"none\",\"fuelType\":0},\"vehicleData\":{\"height\":38},\"doNotUse2\":{\"airTemp\":191}}}}]}}},\"dataType\":\"us.dot.its.jpo.asn.j2735.r2024.BasicSafetyMessage.BasicSafetyMessageMessageFrame\"}}";

    @Autowired
    BsmJsonValidator bsmJsonValidator;

    @Test
    public void testTopology() {
        Topology topology = BsmTopology.build(kafkaTopicOdeBsmJson, kafkaTopicProcessedBsm, bsmJsonValidator);
        try (TopologyTestDriver driver = new TopologyTestDriver(topology)) {
            TestInputTopic<Void, String> inputTopic = driver.createInputTopic(kafkaTopicOdeBsmJson,
                    Serdes.Void().serializer(), Serdes.String().serializer());
            TestOutputTopic<RsuLogKey, ProcessedBsm<Point>> outputTopic =
                    driver.createOutputTopic(kafkaTopicProcessedBsm, JsonSerdes.RsuLogKey().deserializer(),
                            JsonSerdes.ProcessedBsm().deserializer());

            // Send serialized OdeBsmJson to OdeBsmJson topic
            inputTopic.pipeInput(odeBsmJsonString);

            // Check ProcessedBsm topic for properly converted message data
            List<KeyValue<RsuLogKey, ProcessedBsm<Point>>> processedBsmJsonResults = outputTopic.readKeyValuesToList();
            assertEquals(processedBsmJsonResults.size(), 1);

            KeyValue<RsuLogKey, ProcessedBsm<Point>> processedBsmJson = processedBsmJsonResults.get(0);
            assertNotNull(processedBsmJson.key);
            assertEquals(new RsuLogKey("172.18.0.1", "", "31325433"), processedBsmJson.key);
            assertNotNull(processedBsmJson.value);
            assertEquals("31325433", processedBsmJson.value.getProperties().getId());
        }

    }

    @Test
    public void testTopologyFailure() {
        Topology topology = BsmTopology.build(kafkaTopicOdeBsmJson, kafkaTopicProcessedBsm, bsmJsonValidator);
        try (TopologyTestDriver driver = new TopologyTestDriver(topology)) {
            TestInputTopic<Void, String> inputTopic = driver.createInputTopic(kafkaTopicOdeBsmJson,
                    Serdes.Void().serializer(), Serdes.String().serializer());
            TestOutputTopic<RsuLogKey, ProcessedBsm<Point>> outputTopic =
                    driver.createOutputTopic(kafkaTopicProcessedBsm, JsonSerdes.RsuLogKey().deserializer(),
                            JsonSerdes.ProcessedBsm().deserializer());

            // Send serialized OdeSpatJson to OdeSpatJson topic
            inputTopic.pipeInput("{");

            // Check ProcessedBsm topic for properly converted message data
            List<KeyValue<RsuLogKey, ProcessedBsm<Point>>> processedBsmJsonResults = outputTopic.readKeyValuesToList();
            assertEquals(processedBsmJsonResults.size(), 1);

            KeyValue<RsuLogKey, ProcessedBsm<Point>> processedBsmJson = processedBsmJsonResults.get(0);
            assertNotNull(processedBsmJson.key);
            assertEquals(new RsuLogKey(null, null, "ERROR"), processedBsmJson.key);
        }

    }
}
