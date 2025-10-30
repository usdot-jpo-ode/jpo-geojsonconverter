package us.dot.its.jpo.geojsonconverter.serialization;

import us.dot.its.jpo.geojsonconverter.partitioner.*;
import us.dot.its.jpo.geojsonconverter.pojos.geojson.bsm.ProcessedBsm;
import us.dot.its.jpo.geojsonconverter.pojos.geojson.rtcm.ProcessedRTCM;
import us.dot.its.jpo.geojsonconverter.pojos.geojson.srm.ProcessedSrm;
import us.dot.its.jpo.geojsonconverter.pojos.spat.*;
import us.dot.its.jpo.geojsonconverter.pojos.geojson.LineString;
import us.dot.its.jpo.geojsonconverter.pojos.geojson.Point;
import us.dot.its.jpo.geojsonconverter.pojos.geojson.map.ProcessedMap;
import us.dot.its.jpo.geojsonconverter.pojos.geojson.psm.ProcessedPsm;
import us.dot.its.jpo.geojsonconverter.pojos.ssm.ProcessedSsm;
import us.dot.its.jpo.geojsonconverter.serialization.deserializers.*;
import us.dot.its.jpo.geojsonconverter.serialization.serializers.*;
import us.dot.its.jpo.ode.model.OdeMessageFrameData;

import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serdes;

/**
 * Contains static methods that return a "Serde", a serializer/deserializer for JSON for Kafka, for each POJO type.
 */
public class JsonSerdes {
    public static Serde<OdeMessageFrameData> OdeMessageFrame() {
        return Serdes.serdeFrom(new JsonSerializer<OdeMessageFrameData>(),
                new JsonDeserializer<>(OdeMessageFrameData.class));
    }

    public static Serde<ProcessedMap<LineString>> ProcessedMapGeoJson() {
        return Serdes.serdeFrom(new JsonSerializer<ProcessedMap<LineString>>(),
                new ProcessedMapDeserializer<>(LineString.class));
    }

    public static Serde<ProcessedMap<String>> ProcessedMapWKT() {
        return Serdes.serdeFrom(new JsonSerializer<ProcessedMap<String>>(),
                new ProcessedMapDeserializer<>(String.class));
    }

    public static Serde<ProcessedSpat> ProcessedSpat() {
        return Serdes.serdeFrom(new JsonSerializer<ProcessedSpat>(),
                new JsonDeserializer<>(ProcessedSpat.class));
    }

    public static Serde<ProcessedBsm<Point>> ProcessedBsm() {
        return Serdes.serdeFrom(new JsonSerializer<ProcessedBsm<Point>>(), new ProcessedBsmDeserializer<>(Point.class));
    }

    public static Serde<ProcessedPsm<Point>> ProcessedPsm() {
        return Serdes.serdeFrom(new JsonSerializer<ProcessedPsm<Point>>(), new ProcessedPsmDeserializer<>(Point.class));
    }

    public static Serde<RsuIntersectionKey> RsuIntersectionKey() {
        return Serdes.serdeFrom(new JsonSerializer<RsuIntersectionKey>(),
                new JsonDeserializer<>(RsuIntersectionKey.class));
    }

    public static Serde<RsuLogKey> RsuLogKey() {
        return Serdes.serdeFrom(new JsonSerializer<RsuLogKey>(), new JsonDeserializer<>(RsuLogKey.class));
    }

    public static Serde<RsuPsmIdKey> RsuTypeIdKey() {
        return Serdes.serdeFrom(new JsonSerializer<RsuPsmIdKey>(), new JsonDeserializer<>(RsuPsmIdKey.class));
    }

    public static Serde<RsuStationIdKey> RsuStationIdKey() {
        return Serdes.serdeFrom(new JsonSerializer<>(), new JsonDeserializer<>(RsuStationIdKey.class));
    }

    public static Serde<ProcessedRTCM> ProcessedRTCM() {
        return Serdes.serdeFrom(new JsonSerializer<>(), new JsonDeserializer<>(ProcessedRTCM.class));
    }

    public static Serde<ProcessedSsm> ProcessedSsm() {
        return Serdes.serdeFrom(new JsonSerializer<>(), new JsonDeserializer<>(ProcessedSsm.class));
    }

    public static Serde<ProcessedSrm> ProcessedSrm() {
        return Serdes.serdeFrom(new JsonSerializer<>(), new JsonDeserializer<>(ProcessedSrm.class));
    }

    public static Serde<RsuVehicleIdKey> RsuVehicleIdKey() {
        return Serdes.serdeFrom(new JsonSerializer<>(), new JsonDeserializer<>(RsuVehicleIdKey.class));
    }
}
