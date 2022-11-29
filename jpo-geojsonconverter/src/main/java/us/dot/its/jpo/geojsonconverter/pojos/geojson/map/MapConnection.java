package us.dot.its.jpo.geojsonconverter.pojos.geojson.map;

import us.dot.its.jpo.ode.plugin.j2735.J2735IntersectionReferenceID;

public class MapConnection {
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
}
