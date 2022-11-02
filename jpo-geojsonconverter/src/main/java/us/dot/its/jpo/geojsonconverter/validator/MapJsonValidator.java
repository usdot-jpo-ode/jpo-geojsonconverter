package us.dot.its.jpo.geojsonconverter.validator;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;


/**
 * JSON validator for MapData messages.
 */
@Service
public class MapJsonValidator extends AbstractJsonValidator  {

    /**
     * @param jsonSchemaResource The json schema file in resources/schemas.  Injected by Spring DI.
     */
    public MapJsonValidator(@Value("${schema.map}") Resource jsonSchemaResource) {
        super(jsonSchemaResource);
    }


}
