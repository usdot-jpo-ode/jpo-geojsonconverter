package us.dot.its.jpo.geojsonconverter.pojos.spat;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

public class ProcessedSpatValidationMessageTest {
    @Test
    public void testGettersSetters() {
        ProcessedSpatValidationMessage object = new ProcessedSpatValidationMessage();

        object.setMessage("msg");
        String msgResponse = object.getMessage();
        assertEquals(msgResponse, "msg");

        object.setJsonPath("json path");
        String jsonPathResponse = object.getJsonPath();
        assertEquals(jsonPathResponse, "json path");

        object.setSchemaPath("schema path");
        String schemaPathResponse = object.getSchemaPath();
        assertEquals(schemaPathResponse, "schema path");

        object.setException("exception");
        String exceptionResponse = object.getException();
        assertEquals(exceptionResponse, "exception");
    }
}
