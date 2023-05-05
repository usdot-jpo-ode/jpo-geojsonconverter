package us.dot.its.jpo.geojsonconverter.converter.map;

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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import us.dot.its.jpo.geojsonconverter.partitioner.RsuIntersectionKey;
import us.dot.its.jpo.geojsonconverter.pojos.geojson.LineString;
import us.dot.its.jpo.geojsonconverter.pojos.geojson.map.ProcessedMap;
import us.dot.its.jpo.geojsonconverter.serialization.JsonSerdes;
import us.dot.its.jpo.geojsonconverter.validator.MapJsonValidator;

@SpringBootTest
@RunWith(SpringRunner.class)
@ActiveProfiles("test")
public class MapTopologyTest {
    String kafkaTopicOdeMapJson = "topic.OdeMapJson";
    String kafkaTopicMapGeoJson = "topic.ProcessedMap";
    String kafkaTopicMapWKT = "topic.ProcessedMapWKT";

    String odeMapJsonString = "{\"metadata\":{\"logFileName\":\"\",\"recordType\":\"mapTx\",\"securityResultCode\":\"success\",\"receivedMessageDetails\":{\"locationData\":null,\"rxSource\":\"NA\"},\"encodings\":null,\"payloadType\":\"us.dot.its.jpo.ode.model.OdeMapPayload\",\"serialId\":{\"streamId\":\"02733ccc-9f3c-47e6-bbc9-6c670ab9cc41\",\"bundleSize\":1,\"bundleId\":0,\"recordId\":0,\"serialNumber\":0},\"odeReceivedAt\":\"2022-10-21T17:06:13.446432Z\",\"schemaVersion\":6,\"maxDurationTime\":0,\"recordGeneratedAt\":\"\",\"recordGeneratedBy\":null,\"sanitized\":false,\"odePacketID\":\"\",\"odeTimStartDateTime\":\"\",\"mapSource\":\"RSU\",\"originIp\":\"172.19.0.1\"},\"payload\":{\"data\":{\"timeStamp\":null,\"msgIssueRevision\":2,\"layerType\":\"intersectionData\",\"layerID\":0,\"intersections\":{\"intersectionGeometry\":[{\"name\":null,\"id\":{\"region\":null,\"id\":12110},\"revision\":2,\"refPoint\":{\"latitude\":39.5952649,\"longitude\":-105.0914122,\"elevation\":1677.0},\"laneWidth\":366,\"speedLimits\":null,\"laneSet\":{\"GenericLane\":[{\"laneID\":2,\"name\":null,\"ingressApproach\":1,\"egressApproach\":null,\"laneAttributes\":{\"directionalUse\":{\"ingressPath\":true,\"egressPath\":false},\"shareWith\":{\"busVehicleTraffic\":false,\"trackedVehicleTraffic\":false,\"individualMotorizedVehicleTraffic\":false,\"taxiVehicleTraffic\":false,\"overlappingLaneDescriptionProvided\":false,\"cyclistVehicleTraffic\":false,\"otherNonMotorizedTrafficTypes\":false,\"multipleLanesTreatedAsOneLane\":false,\"pedestrianTraffic\":false,\"pedestriansTraffic\":false},\"laneType\":{\"vehicle\":{\"isVehicleRevocableLane\":false,\"isVehicleFlyOverLane\":false,\"permissionOnRequest\":false,\"hasIRbeaconCoverage\":false,\"restrictedToBusUse\":false,\"restrictedToTaxiUse\":false,\"restrictedFromPublicUse\":false,\"hovLaneUseOnly\":false},\"crosswalk\":null,\"bikeLane\":null,\"sidewalk\":null,\"median\":null,\"striping\":null,\"trackedVehicle\":null,\"parking\":null}},\"maneuvers\":{\"maneuverStraightAllowed\":true,\"maneuverNoStoppingAllowed\":false,\"goWithHalt\":false,\"maneuverLeftAllowed\":false,\"maneuverUTurnAllowed\":false,\"maneuverLeftTurnOnRedAllowed\":false,\"reserved1\":false,\"maneuverRightAllowed\":false,\"maneuverLaneChangeAllowed\":false,\"yieldAllwaysRequired\":false,\"maneuverRightTurnOnRedAllowed\":false,\"caution\":false},\"nodeList\":{\"computed\":null,\"nodes\":{\"NodeXY\":[{\"delta\":{\"nodeXY1\":null,\"nodeXY2\":null,\"nodeXY3\":null,\"nodeXY4\":{\"x\":2225,\"y\":808},\"nodeXY5\":null,\"nodeXY6\":null,\"nodeLatLon\":null},\"attributes\":{\"localNode\":null,\"disabled\":null,\"enabled\":null,\"data\":null,\"dWidth\":null,\"dElevation\":-10}},{\"delta\":{\"nodeXY1\":null,\"nodeXY2\":null,\"nodeXY3\":null,\"nodeXY4\":null,\"nodeXY5\":null,\"nodeXY6\":{\"x\":10517,\"y\":-161},\"nodeLatLon\":null},\"attributes\":{\"localNode\":null,\"disabled\":null,\"enabled\":null,\"data\":null,\"dWidth\":null,\"dElevation\":-60}},{\"delta\":{\"nodeXY1\":null,\"nodeXY2\":null,\"nodeXY3\":null,\"nodeXY4\":{\"x\":2769,\"y\":112},\"nodeXY5\":null,\"nodeXY6\":null,\"nodeLatLon\":null},\"attributes\":{\"localNode\":null,\"disabled\":null,\"enabled\":null,\"data\":null,\"dWidth\":null,\"dElevation\":-10}},{\"delta\":{\"nodeXY1\":null,\"nodeXY2\":null,\"nodeXY3\":null,\"nodeXY4\":null,\"nodeXY5\":{\"x\":6142,\"y\":-180},\"nodeXY6\":null,\"nodeLatLon\":null},\"attributes\":{\"localNode\":null,\"disabled\":null,\"enabled\":null,\"data\":null,\"dWidth\":null,\"dElevation\":-30}},{\"delta\":{\"nodeXY1\":null,\"nodeXY2\":null,\"nodeXY3\":null,\"nodeXY4\":null,\"nodeXY5\":{\"x\":6636,\"y\":-12},\"nodeXY6\":null,\"nodeLatLon\":null},\"attributes\":{\"localNode\":null,\"disabled\":null,\"enabled\":null,\"data\":null,\"dWidth\":null,\"dElevation\":-20}},{\"delta\":{\"nodeXY1\":null,\"nodeXY2\":null,\"nodeXY3\":null,\"nodeXY4\":{\"x\":3804,\"y\":-7},\"nodeXY5\":null,\"nodeXY6\":null,\"nodeLatLon\":null},\"attributes\":null}]}},\"connectsTo\":{\"connectsTo\":[{\"connectingLane\":{\"lane\":3,\"maneuver\":{\"maneuverStraightAllowed\":true,\"maneuverNoStoppingAllowed\":false,\"goWithHalt\":false,\"maneuverLeftAllowed\":false,\"maneuverUTurnAllowed\":false,\"maneuverLeftTurnOnRedAllowed\":false,\"reserved1\":false,\"maneuverRightAllowed\":false,\"maneuverLaneChangeAllowed\":false,\"yieldAllwaysRequired\":false,\"maneuverRightTurnOnRedAllowed\":false,\"caution\":false}},\"remoteIntersection\":null,\"signalGroup\":8,\"userClass\":null,\"connectionID\":1}]},\"overlays\":null},{\"laneID\":3,\"name\":null,\"ingressApproach\":1,\"egressApproach\":null,\"laneAttributes\":{\"directionalUse\":{\"ingressPath\":true,\"egressPath\":false},\"shareWith\":{\"busVehicleTraffic\":false,\"trackedVehicleTraffic\":false,\"individualMotorizedVehicleTraffic\":false,\"taxiVehicleTraffic\":false,\"overlappingLaneDescriptionProvided\":false,\"cyclistVehicleTraffic\":false,\"otherNonMotorizedTrafficTypes\":false,\"multipleLanesTreatedAsOneLane\":false,\"pedestrianTraffic\":false,\"pedestriansTraffic\":false},\"laneType\":{\"vehicle\":{\"isVehicleRevocableLane\":false,\"isVehicleFlyOverLane\":false,\"permissionOnRequest\":false,\"hasIRbeaconCoverage\":false,\"restrictedToBusUse\":false,\"restrictedToTaxiUse\":false,\"restrictedFromPublicUse\":false,\"hovLaneUseOnly\":false},\"crosswalk\":null,\"bikeLane\":null,\"sidewalk\":null,\"median\":null,\"striping\":null,\"trackedVehicle\":null,\"parking\":null}},\"maneuvers\":{\"maneuverStraightAllowed\":true,\"maneuverNoStoppingAllowed\":false,\"goWithHalt\":false,\"maneuverLeftAllowed\":false,\"maneuverUTurnAllowed\":false,\"maneuverLeftTurnOnRedAllowed\":false,\"reserved1\":false,\"maneuverRightAllowed\":false,\"maneuverLaneChangeAllowed\":false,\"yieldAllwaysRequired\":false,\"maneuverRightTurnOnRedAllowed\":false,\"caution\":false},\"nodeList\":{\"computed\":null,\"nodes\":{\"NodeXY\":[{\"delta\":{\"nodeXY1\":null,\"nodeXY2\":null,\"nodeXY3\":null,\"nodeXY4\":{\"x\":2222,\"y\":515},\"nodeXY5\":null,\"nodeXY6\":null,\"nodeLatLon\":null},\"attributes\":{\"localNode\":null,\"disabled\":null,\"enabled\":null,\"data\":null,\"dWidth\":null,\"dElevation\":-10}},{\"delta\":{\"nodeXY1\":null,\"nodeXY2\":null,\"nodeXY3\":null,\"nodeXY4\":{\"x\":2933,\"y\":-82},\"nodeXY5\":null,\"nodeXY6\":null,\"nodeLatLon\":null},\"attributes\":{\"localNode\":null,\"disabled\":null,\"enabled\":null,\"data\":null,\"dWidth\":null,\"dElevation\":-10}},{\"delta\":{\"nodeXY1\":null,\"nodeXY2\":null,\"nodeXY3\":null,\"nodeXY4\":null,\"nodeXY5\":{\"x\":6259,\"y\":-85},\"nodeXY6\":null,\"nodeLatLon\":null},\"attributes\":{\"localNode\":null,\"disabled\":null,\"enabled\":null,\"data\":null,\"dWidth\":null,\"dElevation\":-40}},{\"delta\":{\"nodeXY1\":null,\"nodeXY2\":null,\"nodeXY3\":null,\"nodeXY4\":{\"x\":3416,\"y\":-98},\"nodeXY5\":null,\"nodeXY6\":null,\"nodeLatLon\":null},\"attributes\":{\"localNode\":null,\"disabled\":null,\"enabled\":null,\"data\":null,\"dWidth\":null,\"dElevation\":-20}},{\"delta\":{\"nodeXY1\":null,\"nodeXY2\":null,\"nodeXY3\":null,\"nodeXY4\":null,\"nodeXY5\":{\"x\":6867,\"y\":-39},\"nodeXY6\":null,\"nodeLatLon\":null},\"attributes\":{\"localNode\":null,\"disabled\":null,\"enabled\":null,\"data\":null,\"dWidth\":null,\"dElevation\":-30}},{\"delta\":{\"nodeXY1\":null,\"nodeXY2\":null,\"nodeXY3\":null,\"nodeXY4\":{\"x\":2657,\"y\":-55},\"nodeXY5\":null,\"nodeXY6\":null,\"nodeLatLon\":null},\"attributes\":{\"localNode\":null,\"disabled\":null,\"enabled\":null,\"data\":null,\"dWidth\":null,\"dElevation\":-10}},{\"delta\":{\"nodeXY1\":null,\"nodeXY2\":null,\"nodeXY3\":null,\"nodeXY4\":null,\"nodeXY5\":{\"x\":4259,\"y\":67},\"nodeXY6\":null,\"nodeLatLon\":null},\"attributes\":{\"localNode\":null,\"disabled\":null,\"enabled\":null,\"data\":null,\"dWidth\":null,\"dElevation\":-10}},{\"delta\":{\"nodeXY1\":null,\"nodeXY2\":null,\"nodeXY3\":null,\"nodeXY4\":{\"x\":3481,\"y\":0},\"nodeXY5\":null,\"nodeXY6\":null,\"nodeLatLon\":null},\"attributes\":null}]}},\"connectsTo\":{\"connectsTo\":[{\"connectingLane\":{\"lane\":2,\"maneuver\":{\"maneuverStraightAllowed\":true,\"maneuverNoStoppingAllowed\":false,\"goWithHalt\":false,\"maneuverLeftAllowed\":false,\"maneuverUTurnAllowed\":false,\"maneuverLeftTurnOnRedAllowed\":false,\"reserved1\":false,\"maneuverRightAllowed\":false,\"maneuverLaneChangeAllowed\":false,\"yieldAllwaysRequired\":false,\"maneuverRightTurnOnRedAllowed\":false,\"caution\":false}},\"remoteIntersection\":null,\"signalGroup\":8,\"userClass\":null,\"connectionID\":1}]},\"overlays\":null}]}}]},\"roadSegments\":null,\"dataParameters\":null,\"restrictionList\":null},\"dataType\":\"us.dot.its.jpo.ode.plugin.j2735.J2735MAP\"}}";
    String geoJsonMapJsonString = "{\"mapFeatureCollection\":{\"type\":\"FeatureCollection\",\"features\":[{\"type\":\"Feature\",\"id\":2,\"geometry\":{\"type\":\"LineString\",\"coordinates\":[[-105.0911523,39.5953376],[-105.0899239,39.5953231],[-105.0896005,39.5953332],[-105.0888831,39.595317],[-105.088108,39.5953159],[-105.0876637,39.5953153]]},\"properties\":{\"nodes\":[{\"delta\":[2225,808],\"delevation\":-10},{\"delta\":[10517,-161],\"delevation\":-60},{\"delta\":[2769,112],\"delevation\":-10},{\"delta\":[6142,-180],\"delevation\":-30},{\"delta\":[6636,-12],\"delevation\":-20},{\"delta\":[3804,-7]}],\"laneId\":2,\"sharedWith\":{\"busVehicleTraffic\":false,\"trackedVehicleTraffic\":false,\"individualMotorizedVehicleTraffic\":false,\"taxiVehicleTraffic\":false,\"overlappingLaneDescriptionProvided\":false,\"cyclistVehicleTraffic\":false,\"otherNonMotorizedTrafficTypes\":false,\"multipleLanesTreatedAsOneLane\":false,\"pedestrianTraffic\":false,\"pedestriansTraffic\":false},\"egressApproach\":0,\"ingressApproach\":1,\"ingressPath\":true,\"egressPath\":false,\"maneuvers\":{\"maneuverStraightAllowed\":true,\"maneuverNoStoppingAllowed\":false,\"goWithHalt\":false,\"maneuverLeftAllowed\":false,\"maneuverUTurnAllowed\":false,\"maneuverLeftTurnOnRedAllowed\":false,\"reserved1\":false,\"maneuverRightAllowed\":false,\"maneuverLaneChangeAllowed\":false,\"yieldAllwaysRequired\":false,\"maneuverRightTurnOnRedAllowed\":false,\"caution\":false},\"connectsTo\":[{\"connectingLane\":{\"lane\":3,\"maneuver\":{\"maneuverStraightAllowed\":true,\"maneuverNoStoppingAllowed\":false,\"goWithHalt\":false,\"maneuverLeftAllowed\":false,\"maneuverUTurnAllowed\":false,\"maneuverLeftTurnOnRedAllowed\":false,\"reserved1\":false,\"maneuverRightAllowed\":false,\"maneuverLaneChangeAllowed\":false,\"yieldAllwaysRequired\":false,\"maneuverRightTurnOnRedAllowed\":false,\"caution\":false}},\"signalGroup\":8,\"connectionID\":1}]}},{\"type\":\"Feature\",\"id\":3,\"geometry\":{\"type\":\"LineString\",\"coordinates\":[[-105.0911527,39.5953113],[-105.0908101,39.5953039],[-105.0900791,39.5952962],[-105.0896801,39.5952874],[-105.0888781,39.5952839],[-105.0885678,39.5952789],[-105.0880704,39.5952849],[-105.0876638,39.5952849]]},\"properties\":{\"nodes\":[{\"delta\":[2222,515],\"delevation\":-10},{\"delta\":[2933,-82],\"delevation\":-10},{\"delta\":[6259,-85],\"delevation\":-40},{\"delta\":[3416,-98],\"delevation\":-20},{\"delta\":[6867,-39],\"delevation\":-30},{\"delta\":[2657,-55],\"delevation\":-10},{\"delta\":[4259,67],\"delevation\":-10},{\"delta\":[3481,0]}],\"laneId\":3,\"sharedWith\":{\"busVehicleTraffic\":false,\"trackedVehicleTraffic\":false,\"individualMotorizedVehicleTraffic\":false,\"taxiVehicleTraffic\":false,\"overlappingLaneDescriptionProvided\":false,\"cyclistVehicleTraffic\":false,\"otherNonMotorizedTrafficTypes\":false,\"multipleLanesTreatedAsOneLane\":false,\"pedestrianTraffic\":false,\"pedestriansTraffic\":false},\"egressApproach\":0,\"ingressApproach\":1,\"ingressPath\":true,\"egressPath\":false,\"maneuvers\":{\"maneuverStraightAllowed\":true,\"maneuverNoStoppingAllowed\":false,\"goWithHalt\":false,\"maneuverLeftAllowed\":false,\"maneuverUTurnAllowed\":false,\"maneuverLeftTurnOnRedAllowed\":false,\"reserved1\":false,\"maneuverRightAllowed\":false,\"maneuverLaneChangeAllowed\":false,\"yieldAllwaysRequired\":false,\"maneuverRightTurnOnRedAllowed\":false,\"caution\":false},\"connectsTo\":[{\"connectingLane\":{\"lane\":2,\"maneuver\":{\"maneuverStraightAllowed\":true,\"maneuverNoStoppingAllowed\":false,\"goWithHalt\":false,\"maneuverLeftAllowed\":false,\"maneuverUTurnAllowed\":false,\"maneuverLeftTurnOnRedAllowed\":false,\"reserved1\":false,\"maneuverRightAllowed\":false,\"maneuverLaneChangeAllowed\":false,\"yieldAllwaysRequired\":false,\"maneuverRightTurnOnRedAllowed\":false,\"caution\":false}},\"signalGroup\":8,\"connectionID\":1}]}}]},\"connectingLanesFeatureCollection\":{\"type\":\"FeatureCollection\",\"features\":[{\"type\":\"Feature\",\"id\":\"2-3\",\"geometry\":{\"type\":\"LineString\",\"coordinates\":[[-105.0911523,39.5953376],[-105.0911527,39.5953113]]},\"properties\":{\"signalGroupId\":8,\"ingressLaneId\":2,\"egressLaneId\":3}},{\"type\":\"Feature\",\"id\":\"3-2\",\"geometry\":{\"type\":\"LineString\",\"coordinates\":[[-105.0911527,39.5953113],[-105.0911523,39.5953376]]},\"properties\":{\"signalGroupId\":8,\"ingressLaneId\":3,\"egressLaneId\":2}}]},\"properties\":{\"messageType\":\"MAP\",\"odeReceivedAt\":\"2022-10-21T17:06:13.446432Z\",\"originIp\":\"172.19.0.1\",\"intersectionId\":12110,\"msgIssueRevision\":2,\"revision\":2,\"refPoint\":{\"latitude\":39.5952649,\"longitude\":-105.0914122,\"elevation\":1677.0},\"cti4501Conformant\":false,\"validationMessages\":[{\"message\":\"$.payload.data.intersections.intersectionGeometry[0].id.region: null found, integer expected\",\"jsonPath\":\"$.payload.data.intersections.intersectionGeometry[0].id.region\",\"schemaPath\":\"#/$defs/J2735RoadRegulatorID/type\"},{\"message\":\"$.payload.data.intersections.intersectionGeometry[0].speedLimits: null found, object expected\",\"jsonPath\":\"$.payload.data.intersections.intersectionGeometry[0].speedLimits\",\"schemaPath\":\"#/$defs/J2735SpeedLimitList_Wrapper/type\"}],\"laneWidth\":366,\"mapSource\":\"RSU\",\"timeStamp\":\"2022-10-21T17:06:13.446432Z\"}}";

    @Autowired
    private MapJsonValidator mapJsonValidator;
    
    @Test
    public void testTopologyGeoJson() {
        Topology topology = MapTopology.build(kafkaTopicOdeMapJson, kafkaTopicMapGeoJson, kafkaTopicMapWKT, mapJsonValidator, false);
        try (TopologyTestDriver driver = new TopologyTestDriver(topology)) {
            TestInputTopic<Void, String> inputTopic = driver.createInputTopic(
                kafkaTopicOdeMapJson, 
                Serdes.Void().serializer(), 
                Serdes.String().serializer());
            TestOutputTopic<RsuIntersectionKey, ProcessedMap<LineString>> outputTopic = driver.createOutputTopic(
                kafkaTopicMapGeoJson, 
                JsonSerdes.RsuIntersectionKey().deserializer(), 
                JsonSerdes.ProcessedMapGeoJson().deserializer());
            
            // Send serialized OdeMapJson to OdeMapJson topic
            inputTopic.pipeInput(odeMapJsonString);

            // Check MapGeoJson topic for properly converted message data
            List<KeyValue<RsuIntersectionKey, ProcessedMap<LineString>>> mapGeoJsonResults = outputTopic.readKeyValuesToList();
            assertEquals(mapGeoJsonResults.size(), 1);

            KeyValue<RsuIntersectionKey, ProcessedMap<LineString>> mapGeoJson = mapGeoJsonResults.get(0);
            assertNotNull(mapGeoJson.key);
            assertEquals("172.19.0.1", mapGeoJson.key.getRsuId());
            assertEquals(12110, mapGeoJson.key.getIntersectionId());
            assertNotNull(mapGeoJson.value);
            assertEquals(2, mapGeoJson.value.getMapFeatureCollection().getFeatures().length);
            assertEquals(1, mapGeoJson.value.getMapFeatureCollection().getFeatures()[0].getProperties().getIngressApproach());
        }
    }

    @Test
    public void testTopologyWKT() {
        Topology topology = MapTopology.build(kafkaTopicOdeMapJson, kafkaTopicMapGeoJson, kafkaTopicMapWKT, mapJsonValidator, true);
        try (TopologyTestDriver driver = new TopologyTestDriver(topology)) {
            TestInputTopic<Void, String> inputOdeTopic = driver.createInputTopic(
                kafkaTopicOdeMapJson, 
                Serdes.Void().serializer(), 
                Serdes.String().serializer());
            TestOutputTopic<RsuIntersectionKey, ProcessedMap<String>> outputTopic = driver.createOutputTopic(
                kafkaTopicMapWKT, 
                JsonSerdes.RsuIntersectionKey().deserializer(), 
                JsonSerdes.ProcessedMapWKT().deserializer());
            
            // Send serialized OdeMapJson to OdeMapJson topic
            inputOdeTopic.pipeInput(odeMapJsonString);

            // Check MapWKT topic for properly converted message data
            List<KeyValue<RsuIntersectionKey, ProcessedMap<String>>> mapWKTResults = outputTopic.readKeyValuesToList();
            assertEquals(mapWKTResults.size(), 1);

            KeyValue<RsuIntersectionKey, ProcessedMap<String>> mapWKT = mapWKTResults.get(0);
            assertNotNull(mapWKT.key);
            assertEquals("172.19.0.1", mapWKT.key.getRsuId());
            assertEquals(12110, mapWKT.key.getIntersectionId());
            assertNotNull(mapWKT.value);
            assertEquals(2, mapWKT.value.getMapFeatureCollection().getFeatures().length);
            assertEquals("LINESTRING (-105.0911523 39.5953376, -105.0899239 39.5953231, -105.0896005 39.5953332, -105.0888831 39.595317, -105.088108 39.5953159, -105.0876637 39.5953153)", mapWKT.value.getMapFeatureCollection().getFeatures()[0].getGeometry());          
        }
    }

    @Test
    public void testTopologyFailureGeoJson() {
        Topology topology = MapTopology.build(kafkaTopicOdeMapJson, kafkaTopicMapGeoJson, kafkaTopicMapWKT, mapJsonValidator, false);
        try (TopologyTestDriver driver = new TopologyTestDriver(topology)) {
            TestInputTopic<Void, String> inputTopic = driver.createInputTopic(
                kafkaTopicOdeMapJson, 
                Serdes.Void().serializer(), 
                Serdes.String().serializer());
            TestOutputTopic<RsuIntersectionKey, ProcessedMap<LineString>> outputTopic = driver.createOutputTopic(
                kafkaTopicMapGeoJson, 
                JsonSerdes.RsuIntersectionKey().deserializer(), 
                JsonSerdes.ProcessedMapGeoJson().deserializer());
            
            // Send serialized OdeMapJson to OdeMapJson topic
            inputTopic.pipeInput("{");

            // Check MapGeoJson topic for properly converted message data
            List<KeyValue<RsuIntersectionKey, ProcessedMap<LineString>>> mapGeoJsonResults = outputTopic.readKeyValuesToList();
            assertEquals(mapGeoJsonResults.size(), 1);

            KeyValue<RsuIntersectionKey, ProcessedMap<LineString>> mapGeoJson = mapGeoJsonResults.get(0);
            assertNotNull(mapGeoJson.key);
            assertEquals("ERROR", mapGeoJson.key.getRsuId());
            assertNotNull(mapGeoJson.value);
            assertEquals(1, mapGeoJson.value.getProperties().getValidationMessages().size());
        }
    }
}
