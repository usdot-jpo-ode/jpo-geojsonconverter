package us.dot.its.jpo.geojsonconverter.serialization.deserializers;


import java.io.IOException;

import org.apache.kafka.common.serialization.Deserializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;

import us.dot.its.jpo.geojsonconverter.DateJsonMapper;
import us.dot.its.jpo.geojsonconverter.pojos.geojson.map.ProcessedMap;

public class ProcessedMapDeserializer<T> implements Deserializer<ProcessedMap<T>> {
    private static Logger logger = LoggerFactory.getLogger(ProcessedMapDeserializer.class);

    protected final ObjectMapper mapper = DateJsonMapper.getInstance();

    private Class<T> geometryClass;

    public ProcessedMapDeserializer(Class<T> geometryClass) {
        this.geometryClass = geometryClass;
    }

    @SuppressWarnings("unchecked")
    @Override
    public ProcessedMap<T> deserialize(String topic, byte[] data) {
        if (data == null) {
                return null;
        }
        try {
            JavaType javaType = mapper.getTypeFactory().constructParametricType(ProcessedMap.class, geometryClass);
            return (ProcessedMap<T>) mapper.readValue(data, javaType);
        } catch (IOException e) {
            String errMsg = String.format("Exception deserializing for topic %s: %s", topic, e.getMessage());
            logger.error(errMsg, e);
            throw new RuntimeException(errMsg, e);
        } 
    }
}