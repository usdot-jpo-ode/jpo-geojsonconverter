package us.dot.its.jpo.geojsonconverter.validator;

import java.util.Collection;
import java.util.List;
import java.util.ArrayList;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.networknt.schema.ValidationMessage;

public class JsonValidatorResult {

    public JsonValidatorResult() {
        
    }
    
    private final List<JsonProcessingException> exceptions = new ArrayList<JsonProcessingException>();
    private final List<ValidationMessage> validationMessages = new ArrayList<ValidationMessage>();

    public boolean isValid() {
        return exceptions.isEmpty() && validationMessages.isEmpty();
    }

   
    /**
     * @return Jackson JSON processing exceptions
     */
    public List<JsonProcessingException> getExceptions() {
        return exceptions;
    }

    public void addException(JsonProcessingException ex) {
        exceptions.add(ex);
    }

    /**
     * @return ValidationMessages returned by the schema validator.
     */
    public List<ValidationMessage> getValidationMessages() {
        return validationMessages;
    }

    public void addValidationMessages(Collection<ValidationMessage> messages) {
        validationMessages.addAll(messages);
    }

}
