package us.dot.its.jpo.geojsonconverter.pojos;

import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import us.dot.its.jpo.geojsonconverter.DateJsonMapper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class ProcessedValidationMessage {
    private static Logger logger = LoggerFactory.getLogger(ProcessedValidationMessage.class);

    private String message;
    private String jsonPath;
    private String schemaPath;
    private String exception;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getJsonPath() {
        return jsonPath;
    }

    public void setJsonPath(String jsonPath) {
        this.jsonPath = jsonPath;
    }

    public String getSchemaPath() {
        return schemaPath;
    }

    public void setSchemaPath(String schemaPath) {
        this.schemaPath = schemaPath;
    }

    public String getException() {
        return exception;
    }

    public void setException(String exception) {
        this.exception = exception;
    }


    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof ProcessedValidationMessage)) {
            return false;
        }
        ProcessedValidationMessage processedSpatValidationMessage = (ProcessedValidationMessage) o;
        return Objects.equals(message, processedSpatValidationMessage.message) && Objects.equals(jsonPath, processedSpatValidationMessage.jsonPath) && Objects.equals(schemaPath, processedSpatValidationMessage.schemaPath) && Objects.equals(exception, processedSpatValidationMessage.exception);
    }

    @Override
    public int hashCode() {
        return Objects.hash(message, jsonPath, schemaPath, exception);
    }

    @Override
    public String toString() {
        ObjectMapper mapper = DateJsonMapper.getInstance();
        String testReturn = "";
        try {
            testReturn = (mapper.writeValueAsString(this));
        } catch (JsonProcessingException e) {
            logger.error(e.getMessage(), e);
        }
        return testReturn;
    }

}
