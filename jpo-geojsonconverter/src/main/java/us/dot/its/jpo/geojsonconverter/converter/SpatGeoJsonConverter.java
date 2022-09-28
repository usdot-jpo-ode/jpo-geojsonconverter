package us.dot.its.jpo.geojsonconverter.converter;

import java.util.ArrayList;
import java.util.List;
import java.math.BigDecimal;

import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import us.dot.its.jpo.geojsonconverter.GeoJsonConverterProperties;
import us.dot.its.jpo.ode.model.OdeSpatMetadata;
import us.dot.its.jpo.ode.model.OdeSpatPayload;
import us.dot.its.jpo.ode.util.JsonUtils;
import us.dot.its.jpo.ode.wrapper.AbstractSubscriberProcessor;
import us.dot.its.jpo.ode.wrapper.MessageProducer;

public class SpatGeoJsonConverter extends AbstractSubscriberProcessor<String, String> {
  private Logger logger = LoggerFactory.getLogger(this.getClass());

	private GeoJsonConverterProperties geojsonProperties;
	private MessageProducer<String, String> geoJsonProducer;

	public SpatGeoJsonConverter(GeoJsonConverterProperties geojsonProps) {
		super();
		this.geojsonProperties = geojsonProps;
		this.geoJsonProducer = MessageProducer.defaultStringMessageProducer(geojsonProperties.getKafkaBrokers(),
			geojsonProperties.getKafkaProducerType(), geojsonProperties.getKafkaTopicsDisabledSet());
	}

	@Override
	public Object process(String consumedData) {
		try {
			ObjectMapper mapper = new ObjectMapper();
			JsonNode actualObj = mapper.readTree(consumedData);

			// Deserialize the metadata
			JsonNode metadataNode = actualObj.get("metadata");
			String metadataString = metadataNode.toString();
			OdeSpatMetadata metadataObject = (OdeSpatMetadata) JsonUtils.fromJson(metadataString, OdeSpatMetadata.class);
			String rsuIp = metadataObject.getOriginIp();

			// Deserialize the payload
			JsonNode payloadNode = actualObj.get("payload");
			String payloadString = payloadNode.toString();
			OdeSpatPayload spatPayload = (OdeSpatPayload) JsonUtils.fromJson(payloadString, OdeSpatPayload.class);

			// Modify to GeoJSON
			

			// Publish to new GeoJSON Kafka topic
		} catch (Exception e) {
			logger.error("Failed to convert received data to GeoJSON: " + consumedData, e);
		}
		return null;
  }
}