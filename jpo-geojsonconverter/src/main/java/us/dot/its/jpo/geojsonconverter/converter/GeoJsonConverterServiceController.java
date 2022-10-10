package us.dot.its.jpo.geojsonconverter.converter;

import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.Topology;
import org.apache.kafka.streams.errors.LogAndContinueExceptionHandler;
import org.apache.kafka.streams.processor.LogAndSkipOnInvalidTimestamp;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.util.Properties;

import us.dot.its.jpo.geojsonconverter.GeoJsonConverterProperties;
import us.dot.its.jpo.geojsonconverter.converter.map.MapTopology;

/**
 * Launches ToGeoJsonFromJsonConverter service
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

            // Starting the MAP geoJSON converter Kafka message consumer
            logger.info("Creating the MAP geoJSON Kafka-Streams topology");

            // MAP
            topology = MapTopology.build();
            streams = new KafkaStreams(topology, createStreamProperties("mapgeojson", geojsonProps.getKafkaBrokers()));
            Runtime.getRuntime().addShutdownHook(new Thread(streams::close));
            streams.start();

            logger.info("All geoJSON conversion services started!");
        } catch (Exception e) {
            logger.error("Encountered issue with creating topologies", e);
        }
    }

    public Properties createStreamProperties(String name, String bootstrapServer) {
        Properties lcsProps = new Properties();
        lcsProps.put(StreamsConfig.APPLICATION_ID_CONFIG, name);

        lcsProps.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServer);

        lcsProps.put(StreamsConfig.DEFAULT_DESERIALIZATION_EXCEPTION_HANDLER_CLASS_CONFIG,
                        LogAndContinueExceptionHandler.class.getName());
        lcsProps.put(StreamsConfig.DEFAULT_TIMESTAMP_EXTRACTOR_CLASS_CONFIG,
                        LogAndSkipOnInvalidTimestamp.class.getName());

        lcsProps.put(StreamsConfig.NUM_STREAM_THREADS_CONFIG, 2);

        lcsProps.put(StreamsConfig.producerPrefix(ProducerConfig.ACKS_CONFIG), "all");

        // Reduce cache buffering per topology to 1MB
        lcsProps.put(StreamsConfig.CACHE_MAX_BYTES_BUFFERING_CONFIG, 1 * 1024 * 1024L);

        // Decrease default commit interval. Default for 'at least once' mode of 30000ms
        // is too slow.
        lcsProps.put(StreamsConfig.COMMIT_INTERVAL_MS_CONFIG, 100);

        // All the keys are Strings in this app
        lcsProps.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass().getName());

        // Configure the state store location
        lcsProps.put(StreamsConfig.STATE_DIR_CONFIG, "/var/lib/odd/kafka-streams");

        return lcsProps;
}
}