package us.dot.its.jpo.geojsonconverter.converter;

import org.apache.kafka.streams.KafkaStreams;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import us.dot.its.jpo.geojsonconverter.GeoJsonConverterProperties;
import us.dot.its.jpo.geojsonconverter.StreamsExceptionHandler;
import us.dot.its.jpo.geojsonconverter.converter.map.MapTopology;
import us.dot.its.jpo.geojsonconverter.converter.spat.SpatTopology;
import us.dot.its.jpo.geojsonconverter.validator.MapJsonValidator;
import us.dot.its.jpo.geojsonconverter.validator.SpatJsonValidator;

/**
 * Launches JsonFromJsonConverter service
 */
@Controller
public class JsonConverterServiceController {

    private static final Logger logger = LoggerFactory.getLogger(JsonConverterServiceController.class);
    org.apache.kafka.common.serialization.Serdes bas;

    @Autowired
    public JsonConverterServiceController(GeoJsonConverterProperties geojsonProps, MapJsonValidator mapJsonValidator,
            SpatJsonValidator spatJsonValidator) {
        super();

        try {
            logger.info("Starting {}", this.getClass().getSimpleName());
            
            // MAP
            logger.info("Creating the Processed MAP Kafka-Streams topology");
            var mapTopology = MapTopology.build(geojsonProps.getKafkaTopicOdeMapJson(), geojsonProps.getKafkaTopicProcessedMap(), mapJsonValidator);
            var mapStreams = new KafkaStreams(mapTopology, geojsonProps.createStreamProperties("processedmapjson"));
            mapStreams.setUncaughtExceptionHandler(new StreamsExceptionHandler("MapStream"));
            Runtime.getRuntime().addShutdownHook(               
                new Thread(() -> {
                    try {
                        // Workaround to close streams in a finally block to satisfy sonar
                    } finally {
                        mapStreams.close();
                    }
                }));
            mapStreams.start();

            // SPaT
            logger.info("Creating the Processed SPaT Kafka-Streams topology");
            var spatTopology = SpatTopology.build(geojsonProps.getKafkaTopicOdeSpatJson(), geojsonProps.getKafkaTopicSpatGeoJson(), spatJsonValidator);
            var spatStreams = new KafkaStreams(spatTopology, geojsonProps.createStreamProperties("processedspatjson"));
            spatStreams.setUncaughtExceptionHandler(new StreamsExceptionHandler("SpatStream"));
            Runtime.getRuntime().addShutdownHook(
                new Thread(() -> {
                    try {
                        // Workaround to close streams in a finally block to satisfy sonar
                    } finally {
                        spatStreams.close();
                    }
                }));
            spatStreams.start();

            logger.info("All geoJSON conversion services started!");
        } catch (Exception e) {
            logger.error("Encountered issue with creating topologies", e);
        }
    }
}