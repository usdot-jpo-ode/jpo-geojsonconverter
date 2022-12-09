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
        assertEquals("msg", msgResponse);

        object.setJsonPath("json path");
        String jsonPathResponse = object.getJsonPath();
        assertEquals("json path", jsonPathResponse);

        object.setSchemaPath("schema path");
        String schemaPathResponse = object.getSchemaPath();
        assertEquals("schema path", schemaPathResponse);

        object.setException("exception");
        String exceptionResponse = object.getException();
        assertEquals("exception", exceptionResponse);
    }

    @Test
    public void testEquals() {
        ProcessedValidationMessage object = new ProcessedValidationMessage();
        ProcessedValidationMessage otherObject = new ProcessedValidationMessage();
        otherObject.setException("exception");

        boolean equals = object.equals(object);
        assertEquals(true, equals);
        
        boolean otherEquals = object.equals(otherObject);
        assertEquals(false, otherEquals);

        String string = "string";
        boolean notEquals = otherObject.equals(string);
        assertEquals(false, notEquals);
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
