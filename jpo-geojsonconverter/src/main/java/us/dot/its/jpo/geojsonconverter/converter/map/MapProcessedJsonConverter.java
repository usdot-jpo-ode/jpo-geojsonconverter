package us.dot.its.jpo.geojsonconverter.converter.map;

import us.dot.its.jpo.geojsonconverter.pojos.ProcessedValidationMessage;
import us.dot.its.jpo.geojsonconverter.pojos.geojson.LineString;
import us.dot.its.jpo.geojsonconverter.pojos.geojson.connectinglanes.ConnectingLanesFeature;
import us.dot.its.jpo.geojsonconverter.pojos.geojson.connectinglanes.ConnectingLanesFeatureCollection;
import us.dot.its.jpo.geojsonconverter.pojos.geojson.connectinglanes.ConnectingLanesProperties;
import us.dot.its.jpo.geojsonconverter.pojos.geojson.map.*;
import us.dot.its.jpo.geojsonconverter.validator.JsonValidatorResult;
import us.dot.its.jpo.ode.model.*;
import us.dot.its.jpo.ode.plugin.j2735.J2735IntersectionGeometry;
import us.dot.its.jpo.ode.plugin.j2735.J2735Connection;
import us.dot.its.jpo.ode.plugin.j2735.J2735GenericLane;
import us.dot.its.jpo.ode.plugin.j2735.OdePosition3D;
import us.dot.its.jpo.ode.plugin.j2735.J2735NodeOffsetPointXY;
import us.dot.its.jpo.ode.plugin.j2735.J2735NodeLLmD64b;
import us.dot.its.jpo.ode.plugin.j2735.J2735Node_XY;

import java.util.ArrayList;
import java.util.List;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;

import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.kstream.Transformer;
import org.apache.kafka.streams.processor.ProcessorContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.networknt.schema.ValidationMessage;

public class MapProcessedJsonConverter implements Transformer<Void, DeserializedRawMap, KeyValue<String, ProcessedMapPojo>> {
    private static final Logger logger = LoggerFactory.getLogger(MapProcessedJsonConverter.class);

    @Override
    public void init(ProcessorContext arg0) {}

    /**
     * Transform an ODE MAP POJO to MAP GeoJSON POJO.
     * 
     * @param rawKey   - Void type because ODE topics have no specified key
     * @param rawValue - The raw POJO
     * @return A key value pair: the key is the RSU IP concatenated with the intersection ID and the value is the GeoJSON FeatureCollection POJO
     */
    @Override
    public KeyValue<String, ProcessedMapPojo> transform(Void rawKey, DeserializedRawMap rawValue) {
        try {
            OdeMapMetadata mapMetadata = (OdeMapMetadata)rawValue.getOdeMapOdeMapData().getMetadata();
            OdeMapPayload mapPayload = (OdeMapPayload)rawValue.getOdeMapOdeMapData().getPayload();
            J2735IntersectionGeometry intersection = mapPayload.getMap().getIntersections().getIntersections().get(0);

			MapFeatureCollection mapFeatureCollection = createFeatureCollection(mapPayload, mapMetadata, intersection, rawValue.getValidatorResults());
            ConnectingLanesFeatureCollection connectingLanesFeatureCollection = createConnectingLanesFeatureCollection(mapPayload, mapMetadata, intersection);

            ProcessedMapPojo processedMapObject = new ProcessedMapPojo();
            processedMapObject.setMapFeatureCollection(mapFeatureCollection);
            processedMapObject.setConnectingLanesFeatureCollection(connectingLanesFeatureCollection);

            String key = mapMetadata.getOriginIp() + ":" + intersection.getId().getId().toString();
            logger.info("Successfully created processed MAP for " + key);
            return KeyValue.pair(key, processedMapObject);
        } catch (Exception e) {
            String errMsg = String.format("Exception converting ODE MAP to GeoJSON! Message: %s", e.getMessage());
            logger.error(errMsg, e);
            // KafkaStreams knows to remove null responses before allowing further steps from occurring
            return KeyValue.pair("ERROR", null);
        }
    }

    @Override
    public void close() {
        // Nothing to do here
    }

    public MapFeatureCollection createFeatureCollection(OdeMapPayload mapPayload, OdeMapMetadata metadata, J2735IntersectionGeometry intersection, JsonValidatorResult validationMessages) {
        // Save for geometry calculations
        OdePosition3D refPoint = intersection.getRefPoint();

        String odeTimestamp = metadata.getOdeReceivedAt();
        ZonedDateTime odeDate = Instant.parse(odeTimestamp).atZone(ZoneId.of("UTC"));

        // Creating Validation Messages object
        List<ProcessedValidationMessage> processedSpatValidationMessages = new ArrayList<ProcessedValidationMessage>();
        for (Exception exception : validationMessages.getExceptions()){
            ProcessedValidationMessage object = new ProcessedValidationMessage();
            object.setMessage(exception.getMessage());
            object.setException(exception.getStackTrace().toString());
            processedSpatValidationMessages.add(object);
        }
        for (ValidationMessage vm : validationMessages.getValidationMessages()){
            ProcessedValidationMessage object = new ProcessedValidationMessage();
            object.setMessage(vm.getMessage());
            object.setSchemaPath(vm.getSchemaPath());
            object.setJsonPath(vm.getPath());

            processedSpatValidationMessages.add(object);
        }

        List<MapFeature> mapFeatures = new ArrayList<>();
        for (J2735GenericLane lane : intersection.getLaneSet().getLaneSet()) {
            // Create MAP properties
            MapProperties mapProps = new MapProperties();
            // mapProps.setNodes(lane.getNodeList().getNodes().getNodes()); // look at notes to do this
            mapProps.setOriginIp(metadata.getOriginIp());
            mapProps.setOdeReceivedAt(odeDate);
            mapProps.setIntersectionName(intersection.getName());
            mapProps.setRegion(intersection.getId().getRegion());
            mapProps.setIntersectionId(intersection.getId().getId());
            mapProps.setMsgIssueRevision(mapPayload.getMap().getMsgIssueRevision());
            mapProps.setRevision(intersection.getRevision());
            mapProps.setRefPoint(refPoint);
            mapProps.setLaneWidth(intersection.getLaneWidth());
            mapProps.setSpeedLimits(intersection.getSpeedLimits() != null ? intersection.getSpeedLimits().getSpeedLimits() : null);
            mapProps.setMapSource(metadata.getMapSource());
            mapProps.setTimeStamp(generateUTCTimestamp(mapPayload.getMap().getTimeStamp(), odeDate)); 
            
            mapProps.setLaneId(lane.getLaneID());
            mapProps.setLaneName(lane.getName());
            mapProps.setSharedWith(lane.getLaneAttributes().getShareWith());
            mapProps.setIngressPath(lane.getLaneAttributes().getDirectionalUse().get("ingressPath"));
            mapProps.setEgressPath(lane.getLaneAttributes().getDirectionalUse().get("egressPath"));
            mapProps.setIngressApproach(lane.getIngressApproach() != null ? lane.getIngressApproach() : 0);
			mapProps.setEgressApproach(lane.getEgressApproach() != null ? lane.getEgressApproach() : 0);
            
            mapProps.setManeuvers(lane.getManeuvers());
            mapProps.setConnectsTo(lane.getConnectsTo() != null? lane.getConnectsTo().getConnectsTo() : null);

            // Create MAP geometry
            LineString geometry = createGeometry(lane, refPoint);

            // Setting validation fields
            mapProps.setValidationMessages(processedSpatValidationMessages);
            mapProps.setCti4501Conformant(validationMessages != null ? validationMessages.isValid() : null);

            // Create MAP feature and add it to the feature list
            mapFeatures.add(new MapFeature(mapProps.getLaneId(), geometry, mapProps));
        }

        return new MapFeatureCollection(mapFeatures.toArray(new MapFeature[0]));
    }

    public ConnectingLanesFeatureCollection createConnectingLanesFeatureCollection(OdeMapPayload mapPayload, OdeMapMetadata metadata, J2735IntersectionGeometry intersection) {
        // Save for geometry calculations
        OdePosition3D refPoint = intersection.getRefPoint();

        List<ConnectingLanesFeature> lanesFeatures = new ArrayList<>();
        for (J2735GenericLane lane : intersection.getLaneSet().getLaneSet()) {
            if (lane.getLaneAttributes().getDirectionalUse().get("ingressPath") == true){
                logger.info(String.format("ingressPath value: %s", lane.getLaneAttributes().getDirectionalUse().get("ingressPath")));
                for (J2735Connection connection : lane.getConnectsTo().getConnectsTo()){
                    ConnectingLanesProperties laneProps = new ConnectingLanesProperties();

                    laneProps.setIngressLaneId(lane.getLaneID());
                    laneProps.setEgressLaneId(connection.getConnectingLane().getLane());
                    laneProps.setSignalGroupId(connection.getSignalGroup());

                    LineString geometry = createGeometry(lane, refPoint); // should only contain 2 points - first point should be the front of the intersection (INGRESS LANE) and the second point is from the EGRESS LANE
                    // loop through and make a map with the lane associated with a lat long and then iterate through

                    // "ID" field = "ingresslane-egresslane"
                    lanesFeatures.add(new ConnectingLanesFeature(laneProps.getEgressLaneId(), geometry, laneProps));
                }
            }
        }

        return new ConnectingLanesFeatureCollection(lanesFeatures.toArray(new ConnectingLanesFeature[0]));
    }

    public LineString createGeometry(J2735GenericLane lane, OdePosition3D refPoint) {
        // Calculate coordinates from reference point
        BigDecimal anchorLat = new BigDecimal(refPoint.getLatitude().toString());
        BigDecimal anchorLong = new BigDecimal(refPoint.getLongitude().toString());
        List<List<Double>> coordinatesList = new ArrayList<>();
        for (int x = 0; x < lane.getNodeList().getNodes().getNodes().size(); x++) {
            J2735NodeOffsetPointXY nodeOffset = lane.getNodeList().getNodes().getNodes().get(x).getDelta();

            if (nodeOffset.getNodeLatLon() != null) {
                J2735NodeLLmD64b nodeLatLong = nodeOffset.getNodeLatLon();
                // Complete absolute lat-long representation per J2735 
                // Lat-Long values expressed in standard SAE 1/10 of a microdegree
                BigDecimal lat = nodeLatLong.getLat().divide(new BigDecimal("10000000"));
                BigDecimal lon = nodeLatLong.getLon().divide(new BigDecimal("10000000"));

                List<Double> coordinate = new ArrayList<>();
                coordinate.add(lon.doubleValue());
                coordinate.add(lat.doubleValue());
                coordinatesList.add(coordinate);

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
                // Equations may become less accurate the further N/S the coordinate is
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

                List<Double> coordinate = new ArrayList<>();
                coordinate.add(offsetLong.doubleValue());
                coordinate.add(offsetLat.doubleValue());
                coordinatesList.add(coordinate);

                // Reset the anchor point for following offset nodes
                anchorLat = new BigDecimal(offsetLat.toString());
                anchorLong = new BigDecimal(offsetLong.toString());
            }
        }

        double[][] coordinatesArray = coordinatesList.stream()
                        .map(l -> l.stream().mapToDouble(Double::doubleValue).toArray())
                        .toArray(double[][]::new);

        return new LineString(coordinatesArray);
    }

    public ZonedDateTime generateUTCTimestamp(Integer moy, ZonedDateTime odeDate){ //2022-10-31T15:40:26.687292Z
        ZonedDateTime date = null;
        try {
            int year = odeDate.getYear();
            String dateString;
            long milliseconds;
            if (moy != null){
                milliseconds = moy*60*1000; // milliseconds from beginning of year
                dateString = String.format("%d-01-01T00:00:00.00Z", year);
                date = Instant.parse(dateString).plusMillis(milliseconds).atZone(ZoneId.of("UTC"));
            } else {
                date = odeDate;
            }
                        
        } catch (Exception e) {
            logger.error("Failed to generateUTCTimestamp - SpatProcessedJsonConverter", e);
        }
        
        return date;
    }

}
