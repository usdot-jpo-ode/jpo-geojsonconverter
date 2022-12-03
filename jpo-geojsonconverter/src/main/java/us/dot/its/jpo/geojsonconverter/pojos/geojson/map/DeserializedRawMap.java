package us.dot.its.jpo.geojsonconverter.pojos.geojson.map;

import java.util.Objects;

import us.dot.its.jpo.geojsonconverter.validator.JsonValidatorResult;
import us.dot.its.jpo.ode.model.OdeMapData;

public class DeserializedRawMap {
    
    OdeMapData odeMapOdeSpat;
    JsonValidatorResult validatorResults;

    public OdeMapData getOdeMapOdeMapData() {
        return this.odeMapOdeSpat;
    }

    public void setOdeMapOdeMapData(OdeMapData odeMapOdeMapData) {
        this.odeMapOdeSpat = odeMapOdeMapData;
    }

    public JsonValidatorResult getValidatorResults() {
        return this.validatorResults;
    }

    public void setValidatorResults(JsonValidatorResult validatorResults) {
        this.validatorResults = validatorResults;
    }


    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof DeserializedRawMap)) {
            return false;
        }
        DeserializedRawMap deserializedRawSpat = (DeserializedRawMap) o;
        return Objects.equals(odeMapOdeSpat, deserializedRawSpat.odeMapOdeSpat) && Objects.equals(validatorResults, deserializedRawSpat.validatorResults);
    }

    @Override
    public int hashCode() {
        return Objects.hash(odeMapOdeSpat, validatorResults);
    }

    @Override
    public String toString() {
        return "{" +
            " odeMapOdeMapData='" + getOdeMapOdeMapData() + "'" +
            ", validatorResults='" + getValidatorResults() + "'" +
            "}";
    }

}