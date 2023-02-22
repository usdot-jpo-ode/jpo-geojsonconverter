package us.dot.its.jpo.geojsonconverter.serialization.deserializers;

import us.dot.its.jpo.ode.model.OdeMapData;

/**
 * OdeMapData JSON serializer for Kafka.  Converts a OdeMapData POJO
 * to a JSON string encoded as a UTF-8 byte array.
 */
public class OdeMapDataJsonDeserializer extends JsonDeserializer<OdeMapData> {
    
    public OdeMapDataJsonDeserializer() {
        super(OdeMapData.class);
    }

    
}
