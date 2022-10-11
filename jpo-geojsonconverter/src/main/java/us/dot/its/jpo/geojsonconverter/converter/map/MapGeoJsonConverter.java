package us.dot.its.jpo.geojsonconverter.converter.map;

import us.dot.its.jpo.geojsonconverter.geojson.LineString;
import us.dot.its.jpo.geojsonconverter.geojson.map.*;
import us.dot.its.jpo.ode.model.*;
import us.dot.its.jpo.ode.plugin.j2735.J2735IntersectionGeometry;
import us.dot.its.jpo.ode.plugin.j2735.J2735GenericLane;
import us.dot.its.jpo.ode.plugin.j2735.OdePosition3D;
import us.dot.its.jpo.ode.plugin.j2735.J2735NodeOffsetPointXY;
import us.dot.its.jpo.ode.plugin.j2735.J2735NodeLLmD64b;
import us.dot.its.jpo.ode.plugin.j2735.J2735Node_XY;

import java.util.ArrayList;
import java.util.List;
import java.math.BigDecimal;

import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.kstream.Transformer;
import org.apache.kafka.streams.processor.ProcessorContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MapGeoJsonConverter implements Transformer<Void, OdeMapData, KeyValue<String, MapFeatureCollection>> {
    private static final Logger logger = LoggerFactory.getLogger(MapGeoJsonConverter.class);

    @Override
    public void init(ProcessorContext arg0) {}

    /**
     * Transform an ODE MAP POJO to MAP GeoJSON POJO.
     * 
     * @param rawKey   - Void type because ODE topics have no specified key
     * @param rawValue - The raw POJO
     * @return A key value pair: the key is the MAP intersection ID concatenated with the RSU IP and the value is the POJO
     */
    @Override
    public KeyValue<String, MapFeatureCollection> transform(Void rawKey, OdeMapData rawValue) {
        try {
            OdeMapMetadata mapMetadata = (OdeMapMetadata)rawValue.getMetadata();
            OdeMapPayload mapPayload = (OdeMapPayload)rawValue.getPayload();
            J2735IntersectionGeometry intersection = mapPayload.getMap().getIntersections().getIntersections().get(0);

			MapFeatureCollection mapFeatureCollection = createFeatureCollection(intersection, mapMetadata);
            
            String key = mapMetadata.getOriginIp() + ":" + intersection.getId().getId().toString();
            logger.info("Successfully created MAP GeoJSON for " + key);
            return KeyValue.pair(key, mapFeatureCollection);
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

    public MapFeatureCollection createFeatureCollection(J2735IntersectionGeometry intersection, OdeMapMetadata metadata) {
        // Save for geometry calculations
        OdePosition3D refPoint = intersection.getRefPoint();

        List<MapFeature> mapFeatures = new ArrayList<>();
        for (int i = 0; i < intersection.getLaneSet().getLaneSet().size(); i++) {
            // Create MAP properties
            MapProperties mapProps = new MapProperties();
            mapProps.setIp(metadata.getOriginIp());
            mapProps.setOdeReceivedAt(metadata.getOdeReceivedAt());

			J2735GenericLane lane = intersection.getLaneSet().getLaneSet().get(i);
            mapProps.setLaneId(lane.getLaneID());
            mapProps.setIngressPath(lane.getLaneAttributes().getDirectionalUse().get("ingressPath"));
            mapProps.setEgressPath(lane.getLaneAttributes().getDirectionalUse().get("egressPath"));
            mapProps.setIngressApproach(lane.getIngressApproach() != null ? lane.getIngressApproach() : 0);
			mapProps.setEgressApproach(lane.getEgressApproach() != null ? lane.getEgressApproach() : 0);

            List<Integer> connectsToLaneList = new ArrayList<>();
            if (lane.getConnectsTo() != null) {
                for (int x = 0; x < lane.getConnectsTo().getConnectsTo().size(); x++) {
					Integer connectsToLane = lane.getConnectsTo().getConnectsTo().get(x).getConnectingLane().getLane();
					connectsToLaneList.add(connectsToLane);
				}
            }
            mapProps.setConnectedLanes(connectsToLaneList.toArray(new Integer[0]));

            // Create MAP geometry
            LineString geometry = createGeometry(lane, refPoint);

            // Create MAP feature and add it to the feature list
            mapFeatures.add(new MapFeature(mapProps.getLaneId(), geometry, mapProps));
        }

        return new MapFeatureCollection(mapFeatures.toArray(new MapFeature[0]));
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
}
