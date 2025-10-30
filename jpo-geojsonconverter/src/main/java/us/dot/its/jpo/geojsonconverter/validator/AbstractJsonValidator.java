package us.dot.its.jpo.geojsonconverter.validator;

import java.io.IOException;
import java.util.Set;

import lombok.Getter;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.networknt.schema.JsonSchema;
import com.networknt.schema.JsonSchemaFactory;
import com.networknt.schema.ValidationMessage;
import com.networknt.schema.SpecVersion;
import org.springframework.core.io.ResourceLoader;

/**
 * Class for a validator to validate a JSON document against a
 * schema and report all errors.
 */
public abstract class AbstractJsonValidator {

     /**
     * @param schemaLocation The classpath to the schema location
     * For example: ""classpath:schemas/srm.schema.json"
     */
    protected AbstractJsonValidator(String schemaLocation) {
        this.jsonSchemaResource = loadJsonSchemaResource(schemaLocation);
    }

    private final ObjectMapper mapper = new ObjectMapper();
    @Getter
    private final Resource jsonSchemaResource;
    private JsonSchema jsonSchema;


    public JsonSchema getJsonSchema() throws IOException {
        if (jsonSchema == null) {
            try (var inputStream = jsonSchemaResource.getInputStream()) {
                JsonNode schemaNode = mapper.readTree(inputStream);

                // Use Json schema version 2019-09 because the 2020-12 implementation in networknt seems buggy as of now.
                JsonSchemaFactory factory = JsonSchemaFactory.getInstance(SpecVersion.VersionFlag.V201909);

                jsonSchema = factory.getSchema(schemaNode);
            } 
        }
        return jsonSchema;
    }

    public JsonValidatorResult validate(String json) {
        var result = new JsonValidatorResult();
        try {
            JsonNode node = mapper.readTree(json);
            Set<ValidationMessage> validationMessages = getJsonSchema().validate(node);
            result.addValidationMessages(validationMessages);
        } catch (Exception e) {
            result.addException(e);
        }
        return result;
    }

    public JsonValidatorResult validate(byte[] jsonBytes) {
        var result = new JsonValidatorResult();
        try { 
            JsonNode node = mapper.readTree(jsonBytes);
            Set<ValidationMessage> validationMessages = getJsonSchema().validate(node);
            result.addValidationMessages(validationMessages);
        } catch (Exception e) {
            result.addException(e);

        }
        return result;
    }

    private Resource loadJsonSchemaResource(String schemaLocation) {
        ResourceLoader resourceLoader = new DefaultResourceLoader();
        if (schemaLocation != null) {
            return resourceLoader.getResource(schemaLocation);
        }
        return null;
    }
    
}
