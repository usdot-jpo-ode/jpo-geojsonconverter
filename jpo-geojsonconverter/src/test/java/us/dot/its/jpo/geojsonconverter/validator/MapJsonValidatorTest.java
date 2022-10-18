package us.dot.its.jpo.geojsonconverter.validator;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

import java.nio.charset.StandardCharsets;

import org.apache.commons.io.IOUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.Resource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.beans.factory.annotation.Value;


@SpringBootTest( { 
    "valid.map.json=classpath:json/valid.map.json",
    "invalid.map.json=classpath:json/invalid.map.json" })
@RunWith(SpringRunner.class)    
public class MapJsonValidatorTest  {


    @Autowired
    private MapJsonValidator mapJsonValidator;

    
    
    @Test
    public void mapJsonValidatorLoaded() {
        assertThat(mapJsonValidator, notNullValue());
    }

    @Test
    public void jsonSchemaResourceLoaded() {
        var resource = mapJsonValidator.getJsonSchemaResource();
        assertThat(resource, notNullValue());
        assertTrue("Resource does not exist", resource.exists());
    }

    @Test
    public void jsonSchemaLoaded() {
        var jsonSchema = mapJsonValidator.getJsonSchema();
        assertThat(jsonSchema, notNullValue());
    }

    
    @Test
    public void validMapJsonTest_String() {
        testJson(validMapJsonResource, true);
    }

    @Test
    public void invalidMapJsonTest_String() {
        testJson(invalidMapJsonResource, false);
    }

    @Test
    public void validMapJsonTest_ByteArray() {
        testJson_ByteArray(validMapJsonResource, true);
    }

    @Test
    public void invalidMapJsonTest_ByteArray() {
        testJson_ByteArray(invalidMapJsonResource, false);
    }

    private void testJson(Resource resource, boolean expectValid) {
        assertThat("Couldn't get test json resource", resource, notNullValue());
        var json = getTestJson(resource);
        JsonValidatorResult result = mapJsonValidator.validate(json);
        assertThat(result, notNullValue());
        
        assertThat(String.format("Validation result:%n%s", result.describeResults()), result.isValid(), equalTo(expectValid));
        
    }

    private void testJson_ByteArray(Resource resource, boolean expectValid) {
        assertThat("Couldn't get test json resource", resource, notNullValue());
        byte[] jsonBytes = getTestJson_ByteArray(resource);
        JsonValidatorResult result = mapJsonValidator.validate(jsonBytes);
        assertThat(result, notNullValue());
        
        assertThat(String.format("Validation result:%n%s", result.describeResults()), result.isValid(), equalTo(expectValid));
    }

    @Value("${valid.map.json}")
    private Resource validMapJsonResource;

    @Value("${invalid.map.json}")
    private Resource invalidMapJsonResource;

    private String getTestJson(Resource jsonResource) {
        try (var is = jsonResource.getInputStream()) {
            return IOUtils.toString(is, StandardCharsets.UTF_8.name());
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    private byte[] getTestJson_ByteArray(Resource jsonResource) {
        try (var is = jsonResource.getInputStream()) {
            return IOUtils.toByteArray(is);
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    
    
}
