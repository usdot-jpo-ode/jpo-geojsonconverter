package us.dot.its.jpo.geojsonconverter.pojos.geojson.map;

import static org.junit.Assert.assertNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.Test;
import us.dot.its.jpo.asn.j2735.r2024.Common.Elevation;
import us.dot.its.jpo.asn.j2735.r2024.Common.Latitude;
import us.dot.its.jpo.asn.j2735.r2024.Common.Longitude;
import us.dot.its.jpo.asn.j2735.r2024.Common.Position3D;
import us.dot.its.jpo.geojsonconverter.pojos.common.ProcessedPosition3D;

public class MapRefPointTest {
    @Test
    public void testSetFromPosition3D() {
        MapRefPoint mapRefPoint = new MapRefPoint();
        var position3D = new ProcessedPosition3D();
        Integer latitude = 404565300;
        Integer longitude = -1051292800;
        Integer elevation = 16384;
        position3D.setLat(latitude);
        position3D.setLong_(longitude);
        position3D.setElevation(elevation);

        mapRefPoint.setFromPosition3D(position3D);

        assertEquals(Double.valueOf(40.4565300), mapRefPoint.getLatitude(), 1e7);
        assertEquals(Double.valueOf(-105.1292800), mapRefPoint.getLongitude(), 1e7);
        assertEquals(Double.valueOf(1638.4), mapRefPoint.getElevation(), 0.1);
    }

    @Test
    public void testSetFromPosition3DNullFields() {
        MapRefPoint mapRefPoint = new MapRefPoint();
        var position3D = new ProcessedPosition3D();
        position3D.setLat(null);
        position3D.setLong_(null);
        position3D.setElevation(null);

        mapRefPoint.setFromPosition3D(position3D);

        assertNull(mapRefPoint.getLatitude());
        assertNull(mapRefPoint.getLongitude());
        assertNull(mapRefPoint.getElevation());
    }
}
