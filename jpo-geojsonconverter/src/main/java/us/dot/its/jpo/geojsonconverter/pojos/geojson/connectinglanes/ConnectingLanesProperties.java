package us.dot.its.jpo.geojsonconverter.pojos.geojson.connectinglanes;

public class ConnectingLanesProperties {
    private Integer signalGroupId;
    private Integer ingressLaneId;
    private Integer egressLaneId;

    public void setSignalGroupId(Integer signalGroupId) {
        this.signalGroupId = signalGroupId;
    }

    public Integer getSignalGroupId() {
        return this.signalGroupId;
    }

    public void setIngressLaneId(Integer ingressLaneId) {
        this.ingressLaneId = ingressLaneId;
    }

    public Integer getIngressLaneId() {
        return this.ingressLaneId;
    }

    public void setEgressLaneId(Integer egressLaneId) {
        this.egressLaneId = egressLaneId;
    }

    public Integer getEgressLaneId() {
        return this.egressLaneId;
    }
}
