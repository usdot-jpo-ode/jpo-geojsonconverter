package us.dot.its.jpo.geojsonconverter.partitioner;

import java.util.Objects;

public class RsuLogKey implements RsuIdKey {

    private String rsuId;
    private String logId;
    private String bsmId;

    public RsuLogKey() {}

    public RsuLogKey(String rsuId, String logId, String bsmId) {
        this.rsuId = rsuId;
        this.logId = logId;
        this.bsmId = bsmId;
    }

    @Override
    public String getRsuId() {
        return this.rsuId;
    }

    public void setRsuId(String rsuLogId) {
        this.rsuId = rsuLogId;
    }

    public String getLogId() {
        return this.logId;
    }

    public void setLogId(String logId) {
        this.logId = logId;
    }

    public String getBsmId() {
        return this.bsmId;
    }

    public void setBsmId(String bsmId) {
        this.bsmId = bsmId;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof RsuLogKey)) {
            return false;
        }
        RsuLogKey rsuLogKey = (RsuLogKey) o;
        return Objects.equals(rsuId, rsuLogKey.getRsuId()) && Objects.equals(logId, rsuLogKey.getLogId()) && Objects.equals(bsmId, rsuLogKey.getBsmId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(rsuId, logId, bsmId);
    }
    



    @Override
    public String toString() {
        return "{" +
            " rsuId='" + getRsuId() + "'" +
            ", logId='" + getLogId() + "'" +
            ", bsmId='" + getBsmId() + "'" +
            "}";
    }

}
