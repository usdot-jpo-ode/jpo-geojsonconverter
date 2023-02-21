package us.dot.its.jpo.geojsonconverter.pojos.geojson.map;

import java.util.Objects;

import us.dot.its.jpo.geojsonconverter.validator.JsonValidatorResult;
import us.dot.its.jpo.ode.model.OdeMapData;

public class DeserializedRawMap {
    
    OdeMapData odeMapOdeMapData;
    JsonValidatorResult validatorResults;
    Boolean validationFailure = false;
    String failedMessage = null;

    public OdeMapData getOdeMapOdeMapData() {
        return this.odeMapOdeMapData;
    }

    public void setOdeMapOdeMapData(OdeMapData odeMapOdeSpat) {
        this.odeMapOdeMapData = odeMapOdeSpat;
    }

    public JsonValidatorResult getValidatorResults() {
        return this.validatorResults;
    }

    public void setValidatorResults(JsonValidatorResult validatorResults) {
        this.validatorResults = validatorResults;
    }

    public Boolean getValidationFailure() {
        return this.validationFailure;
    }

    public void setValidationFailure(Boolean validationFailure) {
        this.validationFailure = validationFailure;
    }

    public String getFailedMessage() {
        return this.failedMessage;
    }

    public void setFailedMessage(String failedMessage) {
        this.failedMessage = failedMessage;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof DeserializedRawMap)) {
            return false;
        }
        DeserializedRawMap deserializedRawMap = (DeserializedRawMap) o;
        return Objects.equals(odeMapOdeMapData, deserializedRawMap.odeMapOdeMapData) && Objects.equals(validatorResults, deserializedRawMap.validatorResults) && Objects.equals(validationFailure, deserializedRawMap.validationFailure) && Objects.equals(failedMessage, deserializedRawMap.failedMessage);
    }

    @Override
    public int hashCode() {
        return Objects.hash(odeMapOdeMapData, validatorResults, validationFailure, failedMessage);
    }


    @Override
    public String toString() {
        return "{" +
            " odeMapOdeSpat='" + getOdeMapOdeMapData() + "'" +
            ", validatorResults='" + getValidatorResults() + "'" +
            ", validationFailure='" + getValidationFailure() + "'" +
            ", failedMessage='" + getFailedMessage() + "'" +
            "}";
    }

    

}