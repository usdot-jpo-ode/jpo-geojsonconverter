package us.dot.its.jpo.geojsonconverter.validator;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertTrue;

import java.nio.charset.StandardCharsets;

import org.apache.commons.io.IOUtils;
import org.springframework.core.io.Resource;

/**
 * Base class for Json Validator tests
 */
public abstract class BaseJsonValidatorTest {

    protected void testJsonSchemaResourceLoaded(AbstractJsonValidator validator) {
        var resource = validator.getJsonSchemaResource();
        assertThat(resource, notNullValue());
        assertTrue("Resource does not exist", resource.exists());
    }

    protected void testJsonSchemaLoaded(AbstractJsonValidator validator) {
        var jsonSchema = validator.getJsonSchema();
        assertThat(jsonSchema, notNullValue());
    }

    protected void testJson(AbstractJsonValidator validator, Resource resource, boolean expectValid) {
        assertThat("Couldn't get test json resource", resource, notNullValue());
        var json = getTestJson(resource);
        JsonValidatorResult result = validator.validate(json);
        assertThat(result, notNullValue());
        
        assertThat(String.format("Validation result:%n%s", result.describeResults()), result.isValid(), equalTo(expectValid));
        
    }

    protected void testJson_ByteArray(AbstractJsonValidator validator, Resource resource, boolean expectValid) {
        assertThat("Couldn't get test json resource", resource, notNullValue());
        byte[] jsonBytes = getTestJson_ByteArray(resource);
        JsonValidatorResult result = validator.validate(jsonBytes);
        assertThat(result, notNullValue());
        
        assertThat(String.format("Validation result:%n%s", result.describeResults()), result.isValid(), equalTo(expectValid));
    }
    
    protected String getTestJson(Resource jsonResource) {
        try (var is = jsonResource.getInputStream()) {
            return IOUtils.toString(is, StandardCharsets.UTF_8.name());
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    protected byte[] getTestJson_ByteArray(Resource jsonResource) {
        try (var is = jsonResource.getInputStream()) {
            return IOUtils.toByteArray(is);
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }
}
