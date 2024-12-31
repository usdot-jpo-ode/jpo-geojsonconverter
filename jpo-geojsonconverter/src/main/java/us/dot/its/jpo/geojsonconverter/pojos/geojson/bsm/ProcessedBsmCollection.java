package us.dot.its.jpo.geojsonconverter.pojos.geojson.bsm;

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

public class ProcessedBsmCollection<Point> extends BaseFeatureCollection<ProcessedBsm<Point>> {
    private static Logger logger = LoggerFactory.getLogger(ProcessedBsmCollection.class);

    @JsonCreator
    public ProcessedBsmCollection(@JsonProperty("features") ProcessedBsm<Point>[] features) {
        super(features);
    }

    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof ProcessedBsmCollection)) {
            return false;
        }

        @SuppressWarnings("unchecked")
        ProcessedBsmCollection<Point> processedBsm = (ProcessedBsmCollection<Point>) o;
        return (
                Arrays.equals(getFeatures(), processedBsm.getFeatures()));
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
