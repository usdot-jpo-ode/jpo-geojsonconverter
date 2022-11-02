package us.dot.its.jpo.geojsonconverter.validator;

import java.io.IOException;
import java.util.Set;

import org.springframework.core.io.Resource;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.networknt.schema.JsonSchema;
import com.networknt.schema.JsonSchemaFactory;
import com.networknt.schema.ValidationMessage;
import com.networknt.schema.SpecVersion;

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
    protected AbstractJsonValidator(Resource jsonSchemaResource) {
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
        } catch (IOException e) {
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
