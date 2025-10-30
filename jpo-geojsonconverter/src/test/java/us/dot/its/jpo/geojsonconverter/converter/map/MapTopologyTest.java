package us.dot.its.jpo.geojsonconverter.converter.map;

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
import org.junit.jupiter.api.Disabled;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import us.dot.its.jpo.geojsonconverter.partitioner.RsuIntersectionKey;
import us.dot.its.jpo.geojsonconverter.pojos.GeometryOutputMode;
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
    String odeMapJsonString;

    @Autowired
    private MapJsonValidator mapJsonValidator;

    @Before
    public void setup() throws IOException {
        odeMapJsonString = new String(Files.readAllBytes(Paths.get("src/test/resources/json/valid.map.json")));
    }

    @Test
    @Disabled
    public void testTopologyGeoJson() {
        Topology topology = MapTopology.build(kafkaTopicOdeMapJson, kafkaTopicMapGeoJson, kafkaTopicMapWKT,
                mapJsonValidator, GeometryOutputMode.GEOJSON_ONLY);
        try (TopologyTestDriver driver = new TopologyTestDriver(topology)) {
            TestInputTopic<Void, String> inputTopic = driver.createInputTopic(kafkaTopicOdeMapJson,
                    Serdes.Void().serializer(), Serdes.String().serializer());
            TestOutputTopic<RsuIntersectionKey, ProcessedMap<LineString>> outputTopic =
                    driver.createOutputTopic(kafkaTopicMapGeoJson, JsonSerdes.RsuIntersectionKey().deserializer(),
                            JsonSerdes.ProcessedMapGeoJson().deserializer());

            // Send serialized OdeMapJson to OdeMapJson topic
            inputTopic.pipeInput(odeMapJsonString);

            // Check MapGeoJson topic for properly converted message data
            List<KeyValue<RsuIntersectionKey, ProcessedMap<LineString>>> mapGeoJsonResults =
                    outputTopic.readKeyValuesToList();
            assertEquals(mapGeoJsonResults.size(), 1);

            KeyValue<RsuIntersectionKey, ProcessedMap<LineString>> mapGeoJson = mapGeoJsonResults.get(0);
            assertNotNull(mapGeoJson.key);
            assertEquals("172.18.0.1", mapGeoJson.key.getRsuId());
            assertEquals(12112, mapGeoJson.key.getIntersectionId());
            assertNotNull(mapGeoJson.value);
            assertEquals(27, mapGeoJson.value.getMapFeatureCollection().getFeatures().length);
            assertEquals(2,
                    mapGeoJson.value.getMapFeatureCollection().getFeatures()[0].getProperties().getIngressApproach());
            assertEquals(false, mapGeoJson.value.getProperties().getCti4501Conformant());
            assertEquals(5, mapGeoJson.value.getProperties().getValidationMessages().size());
        }
    }

    @Test
    @Disabled
    public void testTopologyWKT() {
        Topology topology = MapTopology.build(kafkaTopicOdeMapJson, kafkaTopicMapGeoJson, kafkaTopicMapWKT,
                mapJsonValidator, GeometryOutputMode.WKT);
        try (TopologyTestDriver driver = new TopologyTestDriver(topology)) {
            TestInputTopic<Void, String> inputOdeTopic = driver.createInputTopic(kafkaTopicOdeMapJson,
                    Serdes.Void().serializer(), Serdes.String().serializer());
            TestOutputTopic<RsuIntersectionKey, ProcessedMap<String>> outputTopic =
                    driver.createOutputTopic(kafkaTopicMapWKT, JsonSerdes.RsuIntersectionKey().deserializer(),
                            JsonSerdes.ProcessedMapWKT().deserializer());

            // Send serialized OdeMapJson to OdeMapJson topic
            inputOdeTopic.pipeInput(odeMapJsonString);

            // Check MapWKT topic for properly converted message data
            List<KeyValue<RsuIntersectionKey, ProcessedMap<String>>> mapWKTResults = outputTopic.readKeyValuesToList();
            assertEquals(mapWKTResults.size(), 1);

            KeyValue<RsuIntersectionKey, ProcessedMap<String>> mapWKT = mapWKTResults.get(0);
            assertNotNull(mapWKT.key);
            assertEquals("172.18.0.1", mapWKT.key.getRsuId());
            assertEquals(12112, mapWKT.key.getIntersectionId());
            assertNotNull(mapWKT.value);
            assertEquals(27, mapWKT.value.getMapFeatureCollection().getFeatures().length);
            assertEquals(
                    "LINESTRING (-105.08731667516962 39.58083233014103, -105.08774523232292 39.581549630858326, -105.08788477758141 39.581774541083234, -105.08809870874727 39.58214795145665, -105.08860236054305 39.58298918229788, -105.08911197406428 39.58384553315423, -105.08961085029375 39.584704764013466)",
                    mapWKT.value.getMapFeatureCollection().getFeatures()[0].getGeometry());
        }
    }

    @Test
    public void testTopologyFailureGeoJson() {
        Topology topology = MapTopology.build(kafkaTopicOdeMapJson, kafkaTopicMapGeoJson, kafkaTopicMapWKT,
                mapJsonValidator, GeometryOutputMode.GEOJSON_ONLY);
        try (TopologyTestDriver driver = new TopologyTestDriver(topology)) {
            TestInputTopic<Void, String> inputTopic = driver.createInputTopic(kafkaTopicOdeMapJson,
                    Serdes.Void().serializer(), Serdes.String().serializer());
            TestOutputTopic<RsuIntersectionKey, ProcessedMap<LineString>> outputTopic =
                    driver.createOutputTopic(kafkaTopicMapGeoJson, JsonSerdes.RsuIntersectionKey().deserializer(),
                            JsonSerdes.ProcessedMapGeoJson().deserializer());

            // Send serialized OdeMapJson to OdeMapJson topic
            inputTopic.pipeInput("{");

            // Check MapGeoJson topic for properly converted message data
            List<KeyValue<RsuIntersectionKey, ProcessedMap<LineString>>> mapGeoJsonResults =
                    outputTopic.readKeyValuesToList();
            assertEquals(mapGeoJsonResults.size(), 1);

            KeyValue<RsuIntersectionKey, ProcessedMap<LineString>> mapGeoJson = mapGeoJsonResults.get(0);
            assertNotNull(mapGeoJson.key);
            assertEquals("ERROR", mapGeoJson.key.getRsuId());
            assertNotNull(mapGeoJson.value);
            assertEquals(1, mapGeoJson.value.getProperties().getValidationMessages().size());
        }
    }
}
