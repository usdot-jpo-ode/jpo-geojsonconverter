package us.dot.its.jpo.geojsonconverter.validator;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertFalse;

import org.junit.Test;

public class JsonValidatorResultTest {
    
    @Test
    public void testAddException() {
        var result = new JsonValidatorResult();
        var ex = new Exception();
        result.addException(ex);
        assertThat(result.getExceptions(), hasItem(equalTo(ex)));
        assertThat(result.describeResults(), containsString("Exception"));
        assertFalse(result.isValid());
    }

    
}
