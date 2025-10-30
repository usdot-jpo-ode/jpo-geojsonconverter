package us.dot.its.jpo.geojsonconverter.validator;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

/**
 * JSON validator for SSM Messages.
 */
@Service
public class SsmJsonValidator extends AbstractJsonValidator {

    public SsmJsonValidator() {
        super("classpath:schemas/ssm.schema.json");
    }

    /**
     * @param schemaLocation The json schema classpath
     */
    public SsmJsonValidator(String schemaLocation) {
        super(schemaLocation);
    }
}
