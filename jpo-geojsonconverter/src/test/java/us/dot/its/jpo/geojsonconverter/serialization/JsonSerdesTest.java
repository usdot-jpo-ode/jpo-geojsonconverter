package us.dot.its.jpo.geojsonconverter.serialization;

import static org.junit.Assert.assertNotNull;

import org.apache.kafka.common.serialization.Serde;
import org.junit.Test;

import us.dot.its.jpo.geojsonconverter.pojos.geojson.connectinglanes.GeoJsonConnectingLanesFeature;
import us.dot.its.jpo.geojsonconverter.pojos.geojson.connectinglanes.WKTConnectingLanesFeature;
import us.dot.its.jpo.geojsonconverter.pojos.geojson.map.GeoJsonMapFeature;
import us.dot.its.jpo.geojsonconverter.pojos.geojson.map.ProcessedMap;
import us.dot.its.jpo.geojsonconverter.pojos.geojson.map.WKTMapFeature;
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
    public void testProcessedMapGeoJsonSerdes() {
        Serde<ProcessedMap<GeoJsonMapFeature, GeoJsonConnectingLanesFeature>> serde = JsonSerdes.ProcessedMapGeoJson();
        assertNotNull(serde);
    }

    @Test
    public void testProcessedMapWKTSerdes() {
        Serde<ProcessedMap<WKTMapFeature, WKTConnectingLanesFeature>> serde = JsonSerdes.ProcessedMapWKT();
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
