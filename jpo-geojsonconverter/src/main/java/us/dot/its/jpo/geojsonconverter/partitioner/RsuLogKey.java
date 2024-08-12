package us.dot.its.jpo.geojsonconverter.partitioner;

import java.util.Objects;

public class RsuLogKey implements RsuIdKey {

    private String rsuId;
    private String logId;

    public RsuLogKey() {}

    public RsuLogKey(String rsuId, String logId) {
        this.rsuId = rsuId;
        this.logId = logId;
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

    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof RsuLogKey)) {
            return false;
        }
        RsuLogKey rsuLogKey = (RsuLogKey) o;
        return Objects.equals(rsuId, rsuLogKey.getRsuId()) && logId == rsuLogKey.getLogId();
    }

    @Override
    public int hashCode() {
        return Objects.hash(rsuId, logId);
    }
    



    @Override
    public String toString() {
        return "{" +
            " rsuId='" + getRsuId() + "'" +
            ", logId='" + getLogId() + "'" +
            "}";
    }

}
