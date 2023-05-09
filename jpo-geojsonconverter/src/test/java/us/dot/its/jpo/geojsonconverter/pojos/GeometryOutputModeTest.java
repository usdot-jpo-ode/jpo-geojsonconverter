package us.dot.its.jpo.geojsonconverter.pojos;

import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.Test;

public class GeometryOutputModeTest {
    @Test
    public void testFindByName() {
        String wktMode = "WKT";
        GeometryOutputMode gomTest = GeometryOutputMode.findByName(wktMode);
        assertNotNull(gomTest);
        assertEquals(GeometryOutputMode.WKT, gomTest);
    }

    @Test
    public void testFindByNameNull() {
        String wktMode = "test";
        GeometryOutputMode gomTest = GeometryOutputMode.findByName(wktMode);
        assertEquals(null, gomTest);
    }
}
