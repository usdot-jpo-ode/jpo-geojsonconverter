package us.dot.its.jpo.geojsonconverter.validator;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

/**
 * JSON validator for BSM messages.
 */
@Service
public class BsmJsonValidator extends AbstractJsonValidator {

    public BsmJsonValidator() {
        super("classpath:schemas/bsm.schema.json");
    }

    /**
     * @param schemaLocation The json schema classpath
     */
    public BsmJsonValidator(String schemaLocation) {
        super(schemaLocation);
    }
}
