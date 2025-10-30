package us.dot.its.jpo.geojsonconverter.validator;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

/**
 * JSON validator for PSM messages.
 */
@Service
public class PsmJsonValidator extends AbstractJsonValidator {

    public PsmJsonValidator() {
        super("classpath:schemas/psm.schema.json");
    }

    /**
     * @param schemaLocation The json schema classpath
     */
    public PsmJsonValidator(String schemaLocation) {
        super(schemaLocation);
    }

}
