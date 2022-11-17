package us.dot.its.jpo.geojsonconverter.pojos.spat;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.Test;

import us.dot.its.jpo.geojsonconverter.validator.JsonValidatorResult;
import us.dot.its.jpo.ode.model.OdeSpatData;

public class DeserializedRawSpatTest {
    @Test
    public void testGettersSetters() {
        DeserializedRawSpat object = new DeserializedRawSpat();
        
        OdeSpatData spatData = new OdeSpatData();
        object.setOdeSpatOdeSpatData(spatData);
        OdeSpatData dataResponse = object.getOdeSpatOdeSpatData();
        assertEquals(dataResponse, spatData);

        JsonValidatorResult validation = new JsonValidatorResult();
        object.setValidatorResults(validation);
        JsonValidatorResult validationResponse = object.getValidatorResults();
        assertEquals(validationResponse, validation);
    }

    @Test
    public void testEquals() {
        DeserializedRawSpat object = new DeserializedRawSpat();
        DeserializedRawSpat otherObject = new DeserializedRawSpat();
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
        DeserializedRawSpat object = new DeserializedRawSpat();

        int hash = object.hashCode();
        assertNotNull(hash);
    }

    @Test
    public void testToString() {
        DeserializedRawSpat object = new DeserializedRawSpat();

        String string = object.toString();
        assertNotNull(string);
    }
}
