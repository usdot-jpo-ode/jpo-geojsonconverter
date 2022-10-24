package us.dot.its.jpo.geojsonconverter.converter;

import static org.junit.Assert.assertNotNull;

import org.junit.jupiter.api.Test;

import us.dot.its.jpo.geojsonconverter.GeoJsonConverterProperties;

public class GeoJsonConverterServiceControllerTest {
    GeoJsonConverterServiceController geoJsonConverterServiceController;
    GeoJsonConverterProperties props = new GeoJsonConverterProperties();

    @Test
    public void testSpringBootLoaded() {
        geoJsonConverterServiceController = new GeoJsonConverterServiceController(props);
        assertNotNull(geoJsonConverterServiceController);
    }
}
