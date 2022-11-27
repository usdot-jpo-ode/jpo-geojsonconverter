package us.dot.its.jpo.geojsonconverter.serialization.deserializers;

import java.io.IOException;

import us.dot.its.jpo.ode.model.OdeSpatData;
import us.dot.its.jpo.ode.model.OdeSpatMetadata;
import us.dot.its.jpo.ode.model.OdeSpatPayload;
import us.dot.its.jpo.ode.util.JsonUtils;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.apache.kafka.common.serialization.Deserializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * OdeSpatData JSON serializer for Kafka.  Converts a OdeSpatData POJO
 * to a JSON string encoded as a UTF-8 byte array.
 */

public class OdeSpatDataJsonDeserializer implements Deserializer<OdeSpatData> {

    private static Logger logger = LoggerFactory.getLogger(OdeSpatDataJsonDeserializer.class);

    protected final ObjectMapper mapper = new ObjectMapper();

    @Override
    public OdeSpatData deserialize(String topic, byte[] data) {
        if (data == null) {
            return null;
        }
        OdeSpatData returnData = new OdeSpatData();
        try {
            JsonNode actualObj = mapper.readTree(data);

            // Deserialize the metadata
            JsonNode metadataNode = actualObj.get("metadata");
            String metadataString = metadataNode.toString();
            OdeSpatMetadata metadataObject = (OdeSpatMetadata) JsonUtils.fromJson(metadataString, OdeSpatMetadata.class);
            returnData.setMetadata(metadataObject);

            // Deserialize the payload
            JsonNode payloadNode = actualObj.get("payload");
            String payloadString = payloadNode.toString();
            OdeSpatPayload mapPayload = (OdeSpatPayload) JsonUtils.fromJson(payloadString, OdeSpatPayload.class);
            returnData.setPayload(mapPayload);

            return returnData;
        } catch (Exception e) {
            String errMsg = String.format("Exception deserializing for topic %s: %s", topic, e.getMessage());
            logger.error(errMsg, e);
            return returnData;
        }
    }
}
