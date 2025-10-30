package us.dot.its.jpo.geojsonconverter.converter.srm;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import us.dot.its.jpo.asn.j2735.r2024.SignalRequestMessage.SignalRequestMessageMessageFrame;
import us.dot.its.jpo.geojsonconverter.pojos.geojson.srm.ProcessedSignalRequest;
import us.dot.its.jpo.geojsonconverter.pojos.geojson.srm.ProcessedSrm;
import us.dot.its.jpo.geojsonconverter.pojos.geojson.srm.SrmProperties;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.runners.Parameterized.Parameters;
import static org.junit.runners.Parameterized.Parameter;
import static us.dot.its.jpo.geojsonconverter.TestResourceUtil.loadResource;

@Slf4j
@RunWith(Parameterized.class)
public class SrmConverterTest {

    private final static ObjectMapper mapper = new ObjectMapper();

    @Parameter(0)
    public String srmJson;

    @Parameter(1)
    public int expectNumberOfRequests;

    @Test
    public void testProcessSrm() throws JsonProcessingException {
        SrmConverter srmConverter = new SrmConverter();
        SignalRequestMessageMessageFrame messageFrame =
                mapper.readValue(srmJson, SignalRequestMessageMessageFrame.class);
        ProcessedSrm processedSrm = srmConverter.processSrm(messageFrame);
        assertThat(processedSrm, notNullValue());
        SrmProperties props = processedSrm.getProperties();
        assertThat(props, hasProperty("requests", notNullValue()));
        List<ProcessedSignalRequest> requests = props.getRequests();
        assertThat(requests, hasSize(expectNumberOfRequests));
    }

    @Parameters
    public static Collection<Object[]> params() throws IOException {
        final String srm1Lane = loadResource("classpath:json/srm.message-frame.json");
        final String srm2Lanes = loadResource("classpath:json/srm.message-frame.2lanes.json");
        final String srm4Lanes = loadResource("classpath:json/srm.message-frame.4lanes.json");
        return Arrays.asList(new Object[][] {
                { srm1Lane, 1 },
                { srm2Lanes, 2},
                { srm4Lanes, 4}
        });
    }


}
