package us.dot.its.jpo.geojsonconverter;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest
@RunWith(SpringRunner.class)
public class GeoJsonConverterPropertiesTest {
    

    @Autowired
    private ApplicationContext appContext;

    @Autowired
    private GeoJsonConverterProperties appProperties;

    @Test
    public void contextLoaded() {
        assertThat(appContext, notNullValue());
    }

    @Test
    public void propertiesLoaded() {
        assertThat(appProperties, notNullValue());
    }

    @Test
    public void envIsSet() {
        assertThat(appProperties.getEnv(), notNullValue());
    }

    @Test
    public void versionIsSet() {
        assertThat(appProperties.getVersion(), is(not(emptyOrNullString())));
    }

    
}
