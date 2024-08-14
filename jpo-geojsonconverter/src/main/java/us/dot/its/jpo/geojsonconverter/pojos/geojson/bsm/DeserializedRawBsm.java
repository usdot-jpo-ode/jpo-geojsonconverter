package us.dot.its.jpo.geojsonconverter.pojos.geojson.bsm;

import java.util.Objects;

import us.dot.its.jpo.geojsonconverter.validator.JsonValidatorResult;
import us.dot.its.jpo.ode.model.OdeBsmData;

public class DeserializedRawBsm {
    
    OdeBsmData odeBsmData;
    JsonValidatorResult validatorResults;
    Boolean validationFailure = false;
    String failedMessage = null;

    public OdeBsmData getOdeBsmData() {
        return this.odeBsmData;
    }

    public void setOdeBsmData(OdeBsmData odeBsmData) {
        this.odeBsmData = odeBsmData;
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
        if (!(o instanceof DeserializedRawBsm)) {
            return false;
        }
        DeserializedRawBsm deserializedRawBsm = (DeserializedRawBsm) o;
        return Objects.equals(odeBsmData, deserializedRawBsm.odeBsmData) && Objects.equals(validatorResults, deserializedRawBsm.validatorResults) && Objects.equals(validationFailure, deserializedRawBsm.validationFailure) && Objects.equals(failedMessage, deserializedRawBsm.failedMessage);
    }

    @Override
    public int hashCode() {
        return Objects.hash(odeBsmData, validatorResults, validationFailure, failedMessage);
    }

    @Override
    public String toString() {
        return "{" +
            " odeSpatOdeSpat='" + getOdeBsmData() + "'" +
            ", validatorResults='" + getValidatorResults() + "'" +
            ", validationFailure='" + getValidationFailure() + "'" +
            ", failedMessage='" + getFailedMessage() + "'" +
            "}";
    }
}
