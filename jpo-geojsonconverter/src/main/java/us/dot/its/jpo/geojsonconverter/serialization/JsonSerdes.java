package us.dot.its.jpo.geojsonconverter.serialization;

import us.dot.its.jpo.geojsonconverter.partitioner.RsuIntersectionKey;
import us.dot.its.jpo.geojsonconverter.partitioner.RsuLogKey;
import us.dot.its.jpo.geojsonconverter.pojos.geojson.bsm.ProcessedBsm;
import us.dot.its.jpo.geojsonconverter.pojos.spat.*;
import us.dot.its.jpo.geojsonconverter.pojos.geojson.LineString;
import us.dot.its.jpo.geojsonconverter.pojos.geojson.Point;
import us.dot.its.jpo.geojsonconverter.pojos.geojson.map.ProcessedMap;
import us.dot.its.jpo.geojsonconverter.pojos.geojson.bsm.ProcessedBsmCollection;
import us.dot.its.jpo.geojsonconverter.serialization.deserializers.*;
import us.dot.its.jpo.geojsonconverter.serialization.serializers.*;
import us.dot.its.jpo.ode.model.OdeMapData;
import us.dot.its.jpo.ode.model.OdeSpatData;
import us.dot.its.jpo.ode.model.OdeBsmData;

import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serdes;

/**
 * Contains static methods that return a "Serde", a serializer/deserializer for
 * JSON for Kafka, for each POJO type.
 */
public class JsonSerdes {
    public static Serde<OdeMapData> OdeMap() {
        return Serdes.serdeFrom(
            new JsonSerializer<OdeMapData>(), 
            new JsonDeserializer<>(OdeMapData.class));
    }

    public static Serde<ProcessedMap<LineString>> ProcessedMapGeoJson() {
        return Serdes.serdeFrom(
            new JsonSerializer<ProcessedMap<LineString>>(), 
            new ProcessedMapDeserializer<>(LineString.class));
    }

    public static Serde<ProcessedMap<String>> ProcessedMapWKT() {
        return Serdes.serdeFrom(
            new JsonSerializer<ProcessedMap<String>>(), 
            new ProcessedMapDeserializer<>(String.class));
    }

    public static Serde<OdeSpatData> OdeSpat() {
        return Serdes.serdeFrom(
            new JsonSerializer<OdeSpatData>(), 
            new JsonDeserializer<>(OdeSpatData.class));
    }

    public static Serde<ProcessedSpat> ProcessedSpat() {
        return Serdes.serdeFrom(
            new JsonSerializer<ProcessedSpat>(), 
            new JsonDeserializer<>(ProcessedSpat.class));
    }

    public static Serde<RsuIntersectionKey> RsuIntersectionKey() {
        return Serdes.serdeFrom(
            new JsonSerializer<RsuIntersectionKey>(),
            new JsonDeserializer<>(RsuIntersectionKey.class)
        );
    }

    public static Serde<OdeBsmData> OdeBsm() {
        return Serdes.serdeFrom(
            new JsonSerializer<OdeBsmData>(), 
            new JsonDeserializer<>(OdeBsmData.class));
    }

    public static Serde<ProcessedBsm<Point>> ProcessedBsm() {
        return Serdes.serdeFrom(
            new JsonSerializer<ProcessedBsm<Point>>(),
            new ProcessedBsmDeserializer<>(Point.class));
    }

    public static Serde<RsuLogKey> RsuLogKey() {
        return Serdes.serdeFrom(
            new JsonSerializer<RsuLogKey>(),
            new JsonDeserializer<>(RsuLogKey.class)
        );
    }
}
