package us.dot.its.jpo.geojsonconverter.validator;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

/**
 * JSON validator for RTCM messasges.
 * Validates J2735 requirements and ODE metadata.
 */
@Service
public class RTCMJsonValidator extends AbstractJsonValidator {

    public RTCMJsonValidator() {
        super("classpath:schemas/srm.schema.json");
    }

    /**
     * @param schemaLocation The json schema classpath
     */
    public RTCMJsonValidator(String schemaLocation) {
        super(schemaLocation);
    }
}
