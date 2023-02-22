package us.dot.its.jpo.geojsonconverter.serialization.deserializers;

import us.dot.its.jpo.ode.model.OdeSpatData;

/**
 * OdeSpatData JSON serializer for Kafka.  Converts a OdeSpatData POJO
 * to a JSON string encoded as a UTF-8 byte array.
 */

public class OdeSpatDataJsonDeserializer extends JsonDeserializer<OdeSpatData> {

    public OdeSpatDataJsonDeserializer() {
        super(OdeSpatData.class);
    }
}
