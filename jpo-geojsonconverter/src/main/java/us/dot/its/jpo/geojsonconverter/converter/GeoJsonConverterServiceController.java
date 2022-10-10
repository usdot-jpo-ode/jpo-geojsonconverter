package us.dot.its.jpo.geojsonconverter.converter;

import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.Topology;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import us.dot.its.jpo.geojsonconverter.GeoJsonConverterProperties;
import us.dot.its.jpo.geojsonconverter.converter.map.MapTopology;

/**
 * Launches GeoJsonFromJsonConverter service
 */
@Controller
public class GeoJsonConverterServiceController {

    private static final Logger logger = LoggerFactory.getLogger(GeoJsonConverterServiceController.class);
    org.apache.kafka.common.serialization.Serdes bas;

    @Autowired
    public GeoJsonConverterServiceController(GeoJsonConverterProperties geojsonProps) {
        super();

        try {
            logger.info("Starting {}", this.getClass().getSimpleName());
            Topology topology;
            KafkaStreams streams;

            // MAP
            logger.info("Creating the MAP geoJSON Kafka-Streams topology");
            topology = MapTopology.build(geojsonProps.getKafkaTopicOdeMapJson(), geojsonProps.getKafkaTopicMapGeoJson());
            streams = new KafkaStreams(topology, geojsonProps.createStreamProperties("mapgeojson"));
            Runtime.getRuntime().addShutdownHook(new Thread(streams::close));
            streams.start();

            logger.info("All geoJSON conversion services started!");
        } catch (Exception e) {
            logger.error("Encountered issue with creating topologies", e);
        }
    }
}