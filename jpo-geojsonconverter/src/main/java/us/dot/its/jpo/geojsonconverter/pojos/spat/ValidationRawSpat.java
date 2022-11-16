package us.dot.its.jpo.geojsonconverter.pojos.spat;

import java.util.Objects;

import org.apache.kafka.common.utils.Bytes;

import us.dot.its.jpo.geojsonconverter.validator.JsonValidatorResult;

public class ValidationRawSpat {
    
    Bytes odeSpatBytes;
    JsonValidatorResult validatorResults;

    public Bytes getOdeSpatBytes() {
        return this.odeSpatBytes;
    }

    public void setOdeSpatBytes(Bytes odeSpatBytes) {
        this.odeSpatBytes = odeSpatBytes;
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
        if (!(o instanceof ValidationRawSpat)) {
            return false;
        }
        ValidationRawSpat validationRawSpat = (ValidationRawSpat) o;
        return Objects.equals(odeSpatBytes, validationRawSpat.odeSpatBytes) && Objects.equals(validatorResults, validationRawSpat.validatorResults);
    }

    @Override
    public int hashCode() {
        return Objects.hash(odeSpatBytes, validatorResults);
    }

    @Override
    public String toString() {
        return "{" +
            " odeSpatBytes='" + getOdeSpatBytes() + "'" +
            ", validatorResults='" + getValidatorResults() + "'" +
            "}";
    }

}