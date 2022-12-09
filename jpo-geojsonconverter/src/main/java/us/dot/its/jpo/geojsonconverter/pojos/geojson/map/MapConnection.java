package us.dot.its.jpo.geojsonconverter.pojos.geojson.map;

import java.util.Objects;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import us.dot.its.jpo.geojsonconverter.DateJsonMapper;
import us.dot.its.jpo.ode.plugin.j2735.J2735IntersectionReferenceID;

public class MapConnection {
    private static Logger logger = LoggerFactory.getLogger(MapConnection.class);

    private Integer lane;
	private J2735IntersectionReferenceID remoteIntersection;
	private Integer signalGroup;
	private Integer userClass;
	private Integer connectionID;

    public void setLane(Integer lane) {
        this.lane = lane;
    }

    public Integer getLane() {
        return this.lane;
    }

    public void setRemoteIntersection(J2735IntersectionReferenceID remoteIntersection) {
        this.remoteIntersection = remoteIntersection;
    }

    public J2735IntersectionReferenceID getRemoteIntersection() {
        return this.remoteIntersection;
    }

    public void setSignalGroup(Integer signalGroup) {
        this.signalGroup = signalGroup;
    }

    public Integer getSignalGroup() {
        return this.signalGroup;
    }

    public void setUserClass(Integer userClass) {
        this.userClass = userClass;
    }

    public Integer getUserClass() {
        return this.userClass;
    }

    public void setConnectionID(Integer connectionID) {
        this.connectionID = connectionID;
    }

    public Integer getConnectionID() {
        return this.connectionID;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof MapConnection)) {
            return false;
        }
        MapConnection mapConnection = (MapConnection) o;
        return Objects.equals(lane, mapConnection.lane) && Objects.equals(remoteIntersection, mapConnection.remoteIntersection) && Objects.equals(signalGroup, mapConnection.signalGroup) && Objects.equals(userClass, mapConnection.userClass) && Objects.equals(connectionID, mapConnection.connectionID);
    }

    @Override
    public int hashCode() {
        return Objects.hash(lane, remoteIntersection, signalGroup, userClass, connectionID);
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
