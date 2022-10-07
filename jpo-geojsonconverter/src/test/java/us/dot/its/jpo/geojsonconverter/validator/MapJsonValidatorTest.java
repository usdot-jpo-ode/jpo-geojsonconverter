package us.dot.its.jpo.geojsonconverter.validator;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;



@SpringBootTest
@RunWith(SpringRunner.class)    
public class MapJsonValidatorTest  {


    @Autowired
    private MapJsonValidator mapJsonValidator;

    
    @Test
    public void mapJsonValidatorLoaded() {
        assertThat(mapJsonValidator, notNullValue());
    }

    @Test
    public void jsonSchemaResourceLoaded() {
        var resource = mapJsonValidator.getJsonSchemaResource();
        assertThat(resource, notNullValue());
        assertTrue("Resource does not exist", resource.exists());
    }

    @Test
    public void jsonSchemaLoaded() {
        var schema = mapJsonValidator.getJsonSchema();
        assertThat(schema, notNullValue());
        assertThat(schema, containsString("$schema"));
    }
    

    
}
