package us.dot.its.jpo.geojsonconverter.tests;

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
public class MapJsonValidatorTest  {

    @Autowired
    private ApplicationContext appContext;

    @Test
    public void contextLoads() {
        assertThat(appContext, notNullValue());
    }

    

    
}
