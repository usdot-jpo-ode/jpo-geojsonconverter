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
import us.dot.its.jpo.geojsonconverter.validator.JsonValidatorResult;
import us.dot.its.jpo.geojsonconverter.validator.MapJsonValidator;
import us.dot.its.jpo.ode.model.OdeMapMetadata;
import us.dot.its.jpo.ode.model.OdeMapPayload;
import us.dot.its.jpo.ode.plugin.j2735.J2735IntersectionGeometry;
import us.dot.its.jpo.ode.plugin.j2735.J2735GenericLane;
import us.dot.its.jpo.ode.plugin.j2735.OdePosition3D;
import us.dot.its.jpo.ode.plugin.j2735.J2735NodeOffsetPointXY;
import us.dot.its.jpo.ode.plugin.j2735.J2735NodeLLmD64b;
import us.dot.its.jpo.ode.plugin.j2735.J2735Node_XY;
import us.dot.its.jpo.ode.util.JsonUtils;
import us.dot.its.jpo.ode.wrapper.AbstractSubscriberProcessor;
import us.dot.its.jpo.ode.wrapper.MessageProducer;

public class MapGeoJsonConverter extends AbstractSubscriberProcessor<String, String> {
  private Logger logger = LoggerFactory.getLogger(this.getClass());

	private GeoJsonConverterProperties geojsonProperties;
	private MessageProducer<String, String> geoJsonProducer;
	private MapJsonValidator mapJsonValidator;

	public MapGeoJsonConverter(GeoJsonConverterProperties geojsonProps, MapJsonValidator mapJsonValidator) {
		super();
		this.geojsonProperties = geojsonProps;
		this.geoJsonProducer = MessageProducer.defaultStringMessageProducer(geojsonProperties.getKafkaBrokers(),
			geojsonProperties.getKafkaProducerType(), geojsonProperties.getKafkaTopicsDisabledSet());
		this.mapJsonValidator = mapJsonValidator;
	}

	@Override
	public Object process(String consumedData) {
		try {
			JsonValidatorResult validationResult = mapJsonValidator.validate(consumedData);
			logger.info("MapJsonValidator result: isValid = {}", validationResult.isValid());
			if (!validationResult.isValid()) {
				for (var exception : validationResult.getExceptions()) {
					logger.error("JsonProcessingException: {}", exception.getMessage());
				}
				for (var validationMessage : validationResult.getValidationMessages()) {
					logger.error("MAP JSON Validation Message: {}", validationMessage.getMessage());
				}
			}

			ObjectMapper mapper = new ObjectMapper();
			JsonNode actualObj = mapper.readTree(consumedData);

			// Deserialize the metadata
			JsonNode metadataNode = actualObj.get("metadata");
			String metadataString = metadataNode.toString();
			OdeMapMetadata metadataObject = (OdeMapMetadata) JsonUtils.fromJson(metadataString, OdeMapMetadata.class);
			String rsuIp = metadataObject.getOriginIp();

			// Deserialize the payload
			JsonNode payloadNode = actualObj.get("payload");
			String payloadString = payloadNode.toString();
			OdeMapPayload mapPayload = (OdeMapPayload) JsonUtils.fromJson(payloadString, OdeMapPayload.class);

			// Modify to GeoJSON
			List<JSONObject> mapGeoJsonList = new ArrayList<>();
			for (int i = 0; i < mapPayload.getMap().getIntersections().getIntersections().size(); i++) {
				JSONObject geoJson = createGeoJsonFromIntersection(mapPayload.getMap().getIntersections().getIntersections().get(i), rsuIp);
				mapGeoJsonList.add(geoJson);
			}

			// Publish to new GeoJSON Kafka topic
			for (JSONObject obj : mapGeoJsonList) { 
				geoJsonProducer.send(geojsonProperties.getKafkaTopicMapGeoJson(), getRecord().key(), obj.toString());
			}
		} catch (Exception e) {
			logger.error("Failed to convert received data to GeoJSON: " + consumedData, e);
		}
		return null;
  }

	public JSONObject createGeoJsonFromIntersection(J2735IntersectionGeometry intersection, String rsuIp) {
		JSONObject intersectionGeoJson = new JSONObject();
		intersectionGeoJson.put("type", "FeatureCollection");

		OdePosition3D refPoint = intersection.getRefPoint();

		JSONArray laneList = new JSONArray();
		for (int i = 0; i < intersection.getLaneSet().getLaneSet().size(); i++) {
			J2735GenericLane lane = intersection.getLaneSet().getLaneSet().get(i);

			// Create the lane feature as a single JSON object
			JSONObject laneGeoJson = new JSONObject();
			laneGeoJson.put("type", "Feature");

			// Create the lane feature properties ---------------------
			JSONObject propertiesGeoJson = new JSONObject();
			propertiesGeoJson.put("ip", rsuIp);
			propertiesGeoJson.put("laneID", lane.getLaneID());

			// Add ingress/egress data
			propertiesGeoJson.put("ingressPath", lane.getLaneAttributes().getDirectionalUse().get("ingressPath"));
			propertiesGeoJson.put("egressPath", lane.getLaneAttributes().getDirectionalUse().get("egressPath"));

			if (lane.getIngressApproach() != null)
				propertiesGeoJson.put("ingressApproach", lane.getIngressApproach());
			else
				propertiesGeoJson.put("ingressApproach", 0);
			
			if (lane.getEgressApproach() != null)
				propertiesGeoJson.put("egressApproach", lane.getEgressApproach());
			else
				propertiesGeoJson.put("egressApproach", 0);

			// Add connected lanes
			JSONArray connectsToList = new JSONArray();
			if (lane.getConnectsTo() != null) {
				for (int x = 0; x < lane.getConnectsTo().getConnectsTo().size(); x++) {
					Integer connectsToLane = lane.getConnectsTo().getConnectsTo().get(x).getConnectingLane().getLane();
					connectsToList.put(connectsToLane);
				}
			}
			propertiesGeoJson.put("connectedLanes", connectsToList);
			laneGeoJson.put("properties", propertiesGeoJson);

			// Create the lane feature geometry ---------------------
			JSONObject geometryGeoJson = new JSONObject();
			geometryGeoJson.put("type", "LineString");

			// Calculate coordinates
			BigDecimal anchorLat = new BigDecimal(refPoint.getLatitude().toString());
			BigDecimal anchorLong = new BigDecimal(refPoint.getLongitude().toString());
			JSONArray coordinatesList = new JSONArray();
			for (int x = 0; x < lane.getNodeList().getNodes().getNodes().size(); x++) {
				J2735NodeOffsetPointXY nodeOffset = lane.getNodeList().getNodes().getNodes().get(x).getDelta();

				if (nodeOffset.getNodeLatLon() != null) {
					J2735NodeLLmD64b nodeLatLong = nodeOffset.getNodeLatLon();
					// Complete absolute lat-long representation per J2735 
					// Lat-Long values expressed in standard SAE 1/10 of a microdegree
					BigDecimal lat = nodeLatLong.getLat().divide(new BigDecimal("10000000"));
					BigDecimal lon = nodeLatLong.getLon().divide(new BigDecimal("10000000"));

					JSONArray coordinate = new JSONArray();
					coordinate.put(lon.doubleValue());
					coordinate.put(lat.doubleValue());
					coordinatesList.put(coordinate);

					// Reset the anchor point for following offset nodes
					// J2735 is not clear if only one of these nodelatlon types is allowed in the lane path nodes
					anchorLat = new BigDecimal(lat.toString());
					anchorLong = new BigDecimal(lon.toString());
				}
				else {
					// Get the NodeXY object or skip node if entirely null
					J2735Node_XY nodexy = null;
					if (nodeOffset.getNodeXY1() != null)
						nodexy = nodeOffset.getNodeXY1();
					else if (nodeOffset.getNodeXY2() != null)
						nodexy = nodeOffset.getNodeXY2();
					else if (nodeOffset.getNodeXY3() != null)
						nodexy = nodeOffset.getNodeXY3();
					else if (nodeOffset.getNodeXY4() != null)
						nodexy = nodeOffset.getNodeXY4();
					else if (nodeOffset.getNodeXY5() != null)
						nodexy = nodeOffset.getNodeXY5();
					else if (nodeOffset.getNodeXY6() != null)
						nodexy = nodeOffset.getNodeXY6();
					else
						continue;
					
					// Calculate offset lon,lat values
					// Equations may become less accurate the futher N/S the coordinate is
					double offsetX = nodexy.getX().doubleValue();
					double offsetY = nodexy.getY().doubleValue();

					// (offsetX * 0.01) / (math.cos((Math.PI / 180.0) * anchorLat) * 111111.0)
					// Step 1. (offsetX * 0.01)
					// Step 2. (math.cos((Math.PI/180.0) * anchorLat) * 111111.0)
					// Step 3. Step 1 / Step 2
					double offsetX_step1 = offsetX * 0.01;
					double offsetX_step2 = Math.cos(((double)(Math.PI / 180.0)) * anchorLat.doubleValue()) * 111111.0;
					double offsetXDegrees = offsetX_step1 / offsetX_step2;
					
					// (offsetY * 0.01) / 111111.0
					double offsetYDegrees = (offsetY * 0.01) / 111111.0;

					// return (reference_point[0] + dx_deg, reference_point[1] + dy_deg)
					BigDecimal offsetLong = new BigDecimal(String.valueOf(anchorLong.doubleValue() + offsetXDegrees));
					BigDecimal offsetLat = new BigDecimal(String.valueOf(anchorLat.doubleValue() + offsetYDegrees));

					JSONArray coordinate = new JSONArray();
					coordinate.put(offsetLong.doubleValue());
					coordinate.put(offsetLat.doubleValue());
					coordinatesList.put(coordinate);

					// Reset the anchor point for following offset nodes
					anchorLat = new BigDecimal(offsetLat.toString());
					anchorLong = new BigDecimal(offsetLong.toString());
				}
			}

			geometryGeoJson.put("coordinates", coordinatesList);
			laneGeoJson.put("geometry", geometryGeoJson);

			// Finally, add lane feature to feature list ---------------------
			laneList.put(laneGeoJson);
		}

		intersectionGeoJson.put("features", laneList);

		return intersectionGeoJson;
	}
}