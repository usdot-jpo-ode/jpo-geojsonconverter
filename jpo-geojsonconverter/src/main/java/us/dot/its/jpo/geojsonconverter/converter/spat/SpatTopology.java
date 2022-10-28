package us.dot.its.jpo.geojsonconverter.converter.spat;

import java.util.ArrayList;
import java.util.List;

import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.common.utils.Bytes;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.Topology;
import org.apache.kafka.streams.kstream.Consumed;
import org.apache.kafka.streams.kstream.Joined;
import org.apache.kafka.streams.kstream.Produced;
import org.apache.kafka.streams.state.KeyValueStore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.KTable;
import org.apache.kafka.streams.kstream.Materialized;

import us.dot.its.jpo.geojsonconverter.serialization.JsonSerdes;
import us.dot.its.jpo.geojsonconverter.validator.JsonValidatorResult;
import us.dot.its.jpo.geojsonconverter.validator.SpatJsonValidator;
import us.dot.its.jpo.geojsonconverter.geojson.Point;
import us.dot.its.jpo.geojsonconverter.geojson.map.MapFeature;
import us.dot.its.jpo.geojsonconverter.geojson.map.MapFeatureCollection;
import us.dot.its.jpo.geojsonconverter.geojson.spat.SpatFeature;
import us.dot.its.jpo.geojsonconverter.geojson.spat.SpatFeatureCollection;
import us.dot.its.jpo.ode.model.OdeSpatData;

/**
 * Kafka Streams Topology builder for processing SPaT messages from
 * ODE SPaT JSON -> SPaT GeoJSON
 */
public class SpatTopology {

    private static final Logger logger = LoggerFactory.getLogger(SpatTopology.class);

    public static Topology build(String spatOdeJsonTopic, String spatGeoJsonTopic, String mapGeoJsonTopic,
            SpatJsonValidator spatJsonValidator) {
        StreamsBuilder builder = new StreamsBuilder();

        // Stream for raw SPAT messages
        KStream<Void, Bytes> rawOdeSpatStream =
            builder.stream(
                spatOdeJsonTopic,
                Consumed.with(
                    Serdes.Void(),  // Raw topic has no key
                    Serdes.Bytes()  // Raw JSON bytes
                )
            ).peek((key, value) -> {
                logger.info("Received raw spat");
            });

        // Validate the JSON and write validation errors to the log at warn level
        // Passes the raw JSON along unchanged, even if there are validation errors.
        KStream<Void, Bytes> validatedOdeSpatStream = 
            rawOdeSpatStream.peek(
                (Void key, Bytes value) -> {
                    JsonValidatorResult validationResults = spatJsonValidator.validate(value.get());
                    if (validationResults.isValid()) {
                        logger.info(validationResults.describeResults());
                    } else {
                        logger.warn(validationResults.describeResults());
                    }
                }
            );

        // Deserialize the raw JSON bytes to SPAT after validation
        KStream<Void, OdeSpatData> odeSpatStream =
                validatedOdeSpatStream.mapValues(
                    (Bytes value) -> {
                        return JsonSerdes.OdeSpat().deserializer().deserialize(spatOdeJsonTopic, value.get());
                    }
                );

        // KTable MapGeoJSON
        KTable<String, MapFeatureCollection> mapFeatureCollectionTable = 
            builder.table(
                mapGeoJsonTopic,
                Consumed.with(Serdes.String(), JsonSerdes.MapGeoJson()),
                Materialized.<String, MapFeatureCollection, KeyValueStore<Bytes, byte[]>> as ("mapgeojson-store")
                    .withKeySerde(Serdes.String()).withValueSerde(JsonSerdes.MapGeoJson())
                );

        // Convert ODE SPaT to GeoJSON
        KStream<String, SpatFeatureCollection> geoJsonSpatStream =
            odeSpatStream.transform(
                () -> new SpatGeoJsonConverter()
            );

        // Join SPaT GeoJSON stream with the MapGeoJSON table to populate feature geometry
        KStream<String, SpatFeatureCollection> joinedGeoJsonSpatStream = 
            geoJsonSpatStream.join(mapFeatureCollectionTable, (spatGeoJson, mapGeoJson) -> {
                List<SpatFeature> spatFeatures = new ArrayList<>();

                for (SpatFeature spatFeature : spatGeoJson.getFeatures()) {
                    for(int i = 0; i < mapGeoJson.getFeatures().length; i++) {
                        MapFeature mapFeature = mapGeoJson.getFeatures()[i];
                        if (mapFeature.getProperties().getLaneId() == spatFeature.getProperties().getSignalGroupId()) {
                            // Create a new SPaT feature with the associated MAP lane's LineString's first coordinate set
                            SpatFeature newSpatFeature = new SpatFeature(
                                spatFeature.getId(), 
                                new Point(mapFeature.getGeometry().getCoordinates()[0]), 
                                spatFeature.getProperties());
                            spatFeatures.add(newSpatFeature);
                            break;
                        }
                        // If a signalStatus does not correlate to a defined lane ID, keep the geometry as null 
                        else if (i+1 == mapGeoJson.getFeatures().length) {
                            spatFeatures.add(spatFeature);
                        }
                    }
                }

                return new SpatFeatureCollection(spatFeatures.toArray(new SpatFeature[0]));
            }, 
            Joined.<String, SpatFeatureCollection, MapFeatureCollection>as("geojson-joined").withKeySerde(Serdes.String())
                .withValueSerde(JsonSerdes.SpatGeoJson()).withOtherValueSerde(JsonSerdes.MapGeoJson()));
        
        joinedGeoJsonSpatStream.to(
            // Push the joined GeoJSON stream back out to the SPaT GeoJSON topic 
            spatGeoJsonTopic, 
            Produced.with(Serdes.String(),
                    JsonSerdes.SpatGeoJson()));
        
        return builder.build();
    }
}
