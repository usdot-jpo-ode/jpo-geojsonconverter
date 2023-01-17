package us.dot.its.jpo.geojsonconverter.serialization;

import us.dot.its.jpo.geojsonconverter.partitioner.RsuIntersectionKey;
import us.dot.its.jpo.geojsonconverter.pojos.geojson.map.ProcessedMap;
import us.dot.its.jpo.geojsonconverter.pojos.spat.*;
import us.dot.its.jpo.geojsonconverter.serialization.deserializers.*;
import us.dot.its.jpo.geojsonconverter.serialization.serializers.*;
import us.dot.its.jpo.ode.model.OdeMapData;
import us.dot.its.jpo.ode.model.OdeSpatData;

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

    public static Serde<ProcessedMap> ProcessedMap() {
        return Serdes.serdeFrom(
            new JsonSerializer<ProcessedMap>(), 
            new JsonDeserializer<>(ProcessedMap.class));
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
}
