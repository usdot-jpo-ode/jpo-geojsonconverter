package us.dot.its.jpo.geojsonconverter.converter;

import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.Topology;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import us.dot.its.jpo.geojsonconverter.GeoJsonConverterProperties;
import us.dot.its.jpo.geojsonconverter.StreamsExceptionHandler;
import us.dot.its.jpo.geojsonconverter.converter.map.MapTopology;
import us.dot.its.jpo.geojsonconverter.converter.psm.PsmTopology;
import us.dot.its.jpo.geojsonconverter.converter.rtcm.RTCMConverter;
import us.dot.its.jpo.geojsonconverter.converter.rtcm.RTCMTopology;
import us.dot.its.jpo.geojsonconverter.converter.spat.SpatTopology;
import us.dot.its.jpo.geojsonconverter.converter.bsm.BsmTopology;
import us.dot.its.jpo.geojsonconverter.converter.srm.SrmConverter;
import us.dot.its.jpo.geojsonconverter.converter.srm.SrmTopology;
import us.dot.its.jpo.geojsonconverter.converter.ssm.SsmConverter;
import us.dot.its.jpo.geojsonconverter.converter.ssm.SsmTopology;
import us.dot.its.jpo.geojsonconverter.validator.*;

/**
 * Launches JsonFromJsonConverter service
 */
@Controller
public class JsonConverterServiceController {

    private static final Logger logger = LoggerFactory.getLogger(JsonConverterServiceController.class);
    org.apache.kafka.common.serialization.Serdes bas;

    @Autowired
    public JsonConverterServiceController(GeoJsonConverterProperties geojsonProps, MapJsonValidator mapJsonValidator,
                                          SpatJsonValidator spatJsonValidator, BsmJsonValidator bsmJsonValidator, PsmJsonValidator psmJsonValidator,
                                          RTCMJsonValidator rtcmJsonValidator, RTCMConverter rtcmConverter,
                                          SrmJsonValidator srmJsonValidator, SrmConverter srmConverter,
                                          SsmJsonValidator ssmJsonValidator, SsmConverter ssmConverter) {
        super();

        try {
            logger.debug("Starting {}", this.getClass().getSimpleName());

            // MAP
            logger.info("Creating the Processed MAP Kafka-Streams topology");

            var mapTopology = MapTopology.build(geojsonProps.getKafkaTopicOdeMapJson(),
                    geojsonProps.getKafkaTopicProcessedMap(), geojsonProps.getKafkaTopicProcessedMapWKT(),
                    mapJsonValidator, geojsonProps.getGeometryOutputMode());
            var mapStreams = new KafkaStreams(mapTopology, geojsonProps.createStreamProperties("processedmapjson"));
            mapStreams.setUncaughtExceptionHandler(new StreamsExceptionHandler("MapStream"));
            Runtime.getRuntime().addShutdownHook(Thread.ofVirtual().unstarted(() -> {
                try {
                    // Workaround to close streams in a finally block to satisfy sonar
                } finally {
                    mapStreams.close();
                }
            }));
            mapStreams.start();

            // SPaT
            logger.info("Creating the Processed SPaT Kafka-Streams topology");

            var spatTopology = SpatTopology.build(geojsonProps.getKafkaTopicOdeSpatJson(),
                    geojsonProps.getKafkaTopicSpatGeoJson(), spatJsonValidator);
            var spatStreams = new KafkaStreams(spatTopology, geojsonProps.createStreamProperties("processedspatjson"));
            spatStreams.setUncaughtExceptionHandler(new StreamsExceptionHandler("SpatStream"));
            Runtime.getRuntime().addShutdownHook(Thread.ofVirtual().unstarted(() -> {
                try {
                    // Workaround to close streams in a finally block to satisfy sonar
                } finally {
                    spatStreams.close();
                }
            }));
            spatStreams.start();

            // BSM
            logger.info("Creating the Processed BSM Kafka-Streams topology");

            var bsmTopology = BsmTopology.build(geojsonProps.getKafkaTopicOdeBsmJson(),
                    geojsonProps.getKafkaTopicProcessedBsm(), bsmJsonValidator);
            var bsmStreams = new KafkaStreams(bsmTopology, geojsonProps.createStreamProperties("processedbsmjson"));
            bsmStreams.setUncaughtExceptionHandler(new StreamsExceptionHandler("BsmStream"));
            Runtime.getRuntime().addShutdownHook(Thread.ofVirtual().unstarted(() -> {
                try {
                    // Workaround to close streams in a finally block to satisfy sonar
                } finally {
                    bsmStreams.close();
                }
            }));
            bsmStreams.start();

            // PSM
            logger.info("Creating the Processed PSM Kafka-Streams topology");

            var psmTopology = PsmTopology.build(geojsonProps.getKafkaTopicOdePsmJson(),
                    geojsonProps.getKafkaTopicProcessedPsm(), psmJsonValidator);
            var psmStreams = new KafkaStreams(psmTopology, geojsonProps.createStreamProperties("processedpsmjson"));
            psmStreams.setUncaughtExceptionHandler(new StreamsExceptionHandler("PsmStream"));
            Runtime.getRuntime().addShutdownHook(Thread.ofVirtual().unstarted(() -> {
                try {
                    // Workaround to close streams in a finally block to satisfy sonar
                } finally {
                    psmStreams.close();
                }
            }));
            psmStreams.start();

            // RTCM
            logger.info("Creating the ProcessedRTCM Kafka Streams topology");
            Topology rtcmTopology = RTCMTopology.build(
                    geojsonProps.getKafkaTopicOdeRtcmJson(),
                    geojsonProps.getKafkaTopicProcessedRtcm(),
                    rtcmJsonValidator,
                    rtcmConverter);
            final var rtcmStreams = new KafkaStreams(rtcmTopology,
                    geojsonProps.createStreamProperties("processedrcmjson"));
            rtcmStreams.setUncaughtExceptionHandler(new StreamsExceptionHandler("RTCMStream"));
            Runtime.getRuntime().addShutdownHook(Thread.ofVirtual().unstarted(() -> {
                try {
                    // Workaround to close streams in a finally block to satisfy sonar
                } finally {
                    rtcmStreams.close();
                }
            }));
            rtcmStreams.start();

            // SRM
            logger.info("Creating the ProcessedSrm Kafka Streams topology");
            Topology srmTopology = SrmTopology.build(
                    geojsonProps.getKafkaTopicOdeSrmJson(),
                    geojsonProps.getKafkaTopicProcessedSrm(),
                    srmJsonValidator,
                    srmConverter);
            final var srmStreams = new KafkaStreams(srmTopology,
                    geojsonProps.createStreamProperties("processedsrmjson"));
            Runtime.getRuntime().addShutdownHook(Thread.ofVirtual().unstarted(() -> {
                try {
                    // Workaround to close streams in a finally block to satisfy sonar
                } finally {
                    srmStreams.close();
                }
            }));
            srmStreams.start();

            // SSM
            logger.info("Creating the ProcessedSsm Kafka Streams topology");
            Topology ssmTopology = SsmTopology.build(
                    geojsonProps.getKafkaTopicOdeSsmJson(),
                    geojsonProps.getKafkaTopicProcessedSsm(),
                    ssmJsonValidator,
                    ssmConverter);
            final var ssmStreams = new KafkaStreams(ssmTopology,
                    geojsonProps.createStreamProperties("processedssmjson"));
            Runtime.getRuntime().addShutdownHook(Thread.ofVirtual().unstarted(() -> {
                try {
                    // Workaround to close streams in a finally block to satisfy sonar
                } finally {
                    ssmStreams.close();
                }
            }));
            ssmStreams.start();

            logger.info("All geoJSON conversion services started!");
        } catch (Exception e) {
            logger.error("Encountered error with creating topologies: ", e);
        }
    }
}
