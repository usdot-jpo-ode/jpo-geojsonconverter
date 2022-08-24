package us.dot.its.jpo.geojsonconverter.converter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import us.dot.its.jpo.geojsonconverter.GeoJsonConverterProperties;
import us.dot.its.jpo.ode.wrapper.MessageConsumer;

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

        logger.info("Starting {}", this.getClass().getSimpleName());

        // Starting the MAP geoJSON converter Kafka message consumer
        logger.info("Creating the MAP geoJSON Converter MessageConsumer");
        
        MapGeoJsonConverter mapGeoJsonConverter = new MapGeoJsonConverter(geojsonProps);
        MessageConsumer<String, String> mapJsonConsumer = MessageConsumer.defaultStringMessageConsumer(
            geojsonProps.getKafkaBrokers(), this.getClass().getSimpleName(), mapGeoJsonConverter);
        mapJsonConsumer.setName("MapJsonToGeoJsonConsumer");
        mapGeoJsonConverter.start(mapJsonConsumer, geojsonProps.getKafkaTopicOdeMapJson());
    }
}