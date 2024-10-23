package us.dot.its.jpo.geojsonconverter.serialization;

import static org.junit.Assert.assertNotNull;

import org.apache.kafka.common.serialization.Serde;
import org.junit.Test;

import us.dot.its.jpo.geojsonconverter.pojos.geojson.LineString;
import us.dot.its.jpo.geojsonconverter.pojos.geojson.Point;
import us.dot.its.jpo.geojsonconverter.pojos.geojson.bsm.ProcessedBsm;
import us.dot.its.jpo.geojsonconverter.pojos.geojson.map.ProcessedMap;
import us.dot.its.jpo.geojsonconverter.pojos.spat.ProcessedSpat;
import us.dot.its.jpo.ode.model.OdeBsmData;
import us.dot.its.jpo.ode.model.OdeMapData;
import us.dot.its.jpo.ode.model.OdeSpatData;

public class JsonSerdesTest {
    @Test
    public void testOdeMapSerdes() {
        Serde<OdeMapData> serde = JsonSerdes.OdeMap();
        assertNotNull(serde);
    }

    @Test
    public void testProcessedMapGeoJsonSerdes() {
        Serde<ProcessedMap<LineString>> serde = JsonSerdes.ProcessedMapGeoJson();
        assertNotNull(serde);
    }

    @Test
    public void testProcessedMapWKTSerdes() {
        Serde<ProcessedMap<String>> serde = JsonSerdes.ProcessedMapWKT();
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

    @Test
    public void testOdeBsmSerdes() {
        Serde<OdeBsmData> serde = JsonSerdes.OdeBsm();
        assertNotNull(serde);
    }

    @Test
    public void testProcessedBsmSerdes() {
        Serde<ProcessedBsm<Point>> serde = JsonSerdes.ProcessedBsm();
        assertNotNull(serde);
    }
}
