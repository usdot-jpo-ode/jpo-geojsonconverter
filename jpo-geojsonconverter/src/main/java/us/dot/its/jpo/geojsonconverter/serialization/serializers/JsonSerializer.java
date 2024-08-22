package us.dot.its.jpo.geojsonconverter.serialization.serializers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import us.dot.its.jpo.geojsonconverter.DateJsonMapper;
import us.dot.its.jpo.geojsonconverter.GeoJsonConverterProperties;

import org.apache.kafka.common.serialization.Serializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * General purpose JSON serializer for Kafka.  Converts a POJO with appropriate Jackson annotations
 * to a JSON string encoded as a UTF-8 byte array.
 * 
 * @param <T> - The type of object to serialize
 */
public class JsonSerializer<T> implements Serializer<T> {

    private static Logger logger = LoggerFactory.getLogger(JsonSerializer.class);

    private final ObjectMapper mapper = DateJsonMapper.getInstance();

    // private final GeoJsonConverterProperties properties;


    // @Autowired
    // public JsonSerializer(GeoJsonConverterProperties properties) {
    //     this.properties = properties;
    //     if (properties.getJacksonAllowUnknownProperties()) {
    //         this.mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    //     }
    // }

    @Override
    public byte[] serialize(String topic, T data) {
        try {
            return mapper.writeValueAsBytes(data);
        } catch (JsonProcessingException e) {
            String errMsg = String.format("Exception serializing %s to bytes: %s", data, e.getMessage());
            logger.error(errMsg);
            return null;
        }
    }
}
