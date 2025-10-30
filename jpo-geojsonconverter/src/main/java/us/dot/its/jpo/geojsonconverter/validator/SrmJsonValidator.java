package us.dot.its.jpo.geojsonconverter.validator;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;

/**
 * JSON validator for SRM messages.
 */
@Service
public class SrmJsonValidator extends AbstractJsonValidator {


    public SrmJsonValidator() {
        super("classpath:schemas/srm.schema.json");
    }

    /**
     * @param schemaLocation The json schema classpath
     */
    public SrmJsonValidator(String schemaLocation) {
        super(schemaLocation);
    }

}
