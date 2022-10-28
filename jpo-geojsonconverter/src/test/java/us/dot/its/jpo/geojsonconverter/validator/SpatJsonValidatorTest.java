package us.dot.its.jpo.geojsonconverter.validator;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;


import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.Resource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.beans.factory.annotation.Value;

@SpringBootTest({
    "valid.spat.json=classpath:json/valid.spat.json",
    "invalid.spat.json=classpath:json/invalid.spat.json" })
@RunWith(SpringRunner.class)
public class SpatJsonValidatorTest extends BaseJsonValidatorTest {
    
    @Autowired
    private SpatJsonValidator spatJsonValidator;

    @Test
    public void spatJsonValidatorLoaded() {
        assertThat(spatJsonValidator, notNullValue());
    }

    @Test
    public void jsonSchemaResourceLoaded() {
        testJsonSchemaResourceLoaded(spatJsonValidator);
    }

    @Test
    public void jsonSchemaLoaded() {
        testJsonSchemaLoaded(spatJsonValidator);
    }


    @Test
    public void validSpatJsonTest_String() {
        testJson(spatJsonValidator, validSpatJsonResource, true);
    }

    @Test
    public void invalidSpatJsonTest_String() {
        testJson(spatJsonValidator, invalidSpatJsonResource, false);
    }

    @Test
    public void validSpatJsonTest_ByteArray() {
        testJson_ByteArray(spatJsonValidator, validSpatJsonResource, true);
    }

    @Test
    public void invalidSpatJsonTest_ByteArray() {
        testJson_ByteArray(spatJsonValidator, invalidSpatJsonResource, false);
    }


    @Value("${valid.spat.json}")
    private Resource validSpatJsonResource;

    @Value("${invalid.spat.json}")
    private Resource invalidSpatJsonResource;

}
