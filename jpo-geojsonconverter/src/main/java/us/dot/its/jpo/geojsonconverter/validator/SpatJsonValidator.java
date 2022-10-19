package us.dot.its.jpo.geojsonconverter.validator;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

/**
 * JSON validator for SpAT messages.
 */
@Service
public class SpatJsonValidator extends AbstractJsonValidator {

    /**
     * @param jsonSchemaResource The json schema file in resources/schemas.  Injected by Spring DI.
     */
    public SpatJsonValidator(@Value("${schema.spat}") Resource jsonSchemaResource) {
        super(jsonSchemaResource);
    }
    
}
