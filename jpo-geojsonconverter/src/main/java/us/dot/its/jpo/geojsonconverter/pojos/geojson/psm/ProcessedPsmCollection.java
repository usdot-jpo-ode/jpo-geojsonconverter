package us.dot.its.jpo.geojsonconverter.pojos.geojson.psm;

import java.util.Arrays;
import java.util.Objects;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import us.dot.its.jpo.geojsonconverter.DateJsonMapper;
import us.dot.its.jpo.geojsonconverter.pojos.geojson.BaseFeatureCollection;

// This is a helper class to allow for a ProcessedPsmCollection to be created with a list of ProcessedPsm Features
public class ProcessedPsmCollection<TGeometry> extends BaseFeatureCollection<ProcessedPsm<TGeometry>> {
    private static Logger logger = LoggerFactory.getLogger(ProcessedPsmCollection.class);

    @JsonCreator
    public ProcessedPsmCollection(@JsonProperty("features") ProcessedPsm<TGeometry>[] features) {
        super(features);
    }

    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof ProcessedPsmCollection)) {
            return false;
        }

        @SuppressWarnings("unchecked")
        ProcessedPsmCollection<TGeometry> processedPsm = (ProcessedPsmCollection<TGeometry>) o;
        return (Arrays.equals(getFeatures(), processedPsm.getFeatures()));
    }

    @Override
    public int hashCode() {
        return Objects.hash((Object[]) getFeatures());
    }

    @Override
    public String toString() {
        ObjectMapper mapper = DateJsonMapper.getInstance();
        String testReturn = "";
        try {
            testReturn = (mapper.writeValueAsString(this));
        } catch (JsonProcessingException e) {
            logger.error(e.getMessage(), e);
        }
        return testReturn;
    }
}
