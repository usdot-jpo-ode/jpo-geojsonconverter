package us.dot.its.jpo.geojsonconverter.converter.map;

import us.dot.its.jpo.geojsonconverter.geojson.LineString;
import us.dot.its.jpo.geojsonconverter.geojson.map.*;
import us.dot.its.jpo.ode.model.*;
import us.dot.its.jpo.ode.plugin.j2735.J2735IntersectionGeometry;
import us.dot.its.jpo.ode.plugin.j2735.J2735GenericLane;
import us.dot.its.jpo.ode.plugin.j2735.OdePosition3D;

import java.util.ArrayList;
import java.util.List;

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
            
            String rsuIp = mapMetadata.getOriginIp();
            String intersectionId = intersection.getId().getId().toString();
            return KeyValue.pair(rsuIp + ":" + intersectionId, mapFeatureCollection);
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

            // Create MAP geometry
            LineString geometry = createGeometry(lane, refPoint);

            // Create MAP feature and add it to the feature list
            mapFeatures.add(new MapFeature(mapProps.getLaneId(), geometry, mapProps));
        }

        return new MapFeatureCollection((MapFeature[])mapFeatures.toArray());
    }

    public LineString createGeometry(J2735GenericLane lane, OdePosition3D refPoint) {
        double[][] coordinates = new double[][] {};
        return new LineString(coordinates);
    }
}
