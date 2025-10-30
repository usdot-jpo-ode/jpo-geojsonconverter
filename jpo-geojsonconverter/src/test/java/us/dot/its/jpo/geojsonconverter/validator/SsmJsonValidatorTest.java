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
        "valid.ssm.json=classpath:json/valid.ssm.json",
        "invalid.ssm.json=classpath:json/invalid.ssm.json"})
@RunWith(SpringRunner.class)
@ActiveProfiles("test")
public class SsmJsonValidatorTest extends AbstractJsonValidatorTest {

    @Autowired
    private SsmJsonValidator jsonValidator;

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
        SsmJsonValidator badValidator = new SsmJsonValidator(null);
        var result = badValidator.validate("invalid");
        assertFalse("An exception should have happened", result.isValid());
    }

    @Value("${valid.ssm.json}")
    private Resource validJsonResource;

    @Value("${invalid.ssm.json}")
    private Resource invalidJsonResource;
}
