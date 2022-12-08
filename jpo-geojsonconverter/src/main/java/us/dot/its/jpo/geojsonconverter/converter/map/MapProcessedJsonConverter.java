package us.dot.its.jpo.geojsonconverter.converter.map;

import us.dot.its.jpo.geojsonconverter.pojos.ProcessedValidationMessage;
import us.dot.its.jpo.geojsonconverter.pojos.geojson.LineString;
import us.dot.its.jpo.geojsonconverter.pojos.geojson.connectinglanes.*;
import us.dot.its.jpo.geojsonconverter.pojos.geojson.map.*;
import us.dot.its.jpo.geojsonconverter.validator.JsonValidatorResult;
import us.dot.its.jpo.ode.model.*;
import us.dot.its.jpo.ode.plugin.j2735.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;

import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.kstream.Transformer;
import org.apache.kafka.streams.processor.ProcessorContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.networknt.schema.ValidationMessage;

public class MapProcessedJsonConverter implements Transformer<Void, DeserializedRawMap, KeyValue<String, ProcessedMap>> {
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
    public KeyValue<String, ProcessedMap> transform(Void rawKey, DeserializedRawMap rawValue) {
        try {
            OdeMapMetadata mapMetadata = (OdeMapMetadata)rawValue.getOdeMapOdeMapData().getMetadata();
            OdeMapPayload mapPayload = (OdeMapPayload)rawValue.getOdeMapOdeMapData().getPayload();
            J2735IntersectionGeometry intersection = mapPayload.getMap().getIntersections().getIntersections().get(0);

            MapSharedProperties sharedProps = createProperties(mapPayload, mapMetadata, intersection, rawValue.getValidatorResults());
			MapFeatureCollection mapFeatureCollection = createFeatureCollection(intersection);
            ConnectingLanesFeatureCollection connectingLanesFeatureCollection = createConnectingLanesFeatureCollection(mapMetadata, intersection);

            ProcessedMap processedMapObject = new ProcessedMap();
            processedMapObject.setMapFeatureCollection(mapFeatureCollection);
            processedMapObject.setConnectingLanesFeatureCollection(connectingLanesFeatureCollection);
            processedMapObject.setProperties(sharedProps);

            String key = mapMetadata.getOriginIp() + ":" + intersection.getId().getId().toString();
            String logMsg = String.format("Successfully created processed MAP for device: %s", key);
            logger.info(logMsg);
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

    public MapSharedProperties createProperties(OdeMapPayload mapPayload, OdeMapMetadata metadata, J2735IntersectionGeometry intersection, JsonValidatorResult validationMessages) {
        // Save for geometry calculations
        OdePosition3D refPoint = intersection.getRefPoint();

        String odeTimestamp = metadata.getOdeReceivedAt();
        ZonedDateTime odeDate = Instant.parse(odeTimestamp).atZone(ZoneId.of("UTC"));

        // Creating Validation Messages object
        List<ProcessedValidationMessage> processedSpatValidationMessages = new ArrayList<ProcessedValidationMessage>();
        for (Exception exception : validationMessages.getExceptions()){
            ProcessedValidationMessage object = new ProcessedValidationMessage();
            object.setMessage(exception.getMessage());
            object.setException(Arrays.toString(exception.getStackTrace()));
            processedSpatValidationMessages.add(object);
        }
        for (ValidationMessage vm : validationMessages.getValidationMessages()){
            ProcessedValidationMessage object = new ProcessedValidationMessage();
            object.setMessage(vm.getMessage());
            object.setSchemaPath(vm.getSchemaPath());
            object.setJsonPath(vm.getPath());

            processedSpatValidationMessages.add(object);
        }
        MapSharedProperties sharedProps = new MapSharedProperties();

        sharedProps.setOriginIp(metadata.getOriginIp());
        sharedProps.setOdeReceivedAt(odeDate);
        sharedProps.setIntersectionName(intersection.getName());
        sharedProps.setRegion(intersection.getId().getRegion());
        sharedProps.setIntersectionId(intersection.getId().getId());
        sharedProps.setMsgIssueRevision(mapPayload.getMap().getMsgIssueRevision());
        sharedProps.setRevision(intersection.getRevision());
        sharedProps.setRefPoint(refPoint);
        sharedProps.setLaneWidth(intersection.getLaneWidth());
        sharedProps.setSpeedLimits(intersection.getSpeedLimits() != null ? intersection.getSpeedLimits().getSpeedLimits() : null);
        sharedProps.setMapSource(metadata.getMapSource());
        sharedProps.setTimeStamp(generateUTCTimestamp(mapPayload.getMap().getTimeStamp(), odeDate)); 
        // Setting validation fields
        sharedProps.setValidationMessages(processedSpatValidationMessages);
        sharedProps.setCti4501Conformant(validationMessages.isValid());

        return sharedProps;
    }

    public MapFeatureCollection createFeatureCollection(J2735IntersectionGeometry intersection) {
        // Save for geometry calculations
        OdePosition3D refPoint = intersection.getRefPoint();

        List<MapFeature> mapFeatures = new ArrayList<>();
        for (J2735GenericLane lane : intersection.getLaneSet().getLaneSet()) {
            // Create MAP properties
            MapProperties mapProps = new MapProperties();
            if (lane.getNodeList().getNodes() != null)
                mapProps.setNodes(nodeConversionList(lane.getNodeList().getNodes().getNodes()));            
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

            // Create MAP feature and add it to the feature list
            mapFeatures.add(new MapFeature(mapProps.getLaneId(), geometry, mapProps));
        }

        return new MapFeatureCollection(mapFeatures.toArray(new MapFeature[0]));
    }

    public ConnectingLanesFeatureCollection createConnectingLanesFeatureCollection(OdeMapMetadata metadata, J2735IntersectionGeometry intersection) {
        // Save for geometry calculations
        OdePosition3D refPoint = intersection.getRefPoint();

        HashMap<Integer, double[]> lanePoints = new HashMap<Integer, double[]>();
        for (J2735GenericLane lane : intersection.getLaneSet().getLaneSet()) {
            if (!lanePoints.containsKey(lane.getLaneID())){
                LineString laneGeometry = createGeometry(lane, refPoint);
                double coordinate[] = {laneGeometry.getCoordinates()[0][0],laneGeometry.getCoordinates()[0][1]};
                lanePoints.put(lane.getLaneID(), coordinate);
            }
        }

        List<ConnectingLanesFeature> lanesFeatures = new ArrayList<>();
        for (J2735GenericLane lane : intersection.getLaneSet().getLaneSet()) {
            if (lane.getLaneAttributes().getDirectionalUse().get("ingressPath") == true){
                double[] laneCoordinates = lanePoints.get(lane.getLaneID()); //first poiunt
                for (J2735Connection connection : lane.getConnectsTo().getConnectsTo()){
                    ConnectingLanesProperties laneProps = new ConnectingLanesProperties();
                    laneProps.setIngressLaneId(lane.getLaneID());
                    laneProps.setEgressLaneId(connection.getConnectingLane().getLane());
                    laneProps.setSignalGroupId(connection.getSignalGroup());

                    // Point
                    double[] connectionCoordinates = lanePoints.get(connection.getConnectingLane().getLane()); // last point
                    double[][] coordinates = new double[][] {laneCoordinates, connectionCoordinates};
                    LineString geometry = new LineString(coordinates);

                    String id = String.format("%s-%s", laneProps.getIngressLaneId(), laneProps.getEgressLaneId());
                    lanesFeatures.add(new ConnectingLanesFeature(id, geometry, laneProps));
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
                offsetLong = offsetLong.setScale(7, RoundingMode.HALF_UP);
                offsetLat = offsetLat.setScale(7, RoundingMode.HALF_UP);

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
                milliseconds = moy*60L*1000L; // milliseconds from beginning of year
                dateString = String.format("%d-01-01T00:00:00.00Z", year);
                date = Instant.parse(dateString).plusMillis(milliseconds).atZone(ZoneId.of("UTC"));
            } else {
                date = odeDate;
            }
                        
        } catch (Exception e) {
            String errMsg = String.format("Failed to generateUTCTimestamp - SpatProcessedJsonConverter. Message: %s", e.getMessage());
            logger.error(errMsg, e);
        }
        
        return date;
    }

    public List<MapNode> nodeConversionList(List<J2735NodeXY> nodeXYs){ //2022-10-31T15:40:26.687292Z
        List<MapNode> mapNodes = new ArrayList<MapNode>();
        try{
            for (J2735NodeXY nodeXy : nodeXYs){
                MapNode mapNode = new MapNode();
                mapNode.setDWidth(nodeXy.getAttributes() != null ? nodeXy.getAttributes().getdWidth() : null);
                mapNode.setDElevation(nodeXy.getAttributes() != null ? nodeXy.getAttributes().getdElevation() : null);

                Integer offsetX = null;
                Integer offsetY = null;
                J2735Node_XY nodexy = null;
                J2735NodeLLmD64b nodeLatLong = null;

                J2735NodeOffsetPointXY nodeOffset = nodeXy.getDelta();
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
                else if (nodeOffset.getNodeLatLon() != null)
                    nodeLatLong = nodeOffset.getNodeLatLon();
                else
                    continue;
                if (nodexy != null) {
                    offsetX = nodexy.getX().intValue();
                    offsetY = nodexy.getY().intValue();
                }
                else if (nodeLatLong != null) {
                    offsetX = nodeLatLong.getLon().divide(new BigDecimal("10000000")).intValue();
                    offsetY = nodeLatLong.getLat().divide(new BigDecimal("10000000")).intValue();
                }
                Integer[] delta = {offsetX,offsetY};
                mapNode.setDelta(delta);
                mapNodes.add(mapNode);
            }
        } catch (Exception e) {
            String errMsg = String.format("Failed to nodeConversionList - SpatProcessedJsonConverter. Message: %s", e.getMessage());
            logger.error(errMsg, e);
        }
        return mapNodes;
    }

}
