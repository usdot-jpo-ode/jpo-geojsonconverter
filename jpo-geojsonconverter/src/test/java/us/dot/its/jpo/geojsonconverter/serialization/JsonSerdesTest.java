package us.dot.its.jpo.geojsonconverter.serialization;

import static org.junit.Assert.assertNotNull;

import org.apache.kafka.common.serialization.Serde;
import org.junit.Test;

import us.dot.its.jpo.geojsonconverter.pojos.geojson.map.MapFeatureCollection;
import us.dot.its.jpo.geojsonconverter.pojos.spat.ProcessedSpat;
import us.dot.its.jpo.ode.model.OdeMapData;
import us.dot.its.jpo.ode.model.OdeSpatData;

public class JsonSerdesTest {
    @Test
    public void testOdeMapSerdes() {
        Serde<OdeMapData> serde = JsonSerdes.OdeMap();
        assertNotNull(serde);
    }

    @Test
    public void testMapGeoJsonSerdes() {
        Serde<MapFeatureCollection> serde = JsonSerdes.MapGeoJson();
        assertNotNull(serde);
    }

    @Test
    public void testOdeSpatSerdes() {
        Serde<OdeSpatData> serde = JsonSerdes.OdeSpat();
        assertNotNull(serde);
    }

    @Test
    public void testProcessedSpatSerdes() {
        Serde<ProcessedSpat> serde = JsonSerdes.ProcessedSpat();
        assertNotNull(serde);
    }
}
