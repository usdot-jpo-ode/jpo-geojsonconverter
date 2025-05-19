package us.dot.its.jpo.geojsonconverter.validator;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

/**
 * JSON validator for PSM messages.
 */
@Service
public class PsmJsonValidator extends AbstractJsonValidator {
    /**
     * @param jsonSchemaResource The json schema file in resources/schemas.  Injected by Spring DI.
     */
    public PsmJsonValidator(@Value("${schema.psm}") Resource jsonSchemaResource) {
        super(jsonSchemaResource);
    }
}
