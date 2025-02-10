package us.dot.its.jpo.geojsonconverter.pojos.geojson.psm;

import java.util.Objects;

import us.dot.its.jpo.geojsonconverter.validator.JsonValidatorResult;
import us.dot.its.jpo.ode.model.OdePsmData;

public class DeserializedRawPsm {
    
    OdePsmData odePsmData;
    JsonValidatorResult validatorResults;
    Boolean validationFailure = false;
    String failedMessage = null;

    public OdePsmData getOdePsmData() {
        return this.odePsmData;
    }

    public void setOdePsmData(OdePsmData odePsmData) {
        this.odePsmData = odePsmData;
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
        if (!(o instanceof DeserializedRawPsm)) {
            return false;
        }
        DeserializedRawPsm deserializedRawPsm = (DeserializedRawPsm) o;
        return Objects.equals(odePsmData, deserializedRawPsm.odePsmData) && Objects.equals(validatorResults, deserializedRawPsm.validatorResults) && Objects.equals(validationFailure, deserializedRawPsm.validationFailure) && Objects.equals(failedMessage, deserializedRawPsm.failedMessage);
    }

    @Override
    public int hashCode() {
        return Objects.hash(odePsmData, validatorResults, validationFailure, failedMessage);
    }

    @Override
    public String toString() {
        return "{" +
            " odeSpatOdeSpat='" + getOdePsmData() + "'" +
            ", validatorResults='" + getValidatorResults() + "'" +
            ", validationFailure='" + getValidationFailure() + "'" +
            ", failedMessage='" + getFailedMessage() + "'" +
            "}";
    }
}
