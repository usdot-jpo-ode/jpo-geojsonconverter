package us.dot.its.jpo.geojsonconverter.pojos.geojson.rtcm;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Generated;
import us.dot.its.jpo.geojsonconverter.pojos.geojson.BaseFeature;
import us.dot.its.jpo.geojsonconverter.pojos.geojson.Point;

/**
 * A point feature representing a processed J2735 RTCMcorrectionsMessageFrame
 *
 * <p>The geometry is the point location from FullPositionVector</p>
 *
 */
@Generated
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class ProcessedRTCM extends BaseFeature<Void, Point, RTCMProperties> {
    @JsonCreator
    public ProcessedRTCM(@JsonProperty("geometry") Point geometry,
                         @JsonProperty("properties") RTCMProperties properties) {
        super(null, geometry, properties);
    }
}
