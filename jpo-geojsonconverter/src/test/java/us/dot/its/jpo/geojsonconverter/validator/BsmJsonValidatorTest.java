package us.dot.its.jpo.geojsonconverter.validator;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertFalse;

import java.io.IOException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.Resource;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.beans.factory.annotation.Value;

@SpringBootTest({
    "valid.bsm.json=classpath:json/valid.bsm.json",
    "invalid.bsm.json=classpath:json/invalid.bsm.json" })
@RunWith(SpringRunner.class)
@ActiveProfiles("test")
public class BsmJsonValidatorTest extends AbstractJsonValidatorTest {

    @Autowired
    private BsmJsonValidator bsmJsonValidator;

    @Test
    public void bsmJsonValidatorLoaded() {
        assertThat(bsmJsonValidator, notNullValue());
    }

    @Test
    public void jsonSchemaResourceLoaded() {
        testJsonSchemaResourceLoaded(bsmJsonValidator);
    }

    @Test
    public void jsonSchemaLoaded() throws IOException {
        testJsonSchemaLoaded(bsmJsonValidator);
    }


    @Test
    public void validBsmJsonTest_String() {
        testJson(bsmJsonValidator, validBsmJsonResource, true);
    }

    @Test
    public void invalidBsmJsonTest_String() {
        testJson(bsmJsonValidator, invalidBsmJsonResource, false);
    }

    @Test
    public void validBsmJsonTest_ByteArray() {
        testJson_ByteArray(bsmJsonValidator, validBsmJsonResource, true);
    }

    @Test
    public void invalidBsmJsonTest_ByteArray() {
        testJson_ByteArray(bsmJsonValidator, invalidBsmJsonResource, false);
    }

    @Test
    public void testException() {
        SpatJsonValidator badValidator = new SpatJsonValidator(null);
        var result = badValidator.validate("invalid");
        assertFalse("An exception should have happened", result.isValid());
    }

    @Value("${valid.bsm.json}")
    private Resource validBsmJsonResource;

    @Value("${invalid.bsm.json}")
    private Resource invalidBsmJsonResource;
}
