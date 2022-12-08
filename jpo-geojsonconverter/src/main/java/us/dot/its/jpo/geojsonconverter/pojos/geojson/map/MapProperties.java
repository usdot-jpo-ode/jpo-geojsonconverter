package us.dot.its.jpo.geojsonconverter.pojos.geojson.map;

import java.util.List;
import java.util.Objects;

import us.dot.its.jpo.geojsonconverter.DateJsonMapper;
import us.dot.its.jpo.ode.plugin.j2735.J2735BitString;
import us.dot.its.jpo.ode.plugin.j2735.J2735Connection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class MapProperties {
    private static Logger logger = LoggerFactory.getLogger(MapProperties.class);

    private List<MapNode> nodes;
    private Integer laneId;
    private String laneName;
    private J2735BitString sharedWith;  // enum is of type J2735LaneSharing
    private Integer egressApproach;
    private Integer ingressApproach;
    private Boolean ingressPath;
    private Boolean egressPath;
    private J2735BitString maneuvers;  // enum is of type J2735AllowedManeuvers
    private List<J2735Connection> connectsTo;

    public void setNodes(List<MapNode> nodes) {
        this.nodes = nodes;
    }

    public List<MapNode> getNodes() {
        return this.nodes;
    }

    public void setLaneId(Integer laneId) {
        this.laneId = laneId;
    }

    public Integer getLaneId() {
        return this.laneId;
    }

    public void setLaneName(String laneName) {
        this.laneName = laneName;
    }

    public String getLaneName() {
        return this.laneName;
    }

    public void setSharedWith(J2735BitString sharedWith) {
        this.sharedWith = sharedWith;
    }

    public J2735BitString getSharedWith() {
        return this.sharedWith;
    }

    public void setEgressApproach(Integer egressApproach) {
        this.egressApproach = egressApproach;
    }

    public Integer getEgressApproach() {
        return this.egressApproach;
    }

    public void setIngressApproach(Integer ingressApproach) {
        this.ingressApproach = ingressApproach;
    }

    public Integer getIngressApproach() {
        return this.ingressApproach;
    }

    public void setIngressPath(Boolean ingressPath) {
        this.ingressPath = ingressPath;
    }

    public Boolean getIngressPath() {
        return this.ingressPath;
    }

    public void setEgressPath(Boolean egressPath) {
        this.egressPath = egressPath;
    }

    public Boolean getEgressPath() {
        return this.egressPath;
    }

    public void setManeuvers(J2735BitString maneuvers) {
        this.maneuvers = maneuvers;
    }

    public J2735BitString getManeuvers() {
        return this.maneuvers;
    }

    public void setConnectsTo(List<J2735Connection> connectsTo) {
        this.connectsTo = connectsTo;
    }

    public List<J2735Connection> getConnectsTo() {
        return this.connectsTo;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof MapProperties)) {
            return false;
        }
        MapProperties mapProperties = (MapProperties) o;
        return Objects.equals(nodes, mapProperties.nodes) && Objects.equals(laneId, mapProperties.laneId) && Objects.equals(laneName, mapProperties.laneName) && Objects.equals(sharedWith, mapProperties.sharedWith) && Objects.equals(egressApproach, mapProperties.egressApproach) && Objects.equals(ingressApproach, mapProperties.ingressApproach) && Objects.equals(ingressPath, mapProperties.ingressPath) && Objects.equals(egressPath, mapProperties.egressPath) && Objects.equals(maneuvers, mapProperties.maneuvers) && Objects.equals(connectsTo, mapProperties.connectsTo);
    }

    @Override
    public int hashCode() {
        return Objects.hash(nodes, laneId, laneName, sharedWith, egressApproach, ingressApproach, ingressPath, egressPath, maneuvers, connectsTo);
    }

    @Override
    public String toString() {
        ObjectMapper mapper = DateJsonMapper.getInstance();
        String testReturn = "";
        try {
            testReturn = (mapper.writeValueAsString(this));
        } catch (JsonProcessingException e) {
            logger.error(e.getMessage(), e);
        }
        return testReturn;
    }
}
