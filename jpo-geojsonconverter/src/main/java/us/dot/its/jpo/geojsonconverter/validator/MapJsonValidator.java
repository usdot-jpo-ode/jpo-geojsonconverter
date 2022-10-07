package us.dot.its.jpo.geojsonconverter.validator;

import java.util.Set;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.networknt.schema.JsonSchema;
import com.networknt.schema.JsonSchemaFactory;
import com.networknt.schema.SpecVersionDetector;
import com.networknt.schema.ValidationMessage;


/**
 * Class for a validator to validate a JSON document against a
 * schema and report all errors
 */
@Service
public class MapJsonValidator  {

    /**
     * @param jsonSchemaResource The json schema file in resources/schemas.  Injected by Spring DI.
     */
    public MapJsonValidator(@Value("${schema.map}") Resource jsonSchemaResource) {
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
    
}
