package us.dot.its.jpo.geojsonconverter.validator;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

/**
 * Class for a validator to validate a JSON document against a
 * schema and report all errors
 */
@Component
public class MapJsonValidator  {

    /**
     * Constructor that loads the schema from a resource file.
     * 
     * @param jsonSchemaResource Name of a json schema file in resources/schemas.
     */
    public MapJsonValidator(@Value("${schema.map}") Resource jsonSchemaResource) {
        this.jsonSchemaResource = jsonSchemaResource;
        // if (schemaResourceName == null) {
        //     throw new IllegalArgumentException("schemaResourceName is null");
        // }
        // var schemaPath = "schema/" + schemaResourceName;
        // var classLoader = getClass().getClassLoader();
        // try (var inputStream = classLoader.getResourceAsStream(schemaPath)) {
        //     this.jsonSchema = IOUtils.toString(inputStream, StandardCharsets.UTF_8.name());
        // } catch (IOException ioe) {
        //     throw new RuntimeException(String.format("Failed to load json schema from resource path '%'", schemaPath), ioe);
        // }
    }

    private final Resource jsonSchemaResource;

    /**
     * @return The json schema to validate against
     */
    public Resource getJsonSchemaResource() {
        return jsonSchemaResource;
    }

 

    // public String validate(String json) {

    // }
    
}
