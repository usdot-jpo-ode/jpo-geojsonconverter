package us.dot.its.jpo.geojsonconverter.pojos.spat;

import java.util.Objects;

import us.dot.its.jpo.geojsonconverter.validator.JsonValidatorResult;
import us.dot.its.jpo.ode.model.OdeSpatData;

public class DeserializedRawSpat {
    
    OdeSpatData odeSpatOdeSpat;
    JsonValidatorResult validatorResults;

    public OdeSpatData getOdeSpatOdeSpatData() {
        return this.odeSpatOdeSpat;
    }

    public void setOdeSpatOdeSpatData(OdeSpatData odeSpatOdeSpatData) {
        this.odeSpatOdeSpat = odeSpatOdeSpatData;
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
        if (!(o instanceof DeserializedRawSpat)) {
            return false;
        }
        DeserializedRawSpat deserializedRawSpat = (DeserializedRawSpat) o;
        return Objects.equals(odeSpatOdeSpat, deserializedRawSpat.odeSpatOdeSpat) && Objects.equals(validatorResults, deserializedRawSpat.validatorResults);
    }

    @Override
    public int hashCode() {
        return Objects.hash(odeSpatOdeSpat, validatorResults);
    }

    @Override
    public String toString() {
        return "{" +
            " odeSpatOdeSpatData='" + getOdeSpatOdeSpatData() + "'" +
            ", validatorResults='" + getValidatorResults() + "'" +
            "}";
    }

}