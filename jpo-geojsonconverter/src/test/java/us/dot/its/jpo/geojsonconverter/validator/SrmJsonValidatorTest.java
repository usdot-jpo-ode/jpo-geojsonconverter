package us.dot.its.jpo.geojsonconverter.validator;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.Resource;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertFalse;

@SpringBootTest({
        "valid.srm.json=classpath:json/valid.srm.json",
        "invalid.srm.json=classpath:json/invalid.srm.json"})
@RunWith(SpringRunner.class)
@ActiveProfiles("test")
public class SrmJsonValidatorTest extends AbstractJsonValidatorTest {

    @Autowired
    private SrmJsonValidator jsonValidator;

    @Test
    public void jsonValidatorLoaded() {
        assertThat(jsonValidator, notNullValue());
    }

    @Test
    public void jsonSchemaResourceLoaded() {
        testJsonSchemaResourceLoaded(jsonValidator);
    }

    @Test
    public void jsonSchemaLoaded() throws IOException {
        testJsonSchemaLoaded(jsonValidator);
    }

    @Test
    public void validJsonTest_String() {
        testJson(jsonValidator, validJsonResource, true);
    }

    @Test
    public void invalidJsonTest_String() {
        testJson(jsonValidator, invalidJsonResource, false);
    }

    @Test
    public void validJsonTest_ByteArray() {
        testJson_ByteArray(jsonValidator, validJsonResource, true);
    }

    @Test
    public void invalidJsonTest_ByteArray() {
        testJson_ByteArray(jsonValidator, invalidJsonResource, false);
    }

    @Test
    public void testException() {
        SrmJsonValidator badValidator = new SrmJsonValidator(null);
        var result = badValidator.validate("invalid");
        assertFalse("An exception should have happened", result.isValid());
    }


    @Value("${valid.srm.json}")
    private Resource validJsonResource;

    @Value("${invalid.srm.json}")
    private Resource invalidJsonResource;
}
