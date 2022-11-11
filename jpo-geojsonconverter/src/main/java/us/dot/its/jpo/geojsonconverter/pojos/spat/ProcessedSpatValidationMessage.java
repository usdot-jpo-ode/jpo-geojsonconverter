
package us.dot.its.jpo.geojsonconverter.pojos.spat;

import javax.annotation.Generated;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "message",
    "jsonPath",
    "schemaPath",
    "exception"
})
@Generated("jsonschema2pojo")
public class ProcessedSpatValidationMessage {

    @JsonProperty("message")
    private String message;
    @JsonProperty("jsonPath")
    private String jsonPath;
    @JsonProperty("schemaPath")
    private String schemaPath;
    @JsonProperty("exception")
    private String exception;

    @JsonProperty("message")
    public String getMessage() {
        return message;
    }

    @JsonProperty("message")
    public void setMessage(String message) {
        this.message = message;
    }

    @JsonProperty("jsonPath")
    public String getJsonPath() {
        return jsonPath;
    }

    @JsonProperty("jsonPath")
    public void setJsonPath(String jsonPath) {
        this.jsonPath = jsonPath;
    }

    @JsonProperty("schemaPath")
    public String getSchemaPath() {
        return schemaPath;
    }

    @JsonProperty("schemaPath")
    public void setSchemaPath(String schemaPath) {
        this.schemaPath = schemaPath;
    }

    @JsonProperty("exception")
    public String getException() {
        return exception;
    }

    @JsonProperty("exception")
    public void setException(String exception) {
        this.exception = exception;
    }

}
