package us.dot.its.jpo.geojsonconverter.pojos.geojson.map;

import com.fasterxml.jackson.annotation.JsonProperty;

public class MapNode {
    @JsonProperty("delta")
    private Integer[] delta;
    @JsonProperty("dWidth")
    private Integer dWidth;
    @JsonProperty("dElevation")
    private Integer dElevation;
    @JsonProperty("stopLine")
    private Boolean stopLine;

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
}
