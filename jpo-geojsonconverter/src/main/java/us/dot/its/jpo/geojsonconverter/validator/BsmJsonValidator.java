package us.dot.its.jpo.geojsonconverter.validator;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

/**
 * JSON validator for BSM messages.
 */
@Service
public class BsmJsonValidator extends AbstractJsonValidator {
    /**
     * @param jsonSchemaResource The json schema file in resources/schemas.  Injected by Spring DI.
     */
    public BsmJsonValidator(@Value("${schema.bsm}") Resource jsonSchemaResource) {
        super(jsonSchemaResource);
    }
}
