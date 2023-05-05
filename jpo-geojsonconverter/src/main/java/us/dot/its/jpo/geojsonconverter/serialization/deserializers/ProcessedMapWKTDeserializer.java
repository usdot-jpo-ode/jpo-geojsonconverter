package us.dot.its.jpo.geojsonconverter.serialization.deserializers;


import java.io.IOException;

import org.apache.kafka.common.serialization.Deserializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;

import us.dot.its.jpo.geojsonconverter.DateJsonMapper;
import us.dot.its.jpo.geojsonconverter.pojos.geojson.map.ProcessedMap;

public class ProcessedMapWKTDeserializer implements Deserializer<ProcessedMap<String>> {
    private static Logger logger = LoggerFactory.getLogger(ProcessedMapWKTDeserializer.class);

    protected final ObjectMapper mapper = DateJsonMapper.getInstance();

    @SuppressWarnings("unchecked")
    @Override
    public ProcessedMap<String> deserialize(String topic, byte[] data) {
        if (data == null) {
            return null;
        }
        try {
            JavaType javaType = mapper.getTypeFactory().constructParametricType(ProcessedMap.class, String.class);
            return (ProcessedMap<String>) mapper.readValue(data, javaType);
        } catch (IOException e) {
            String errMsg = String.format("Exception deserializing for topic %s: %s", topic, e.getMessage());
            logger.error(errMsg, e);
            throw new RuntimeException(errMsg, e);
        }
    }
}