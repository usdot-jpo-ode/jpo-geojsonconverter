package us.dot.its.jpo.geojsonconverter.serialization.deserializers;

import us.dot.its.jpo.ode.model.OdeMapData;
import us.dot.its.jpo.ode.model.OdeMapMetadata;
import us.dot.its.jpo.ode.model.OdeMapPayload;
import us.dot.its.jpo.ode.util.JsonUtils;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import org.apache.kafka.common.serialization.Deserializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * OdeMapData JSON serializer for Kafka.  Converts a OdeMapData POJO
 * to a JSON string encoded as a UTF-8 byte array.
 */
public class OdeMapDataJsonDeserializer implements Deserializer<OdeMapData> {
    
    private static Logger logger = LoggerFactory.getLogger(OdeMapDataJsonDeserializer.class);

    protected final ObjectMapper mapper = new ObjectMapper();

    @Override
    public OdeMapData deserialize(String topic, byte[] data) {
        if (data == null) {
            return null;
        }
        try {
            JsonNode actualObj = mapper.readTree(data);

			// Deserialize the metadata
			JsonNode metadataNode = actualObj.get("metadata");
            if (metadataNode.get("@class") != null) {
                ((ObjectNode)metadataNode).remove("@class");
            }
			String metadataString = metadataNode.toString();
			OdeMapMetadata metadataObject = (OdeMapMetadata) JsonUtils.fromJson(metadataString, OdeMapMetadata.class);

			// Deserialize the payload
			JsonNode payloadNode = actualObj.get("payload");
            if (payloadNode.get("@class") != null) {
                ((ObjectNode)payloadNode).remove("@class");
            }
			String payloadString = payloadNode.toString();
			OdeMapPayload mapPayload = (OdeMapPayload) JsonUtils.fromJson(payloadString, OdeMapPayload.class);

            OdeMapData returnData = new OdeMapData(metadataObject, mapPayload);
            return returnData;
        } catch (Exception e) {
            String errMsg = String.format("Exception deserializing for topic %s: %s", topic, e.getMessage());
            logger.error(errMsg, e);
            throw new RuntimeException(errMsg, e);
        }
    }
}
