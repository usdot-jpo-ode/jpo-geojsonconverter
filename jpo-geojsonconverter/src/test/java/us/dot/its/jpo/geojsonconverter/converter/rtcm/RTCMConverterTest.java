package us.dot.its.jpo.geojsonconverter.converter.rtcm;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameter;
import org.junit.runners.Parameterized.Parameters;
import us.dot.its.jpo.asn.j2735.r2024.RTCMcorrections.RTCMcorrectionsMessageFrame;
import us.dot.its.jpo.geojsonconverter.pojos.ProcessedValidationMessage;
import us.dot.its.jpo.geojsonconverter.pojos.geojson.rtcm.ProcessedRTCM;
import us.dot.its.jpo.geojsonconverter.pojos.geojson.rtcm.RTCMProperties;

import java.util.Arrays;
import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

@Slf4j
@RunWith(Parameterized.class)
public class RTCMConverterTest {

    private final static ObjectMapper mapper = new ObjectMapper();

    @Parameter(0)
    public String rtcmJson;

    @Parameter(1)
    public boolean expectCti4501Conformant;

    @Parameter(2)
    public String expectValidationMessageIncludes;

    @Parameter(3)
    public Long expectUtcTime;

    @Test
    public void testProcessRtcm() throws JsonProcessingException {
        RTCMDecoder decoder = new RTCMDecoder(false);
        RTCMConverter converter = new RTCMConverter(decoder);
        RTCMcorrectionsMessageFrame messageFrame = mapper.readValue(rtcmJson, RTCMcorrectionsMessageFrame.class);
        String str = mapper.writeValueAsString(messageFrame);
        log.info(str);
        ProcessedRTCM processedRtcm = converter.processRTCM(messageFrame);
        assertThat(processedRtcm, notNullValue());
        RTCMProperties properties = processedRtcm.getProperties();
        assertThat(properties, notNullValue());
        assertThat(properties.getMsgCnt(), equalTo(82));

        log.info(mapper.writeValueAsString(processedRtcm));
        if (expectCti4501Conformant) {
            assertThat(properties.getRev(), equalTo("rtcmRev3"));
            assertThat(properties.getValidationMessages(), hasSize(equalTo(0)));
            assertThat(properties.isCti4501Conformant(), equalTo(true));
        } else {
            assertThat(properties.getValidationMessages(), hasSize(greaterThanOrEqualTo(1)));
            Set<String> messages = properties.getValidationMessages().stream().map(ProcessedValidationMessage::getMessage).collect(Collectors.toSet());
            assertThat(messages, hasItems(containsString(expectValidationMessageIncludes)));
            assertThat(properties.isCti4501Conformant(), equalTo(false));
        }
        if (expectUtcTime != null) {
            assertThat(properties.getUtcTime(), equalTo(expectUtcTime));
        }
    }

    @Parameters
    public static Collection<Object[]> params() {
        return Arrays.asList(new Object[][] {
                { RTCM , false, "DDateTime", null },
                { RTCM_CTI4501_VALID, true, null,  1753482274168L },
                { RTCM_REV2_INVALID, false, "DE_RTCM_Revision", null }
        });
    };


    public static final String RTCM = """
        {
            "messageId": 28,
            "value": {
                "RTCMcorrections": {
                    "msgCnt": 82,
                    "rev": "rtcmRev3",
                    "anchorPoint": {
                        "long": -1115094349,
                        "lat": 406603360,
                        "elevation": 20630
                    },
                    "msgs": [
                        "D300133ED980037BDD1A80C6358121DD4E499FFC6712E91F0D",
                        "D3003E409980144144564E554C4C414E54454E4E4120204E4F4E4500000D5452494D424C4520414C4C4F59144E617620352E3434202F20426F6F7420352E3434008C65F9"
                    ]
                }
            }
        }
        """;

    public static final String RTCM_CTI4501_VALID = """
        {
            "messageId": 28,
            "value": {
                "RTCMcorrections": {
                    "msgCnt": 82,
                    "rev": "rtcmRev3",
                    "anchorPoint": {
                        "utcTime": {
                            "year": 2025,
                            "month": 7,
                            "day": 25,
                            "hour": 16,
                            "minute": 24,
                            "second": 34168,
                            "offset": -360
                        },
                        "long": -1115094349,
                        "lat": 406603360,
                        "elevation": 20630
                    },
                    "msgs": [
                        "D300133ED980037BDD1A80C6358121DD4E499FFC6712E91F0D",
                        "D3003E409980144144564E554C4C414E54454E4E4120204E4F4E4500000D5452494D424C4520414C4C4F59144E617620352E3434202F20426F6F7420352E3434008C65F9"
                    ]
                }
            }
        }
        """;

    public static final String RTCM_REV2_INVALID = """
            {
              "messageId": 28,
              "value": {
                "RTCMcorrections": {
                  "msgCnt": 82,
                  "rev": "rtcmRev2",
                  "timeStamp": 527040,
                  "msgs": [
                    "66300D0A597E7D7C5A7963686F7D4B4278774F4068414565405145404E6A73775F486841406865534164645A40704779467F7E44405E72517B4F7E727F7F4E6E5342544F47404043517F405145406B4D4D47507E437C7F7F7A59467C7C60466E5B75664B406460657F637D624E7F634E60536670715F4F4740604968734352654040594253595F777E7F70584F475068747C5F767159567E7C5C79634C4A5E546F564B7C675D6E776559627C7C7A465C7D71635577784F404060707F534F7C7F714A665D434360465C55775E6A48476C78767F7F7F646F42757F5F4040505A6C7F7F7B5F564C704043534C6A6F5263534F7E7F530D0A"
                  ]
                }
              }
            }
            """;

}
