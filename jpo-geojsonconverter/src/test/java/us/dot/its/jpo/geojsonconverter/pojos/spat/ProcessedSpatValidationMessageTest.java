package us.dot.its.jpo.geojsonconverter.pojos.spat;

import static org.junit.Assert.assertNotNull;
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

    @Test
    public void testEquals() {
        ProcessedSpatValidationMessage object = new ProcessedSpatValidationMessage();
        ProcessedSpatValidationMessage otherObject = new ProcessedSpatValidationMessage();
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
        ProcessedSpatValidationMessage object = new ProcessedSpatValidationMessage();

        int hash = object.hashCode();
        assertNotNull(hash);
    }
    
    @Test
    public void testToString() {
        ProcessedSpatValidationMessage object = new ProcessedSpatValidationMessage();

        String string = object.toString();
        assertNotNull(string);
    }
}
