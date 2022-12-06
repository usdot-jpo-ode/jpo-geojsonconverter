package us.dot.its.jpo.geojsonconverter.pojos;

import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.Test;

public class ProcessedValidationMessageTest {
    @Test
    public void testGettersSetters() {
        ProcessedValidationMessage object = new ProcessedValidationMessage();

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

    @Test
    public void testEquals() {
        ProcessedValidationMessage object = new ProcessedValidationMessage();
        ProcessedValidationMessage otherObject = new ProcessedValidationMessage();
        boolean equals = object.equals(object);
        assertEquals(equals, true);
        
        boolean otherEquals = object.equals(otherObject);
        assertEquals(otherEquals, true);

        String string = "string";
        boolean notEquals = otherObject.equals(string);
        assertEquals(notEquals, false);
    }


    @Test
    public void testHashCode() {
        ProcessedValidationMessage object = new ProcessedValidationMessage();

        Integer hash = object.hashCode();
        assertNotNull(hash);
    }
    
    @Test
    public void testToString() {
        ProcessedValidationMessage object = new ProcessedValidationMessage();

        String string = object.toString();
        assertNotNull(string);
    }
}
