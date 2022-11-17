package us.dot.its.jpo.geojsonconverter.pojos.spat;

import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.Test;

import org.apache.kafka.common.utils.Bytes;

import us.dot.its.jpo.geojsonconverter.validator.JsonValidatorResult;

public class ValidationRawSpatTest {
    @Test
    public void testGettersSetters() {
        ValidationRawSpat object = new ValidationRawSpat();
        
        Bytes byteData = new Bytes(null);
        object.setOdeSpatBytes(byteData);
        Bytes dataResponse = object.getOdeSpatBytes();
        assertEquals(dataResponse, byteData);

        JsonValidatorResult validation = new JsonValidatorResult();
        object.setValidatorResults(validation);
        JsonValidatorResult validationResponse = object.getValidatorResults();
        assertEquals(validationResponse, validation);
    }

    @Test
    public void testEquals() {
        ValidationRawSpat object = new ValidationRawSpat();
        ValidationRawSpat otherObject = new ValidationRawSpat();
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
        ValidationRawSpat object = new ValidationRawSpat();

        int hash = object.hashCode();
        assertNotNull(hash);
    }
    
    @Test
    public void testToString() {
        ValidationRawSpat object = new ValidationRawSpat();

        String string = object.toString();
        assertNotNull(string);
    }
}
