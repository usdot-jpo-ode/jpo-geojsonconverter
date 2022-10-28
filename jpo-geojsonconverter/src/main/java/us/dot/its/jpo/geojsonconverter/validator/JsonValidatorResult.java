package us.dot.its.jpo.geojsonconverter.validator;

import java.util.Collection;
import java.util.List;
import java.util.ArrayList;

import com.networknt.schema.ValidationMessage;

public class JsonValidatorResult {

      
    private final List<Exception> exceptions = new ArrayList<>();
    private final List<ValidationMessage> validationMessages = new ArrayList<>();

    public boolean isValid() {
        return exceptions.isEmpty() && validationMessages.isEmpty();
    }

   
    /**
     * @return Jackson JSON processing exceptions
     */
    public List<Exception> getExceptions() {
        return exceptions;
    }

    public void addException(Exception ex) {
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

    public String describeResults() {
        var sb = new StringBuilder();
        sb.append(String.format("Json Validator result: isValid = %s%n", isValid()));

        for (var exception : getExceptions()) {
            sb.append(String.format("JsonProcessingException: %s%n", exception.getMessage()));
        }

        for (var validationMessage : getValidationMessages()) {
            sb.append(String.format("JSON Validation Message: %s%n", validationMessage.getMessage()));
        }

        return sb.toString();
    }

}
