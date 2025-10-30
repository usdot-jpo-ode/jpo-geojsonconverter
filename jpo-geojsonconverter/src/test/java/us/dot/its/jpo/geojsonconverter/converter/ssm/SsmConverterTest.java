package us.dot.its.jpo.geojsonconverter.converter.ssm;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import us.dot.its.jpo.asn.j2735.r2024.SignalStatusMessage.SignalStatusMessageMessageFrame;
import us.dot.its.jpo.geojsonconverter.pojos.ssm.ProcessedSsm;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Collection;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.runners.Parameterized.Parameters;
import static org.junit.runners.Parameterized.Parameter;
import static us.dot.its.jpo.geojsonconverter.TestResourceUtil.loadResource;

@Slf4j
@RunWith(Parameterized.class)
public class SsmConverterTest {

    private final static ObjectMapper mapper = new ObjectMapper();

    @Parameter(0)
    public String ssmJson;

    @Parameter(1)
    public int expectNumberOfRequests;

    @Test
    public void testProcessSsm() throws JsonProcessingException {
        SsmConverter ssmConverter = new SsmConverter();
        SignalStatusMessageMessageFrame messageFrame =
                mapper.readValue(ssmJson, SignalStatusMessageMessageFrame.class);
        ProcessedSsm processedSsm = ssmConverter.processSsm(messageFrame);
        assertThat(processedSsm, notNullValue());
        assertThat(processedSsm, hasProperty("statusList", notNullValue()));
        assertThat(processedSsm.getStatusList(), hasSize(equalTo(expectNumberOfRequests)));
    }

    @Parameters
    public static Collection<Object[]> params() throws IOException {
        final String ssmJson = loadResource("classpath:json/ssm.message-frame.json");
        final String ssmJsonMulti = loadResource("classpath:json/ssm.message-frame.multi.json");
        return Arrays.asList(new Object[][] {
                { ssmJson, 1},
                { ssmJsonMulti, 2}
        });
    }


}
