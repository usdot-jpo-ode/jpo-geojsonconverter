package us.dot.its.jpo.geojsonconverter.validator;

import java.io.IOException;
import java.util.Set;

import org.springframework.core.io.Resource;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.networknt.schema.JsonSchema;
import com.networknt.schema.JsonSchemaFactory;
import com.networknt.schema.SpecVersionDetector;
import com.networknt.schema.ValidationMessage;

/**
 * Class for a validator to validate a JSON document against a
 * schema and report all errors.
 * 
 * Annotate implementing classes with <code>@Service</code> to inject with Spring DI.
 */
public abstract class AbstractJsonValidator {

     /**
     * @param jsonSchemaResource The json schema file in resources/schemas.  
     * Add an <code>@Value("${schema.resourceName}")</code> annotation to this argument
     * in implementing classes to specify the schema location.
     */
    public AbstractJsonValidator(Resource jsonSchemaResource) {
        this.jsonSchemaResource = jsonSchemaResource;
    }

    private final ObjectMapper mapper = new ObjectMapper();
    private final Resource jsonSchemaResource;
    private JsonSchema jsonSchema;

    /**
     * The resource where the json schema file is
     * 
     * @return Resource
     */
    public Resource getJsonSchemaResource() {
        return jsonSchemaResource;
    }


    public JsonSchema getJsonSchema() {
        if (jsonSchema == null) {
            try (var inputStream = jsonSchemaResource.getInputStream()) {
                JsonNode schemaNode = mapper.readTree(inputStream);
                JsonSchemaFactory factory = JsonSchemaFactory.getInstance(SpecVersionDetector.detect(schemaNode));
                jsonSchema = factory.getSchema(schemaNode);
            } catch (Exception ex) {
                throw new RuntimeException(String.format("Failed to load json schema from resource '%s'", jsonSchemaResource), ex);
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
        } catch (JsonProcessingException e) {
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
        } catch (IOException e) {
            result.addException(e);
        }
        return result;
    }
    
}
