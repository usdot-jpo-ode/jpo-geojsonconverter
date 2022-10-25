package us.dot.its.jpo.geojsonconverter.converter;

import static org.junit.Assert.assertNotNull;

import org.junit.Before;
import org.junit.Test;
import mockit.*;

import us.dot.its.jpo.geojsonconverter.GeoJsonConverterProperties;
import us.dot.its.jpo.geojsonconverter.validator.MapJsonValidator;

public class GeoJsonConverterServiceControllerTest {
    GeoJsonConverterServiceController geoJsonConverterServiceController;
    GeoJsonConverterProperties props;

    @Mocked MapJsonValidator mapJsonValidator;

    @Before
    public void setup() {
        props = new GeoJsonConverterProperties();
        props.initialize();
    }

    @Test
    public void testSpringBootLoaded() {
        geoJsonConverterServiceController = new GeoJsonConverterServiceController(props, mapJsonValidator);
        assertNotNull(geoJsonConverterServiceController);
    }
}
