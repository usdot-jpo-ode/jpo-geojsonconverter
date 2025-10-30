package us.dot.its.jpo.geojsonconverter.converter.ssm;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.serialization.StringSerializer;
import org.apache.kafka.common.serialization.VoidSerializer;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.Topology;
import org.apache.kafka.streams.TopologyTestDriver;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameter;
import org.junit.runners.Parameterized.Parameters;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import us.dot.its.jpo.geojsonconverter.partitioner.RsuVehicleIdKey;
import us.dot.its.jpo.geojsonconverter.pojos.ssm.ProcessedSsm;
import us.dot.its.jpo.geojsonconverter.serialization.deserializers.JsonDeserializer;
import us.dot.its.jpo.geojsonconverter.validator.SsmJsonValidator;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

@Slf4j
@RunWith(Parameterized.class)
public class SsmTopologyTest {
    final String inputTopicName = "topic.OdeSsmJson";
    final String outputTopicName = "topic.ProcessedSsm";

    @Parameter(0)
    public String inputJson;

    @Parameter(1)
    public int expectNumberOfRequests;

    @Parameter(2)
    public boolean expectValid;

    @Parameters
    public static Collection<Object[]> params() throws IOException {
        return Arrays.asList(new Object[][] {
                { loadResource("json/valid.ssm.json"), 1, true },
                { loadResource("json/valid.ssm-multi.json"), 2, true },
                { loadResource("json/invalid.ssm.json"), 1, false },
        });
    }

    @Test
    public void topologyTest() {
        SsmJsonValidator validator = new SsmJsonValidator("classpath:schemas/ssm.schema.json");
        SsmConverter converter = new SsmConverter();
        Topology topology = SsmTopology.build(inputTopicName, outputTopicName, validator, converter);
        try (var driver = new TopologyTestDriver(topology)) {
            var inputTopic = driver.createInputTopic(inputTopicName, new VoidSerializer(), new StringSerializer());
            var outputTopic = driver.createOutputTopic(outputTopicName,
                    new JsonDeserializer<>(RsuVehicleIdKey.class), new JsonDeserializer<>(ProcessedSsm.class));

            inputTopic.pipeInput(inputJson);

            List<KeyValue<RsuVehicleIdKey, ProcessedSsm>> results = outputTopic.readKeyValuesToList();

            assertThat(results, hasSize(1));

            KeyValue<RsuVehicleIdKey, ProcessedSsm> result = results.getFirst();
            RsuVehicleIdKey key = result.key;
            assertThat(key, notNullValue());
            assertThat(key.getRsuId(), equalTo("172.18.0.1"));
            ProcessedSsm processedSsm = result.value;
            assertThat(processedSsm, notNullValue());
            assertThat(processedSsm.getAsn1(), notNullValue());
            assertThat(processedSsm.getOdeReceivedAt(), notNullValue());
            assertThat(processedSsm.getMessageType(), equalTo("SSM"));
            assertThat(processedSsm.getStatusList(), hasSize(greaterThan(0)));
            if (expectValid) {
                assertThat("expected valid message but has validation messages",
                        processedSsm.getValidationMessages(), hasSize(equalTo(0)));
            } else {
                assertThat("expected invalid message but has no validation messages",
                        processedSsm.getValidationMessages(), hasSize(greaterThan(0)));
            }
        }
    }

    private static String loadResource(String path) throws IOException {
        Resource resource = getResource(path);
        return readResource(resource);
    }

    private static Resource getResource(String path) {
        ResourceLoader resourceLoader = new DefaultResourceLoader();
        return resourceLoader.getResource("classpath:" + path);
    }

    private static String readResource(Resource resource) throws IOException {
        byte[] bytes = resource.getInputStream().readAllBytes();
        return  new String(bytes, StandardCharsets.UTF_8);
    }
}
