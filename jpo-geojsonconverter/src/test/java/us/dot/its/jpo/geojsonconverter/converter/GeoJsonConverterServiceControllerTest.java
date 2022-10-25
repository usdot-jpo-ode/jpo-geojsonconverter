package us.dot.its.jpo.geojsonconverter.converter;

import static org.junit.Assert.assertNotNull;

import org.junit.Before;
import org.junit.Test;

import us.dot.its.jpo.geojsonconverter.GeoJsonConverterProperties;

public class GeoJsonConverterServiceControllerTest {
    GeoJsonConverterServiceController geoJsonConverterServiceController;
    GeoJsonConverterProperties props;

    @Before
    public void setup() {
        props = new GeoJsonConverterProperties();
        props.initialize();
    }

    @Test
    public void testSpringBootLoaded() {
        geoJsonConverterServiceController = new GeoJsonConverterServiceController(props);
        assertNotNull(geoJsonConverterServiceController);
    }
}
