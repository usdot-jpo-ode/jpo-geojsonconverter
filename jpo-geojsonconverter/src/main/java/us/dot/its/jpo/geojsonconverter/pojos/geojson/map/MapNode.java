package us.dot.its.jpo.geojsonconverter.pojos.geojson.map;

import java.util.Objects;

public class MapNode {
    private Integer[] delta;
    private Integer dWidth;
    private Integer dElevation;
    private Boolean stopLine = null; // TODO: set to null for now, impliment correctly in future

    public void setDelta(Integer[] delta) {
        this.delta = delta;
    }

    public Integer[] getDelta() {
        return this.delta;
    }

    public void setDWidth(Integer dWidth) {
        this.dWidth = dWidth;
    }

    public Integer getDWidth() {
        return this.dWidth;
    }

    public void setDElevation(Integer dElevation) {
        this.dElevation = dElevation;
    }

    public Integer getDElevation() {
        return this.dElevation;
    }

    public void setStopLine(Boolean stopLine) {
        this.stopLine = stopLine;
    }

    public Boolean getStopLine() {
        return this.stopLine;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof MapNode)) {
            return false;
        }
        MapNode mapNode = (MapNode) o;
        return Objects.equals(delta, mapNode.delta) && Objects.equals(dWidth, mapNode.dWidth) && Objects.equals(dElevation, mapNode.dElevation) && Objects.equals(stopLine, mapNode.stopLine);
    }

    @Override
    public int hashCode() {
        return Objects.hash(delta, dWidth, dElevation, stopLine);
    }

    @Override
    public String toString() {
        return "{" +
            " delta='" + getDelta() + "'" +
            ", dWidth='" + getDWidth() + "'" +
            ", dElevation='" + getDElevation() + "'" +
            ", stopLine='" + getStopLine() + "'" +
            "}";
    }
}
