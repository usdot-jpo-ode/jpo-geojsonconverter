package us.dot.its.jpo.geojsonconverter.validator;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

/**
 * JSON validator for SpAT messages.
 */
@Service
public class SpatJsonValidator extends AbstractJsonValidator {

    public SpatJsonValidator() {
        super("classpath:schemas/spat.schema.json");
    }

    /**
     * @param schemaLocation The json schema classpath
     */
    public SpatJsonValidator(String schemaLocation) {
        super(schemaLocation);
    }
    
}
