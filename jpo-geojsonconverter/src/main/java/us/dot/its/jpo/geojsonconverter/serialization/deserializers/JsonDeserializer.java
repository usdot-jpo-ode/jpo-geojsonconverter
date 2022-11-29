package us.dot.its.jpo.geojsonconverter.serialization.deserializers;

import java.io.IOException;

import com.fasterxml.jackson.databind.ObjectMapper;

import us.dot.its.jpo.geojsonconverter.DateJsonMapper;

import org.apache.kafka.common.serialization.Deserializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JsonDeserializer<T> implements Deserializer<T> {
    private static Logger logger = LoggerFactory.getLogger(JsonDeserializer.class);

    protected final ObjectMapper mapper = DateJsonMapper.getInstance();

    private Class<T> destinationClass;

    public JsonDeserializer(Class<T> destinationClass) {
        this.destinationClass = destinationClass;
    }

    @Override
    public T deserialize(String topic, byte[] data) {
        if (data == null) {
            return null;
        }
        try {
            T returnData = mapper.readValue(data, destinationClass);
            //logger.info("Deserialized value: {} from class {}", returnData, destinationClass);
            return returnData;
        } catch (IOException e) {
            String errMsg = String.format("Exception deserializing for topic %s: %s", topic, e.getMessage());
            logger.error(errMsg, e);
            throw new RuntimeException(errMsg, e);
        }
    }
}
