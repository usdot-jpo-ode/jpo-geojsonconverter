package us.dot.its.jpo.geojsonconverter.converter.map;

import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URL;
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
import org.springframework.core.io.Resource;
import org.springframework.lang.Nullable;
import org.springframework.test.context.junit4.SpringRunner;

import us.dot.its.jpo.geojsonconverter.geojson.map.MapFeatureCollection;
import us.dot.its.jpo.geojsonconverter.serialization.JsonSerdes;
import us.dot.its.jpo.geojsonconverter.validator.JsonValidatorResult;
import us.dot.its.jpo.geojsonconverter.validator.MapJsonValidator;

@SpringBootTest
@RunWith(SpringRunner.class)
public class MapTopologyTest {
    String kafkaTopicOdeMapJson = "topic.OdeMapJson";
    String kafkaTopicMapGeoJson = "topic.MapGeoJson";

    String odeMapJsonString = "{\"metadata\":{\"logFileName\":\"\",\"recordType\":\"mapTx\",\"securityResultCode\":\"success\",\"receivedMessageDetails\":{\"locationData\":null,\"rxSource\":\"NA\"},\"encodings\":null,\"payloadType\":\"us.dot.its.jpo.ode.model.OdeMapPayload\",\"serialId\":{\"streamId\":\"02733ccc-9f3c-47e6-bbc9-6c670ab9cc41\",\"bundleSize\":1,\"bundleId\":0,\"recordId\":0,\"serialNumber\":0},\"odeReceivedAt\":\"2022-10-21T17:06:13.446432Z\",\"schemaVersion\":6,\"maxDurationTime\":0,\"recordGeneratedAt\":\"\",\"recordGeneratedBy\":null,\"sanitized\":false,\"odePacketID\":\"\",\"odeTimStartDateTime\":\"\",\"mapSource\":\"RSU\",\"originIp\":\"172.19.0.1\"},\"payload\":{\"data\":{\"timeStamp\":null,\"msgIssueRevision\":2,\"layerType\":\"intersectionData\",\"layerID\":0,\"intersections\":{\"intersectionGeometry\":[{\"name\":null,\"id\":{\"region\":null,\"id\":12110},\"revision\":2,\"refPoint\":{\"latitude\":39.5952649,\"longitude\":-105.0914122,\"elevation\":1677.0},\"laneWidth\":366,\"speedLimits\":null,\"laneSet\":{\"GenericLane\":[{\"laneID\":2,\"name\":null,\"ingressApproach\":1,\"egressApproach\":null,\"laneAttributes\":{\"directionalUse\":{\"ingressPath\":true,\"egressPath\":false},\"shareWith\":{\"busVehicleTraffic\":false,\"trackedVehicleTraffic\":false,\"individualMotorizedVehicleTraffic\":false,\"taxiVehicleTraffic\":false,\"overlappingLaneDescriptionProvided\":false,\"cyclistVehicleTraffic\":false,\"otherNonMotorizedTrafficTypes\":false,\"multipleLanesTreatedAsOneLane\":false,\"pedestrianTraffic\":false,\"pedestriansTraffic\":false},\"laneType\":{\"vehicle\":{\"isVehicleRevocableLane\":false,\"isVehicleFlyOverLane\":false,\"permissionOnRequest\":false,\"hasIRbeaconCoverage\":false,\"restrictedToBusUse\":false,\"restrictedToTaxiUse\":false,\"restrictedFromPublicUse\":false,\"hovLaneUseOnly\":false},\"crosswalk\":null,\"bikeLane\":null,\"sidewalk\":null,\"median\":null,\"striping\":null,\"trackedVehicle\":null,\"parking\":null}},\"maneuvers\":{\"maneuverStraightAllowed\":true,\"maneuverNoStoppingAllowed\":false,\"goWithHalt\":false,\"maneuverLeftAllowed\":false,\"maneuverUTurnAllowed\":false,\"maneuverLeftTurnOnRedAllowed\":false,\"reserved1\":false,\"maneuverRightAllowed\":false,\"maneuverLaneChangeAllowed\":false,\"yieldAllwaysRequired\":false,\"maneuverRightTurnOnRedAllowed\":false,\"caution\":false},\"nodeList\":{\"computed\":null,\"nodes\":{\"NodeXY\":[{\"delta\":{\"nodeXY1\":null,\"nodeXY2\":null,\"nodeXY3\":null,\"nodeXY4\":{\"x\":2225,\"y\":808},\"nodeXY5\":null,\"nodeXY6\":null,\"nodeLatLon\":null},\"attributes\":{\"localNode\":null,\"disabled\":null,\"enabled\":null,\"data\":null,\"dWidth\":null,\"dElevation\":-10}},{\"delta\":{\"nodeXY1\":null,\"nodeXY2\":null,\"nodeXY3\":null,\"nodeXY4\":null,\"nodeXY5\":null,\"nodeXY6\":{\"x\":10517,\"y\":-161},\"nodeLatLon\":null},\"attributes\":{\"localNode\":null,\"disabled\":null,\"enabled\":null,\"data\":null,\"dWidth\":null,\"dElevation\":-60}},{\"delta\":{\"nodeXY1\":null,\"nodeXY2\":null,\"nodeXY3\":null,\"nodeXY4\":{\"x\":2769,\"y\":112},\"nodeXY5\":null,\"nodeXY6\":null,\"nodeLatLon\":null},\"attributes\":{\"localNode\":null,\"disabled\":null,\"enabled\":null,\"data\":null,\"dWidth\":null,\"dElevation\":-10}},{\"delta\":{\"nodeXY1\":null,\"nodeXY2\":null,\"nodeXY3\":null,\"nodeXY4\":null,\"nodeXY5\":{\"x\":6142,\"y\":-180},\"nodeXY6\":null,\"nodeLatLon\":null},\"attributes\":{\"localNode\":null,\"disabled\":null,\"enabled\":null,\"data\":null,\"dWidth\":null,\"dElevation\":-30}},{\"delta\":{\"nodeXY1\":null,\"nodeXY2\":null,\"nodeXY3\":null,\"nodeXY4\":null,\"nodeXY5\":{\"x\":6636,\"y\":-12},\"nodeXY6\":null,\"nodeLatLon\":null},\"attributes\":{\"localNode\":null,\"disabled\":null,\"enabled\":null,\"data\":null,\"dWidth\":null,\"dElevation\":-20}},{\"delta\":{\"nodeXY1\":null,\"nodeXY2\":null,\"nodeXY3\":null,\"nodeXY4\":{\"x\":3804,\"y\":-7},\"nodeXY5\":null,\"nodeXY6\":null,\"nodeLatLon\":null},\"attributes\":null}]}},\"connectsTo\":{\"connectsTo\":[{\"connectingLane\":{\"lane\":19,\"maneuver\":{\"maneuverStraightAllowed\":true,\"maneuverNoStoppingAllowed\":false,\"goWithHalt\":false,\"maneuverLeftAllowed\":false,\"maneuverUTurnAllowed\":false,\"maneuverLeftTurnOnRedAllowed\":false,\"reserved1\":false,\"maneuverRightAllowed\":false,\"maneuverLaneChangeAllowed\":false,\"yieldAllwaysRequired\":false,\"maneuverRightTurnOnRedAllowed\":false,\"caution\":false}},\"remoteIntersection\":null,\"signalGroup\":8,\"userClass\":null,\"connectionID\":1}]},\"overlays\":null},{\"laneID\":3,\"name\":null,\"ingressApproach\":1,\"egressApproach\":null,\"laneAttributes\":{\"directionalUse\":{\"ingressPath\":true,\"egressPath\":false},\"shareWith\":{\"busVehicleTraffic\":false,\"trackedVehicleTraffic\":false,\"individualMotorizedVehicleTraffic\":false,\"taxiVehicleTraffic\":false,\"overlappingLaneDescriptionProvided\":false,\"cyclistVehicleTraffic\":false,\"otherNonMotorizedTrafficTypes\":false,\"multipleLanesTreatedAsOneLane\":false,\"pedestrianTraffic\":false,\"pedestriansTraffic\":false},\"laneType\":{\"vehicle\":{\"isVehicleRevocableLane\":false,\"isVehicleFlyOverLane\":false,\"permissionOnRequest\":false,\"hasIRbeaconCoverage\":false,\"restrictedToBusUse\":false,\"restrictedToTaxiUse\":false,\"restrictedFromPublicUse\":false,\"hovLaneUseOnly\":false},\"crosswalk\":null,\"bikeLane\":null,\"sidewalk\":null,\"median\":null,\"striping\":null,\"trackedVehicle\":null,\"parking\":null}},\"maneuvers\":{\"maneuverStraightAllowed\":true,\"maneuverNoStoppingAllowed\":false,\"goWithHalt\":false,\"maneuverLeftAllowed\":false,\"maneuverUTurnAllowed\":false,\"maneuverLeftTurnOnRedAllowed\":false,\"reserved1\":false,\"maneuverRightAllowed\":false,\"maneuverLaneChangeAllowed\":false,\"yieldAllwaysRequired\":false,\"maneuverRightTurnOnRedAllowed\":false,\"caution\":false},\"nodeList\":{\"computed\":null,\"nodes\":{\"NodeXY\":[{\"delta\":{\"nodeXY1\":null,\"nodeXY2\":null,\"nodeXY3\":null,\"nodeXY4\":{\"x\":2222,\"y\":515},\"nodeXY5\":null,\"nodeXY6\":null,\"nodeLatLon\":null},\"attributes\":{\"localNode\":null,\"disabled\":null,\"enabled\":null,\"data\":null,\"dWidth\":null,\"dElevation\":-10}},{\"delta\":{\"nodeXY1\":null,\"nodeXY2\":null,\"nodeXY3\":null,\"nodeXY4\":{\"x\":2933,\"y\":-82},\"nodeXY5\":null,\"nodeXY6\":null,\"nodeLatLon\":null},\"attributes\":{\"localNode\":null,\"disabled\":null,\"enabled\":null,\"data\":null,\"dWidth\":null,\"dElevation\":-10}},{\"delta\":{\"nodeXY1\":null,\"nodeXY2\":null,\"nodeXY3\":null,\"nodeXY4\":null,\"nodeXY5\":{\"x\":6259,\"y\":-85},\"nodeXY6\":null,\"nodeLatLon\":null},\"attributes\":{\"localNode\":null,\"disabled\":null,\"enabled\":null,\"data\":null,\"dWidth\":null,\"dElevation\":-40}},{\"delta\":{\"nodeXY1\":null,\"nodeXY2\":null,\"nodeXY3\":null,\"nodeXY4\":{\"x\":3416,\"y\":-98},\"nodeXY5\":null,\"nodeXY6\":null,\"nodeLatLon\":null},\"attributes\":{\"localNode\":null,\"disabled\":null,\"enabled\":null,\"data\":null,\"dWidth\":null,\"dElevation\":-20}},{\"delta\":{\"nodeXY1\":null,\"nodeXY2\":null,\"nodeXY3\":null,\"nodeXY4\":null,\"nodeXY5\":{\"x\":6867,\"y\":-39},\"nodeXY6\":null,\"nodeLatLon\":null},\"attributes\":{\"localNode\":null,\"disabled\":null,\"enabled\":null,\"data\":null,\"dWidth\":null,\"dElevation\":-30}},{\"delta\":{\"nodeXY1\":null,\"nodeXY2\":null,\"nodeXY3\":null,\"nodeXY4\":{\"x\":2657,\"y\":-55},\"nodeXY5\":null,\"nodeXY6\":null,\"nodeLatLon\":null},\"attributes\":{\"localNode\":null,\"disabled\":null,\"enabled\":null,\"data\":null,\"dWidth\":null,\"dElevation\":-10}},{\"delta\":{\"nodeXY1\":null,\"nodeXY2\":null,\"nodeXY3\":null,\"nodeXY4\":null,\"nodeXY5\":{\"x\":4259,\"y\":67},\"nodeXY6\":null,\"nodeLatLon\":null},\"attributes\":{\"localNode\":null,\"disabled\":null,\"enabled\":null,\"data\":null,\"dWidth\":null,\"dElevation\":-10}},{\"delta\":{\"nodeXY1\":null,\"nodeXY2\":null,\"nodeXY3\":null,\"nodeXY4\":{\"x\":3481,\"y\":0},\"nodeXY5\":null,\"nodeXY6\":null,\"nodeLatLon\":null},\"attributes\":null}]}},\"connectsTo\":{\"connectsTo\":[{\"connectingLane\":{\"lane\":18,\"maneuver\":{\"maneuverStraightAllowed\":true,\"maneuverNoStoppingAllowed\":false,\"goWithHalt\":false,\"maneuverLeftAllowed\":false,\"maneuverUTurnAllowed\":false,\"maneuverLeftTurnOnRedAllowed\":false,\"reserved1\":false,\"maneuverRightAllowed\":false,\"maneuverLaneChangeAllowed\":false,\"yieldAllwaysRequired\":false,\"maneuverRightTurnOnRedAllowed\":false,\"caution\":false}},\"remoteIntersection\":null,\"signalGroup\":8,\"userClass\":null,\"connectionID\":1}]},\"overlays\":null}]}}]},\"roadSegments\":null,\"dataParameters\":null,\"restrictionList\":null},\"dataType\":\"us.dot.its.jpo.ode.plugin.j2735.J2735MAP\"}}";
  
    @Autowired
    private MapJsonValidator mapJsonValidator;
    
    @Test
    public void testTopology() {

        

        Topology topology = MapTopology.build(kafkaTopicOdeMapJson, kafkaTopicMapGeoJson, mapJsonValidator);
        try (TopologyTestDriver driver = new TopologyTestDriver(topology)) {
            TestInputTopic<Void, String> inputTopic = driver.createInputTopic(
                kafkaTopicOdeMapJson, 
                Serdes.Void().serializer(), 
                Serdes.String().serializer());
            TestOutputTopic<String, MapFeatureCollection> outputTopic = driver.createOutputTopic(
                kafkaTopicMapGeoJson, 
                Serdes.String().deserializer(), 
                JsonSerdes.MapGeoJson().deserializer());
            
            // Send serialized OdeMapJson to OdeMapJson topic
            inputTopic.pipeInput(odeMapJsonString);

            // Check MapGeoJson topic for properly converted message data
            List<KeyValue<String, MapFeatureCollection>> mapGeoJsonResults = outputTopic.readKeyValuesToList();
            assertEquals(mapGeoJsonResults.size(), 1);

            KeyValue<String, MapFeatureCollection> mapGeoJson = mapGeoJsonResults.get(0);
            assertNotNull(mapGeoJson.key);
            assertEquals("172.19.0.1:12110", mapGeoJson.key);
            assertNotNull(mapGeoJson.value);
            assertEquals(2, mapGeoJson.value.getFeatures().length);
            assertEquals(1, mapGeoJson.value.getFeatures()[0].getProperties().getIngressApproach());
        }
    }
}
