package us.dot.its.jpo.geojsonconverter.pojos.geojson.connectinglanes;

import java.util.Objects;

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

    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof ConnectingLanesProperties)) {
            return false;
        }
        ConnectingLanesProperties connectingLanesProperties = (ConnectingLanesProperties) o;
        return Objects.equals(signalGroupId, connectingLanesProperties.signalGroupId) && Objects.equals(ingressLaneId, connectingLanesProperties.ingressLaneId) && Objects.equals(egressLaneId, connectingLanesProperties.egressLaneId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(signalGroupId, ingressLaneId, egressLaneId);
    }

    @Override
    public String toString() {
        return "{" +
            " signalGroupId='" + getSignalGroupId() + "'" +
            ", ingressLaneId='" + getIngressLaneId() + "'" +
            ", egressLaneId='" + getEgressLaneId() + "'" +
            "}";
    }
}
