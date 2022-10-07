package us.dot.its.jpo.geojsonconverter.validator;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

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

    private String jsonSchema;
    private final Resource jsonSchemaResource;

    /**
     * The resource where the json schema file is
     * 
     * @return Resource
     */
    public Resource getJsonSchemaResource() {
        return jsonSchemaResource;
    }

    /**
     * @return The json schema to validate against
     */
    public String getJsonSchema() {
        if (jsonSchema == null) {
            // Load the schema lazily
            try (var inputStream = jsonSchemaResource.getInputStream()) {
                jsonSchema = IOUtils.toString(inputStream, StandardCharsets.UTF_8.name());
            } catch (IOException ioe) {
                throw new RuntimeException(String.format("Failed to load json schema from resource '%'", jsonSchemaResource), ioe);
            }
        }
        return jsonSchema;
    }

 

    // public String validate(String json) {

    // }
    
}
