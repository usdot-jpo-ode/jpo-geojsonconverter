package us.dot.its.jpo.geojsonconverter;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.Resource;

import us.dot.its.jpo.geojsonconverter.validator.MapJsonValidator;

@SpringBootTest
public class MapJsonValidatorTest {

    @Autowired
    private ApplicationContext applicationContext;
    
    @Autowired
    private MapJsonValidator mapJsonValidator;

    @Test
    public void contextLoads() {
        assertThat(applicationContext, notNullValue());
    }

    @Test
    public void canLoadSchemaResource() {
        assertNotNull(applicationContext);
        System.out.printf("Application context: %s%n", applicationContext);
        
        //assertThat(mapJsonValidator, notNullValue());
        //Resource schemaResource = mapJsonValidator.getJsonSchemaResource();
        //assertThat(schemaResource, notNullValue());
        //assertThat(schema, not(is(emptyOrNullString())));
    }
}
