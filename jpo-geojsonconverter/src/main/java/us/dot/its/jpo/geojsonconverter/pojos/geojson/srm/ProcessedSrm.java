package us.dot.its.jpo.geojsonconverter.pojos.geojson.srm;


import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Generated;
import us.dot.its.jpo.geojsonconverter.pojos.geojson.BaseFeature;
import us.dot.its.jpo.geojsonconverter.pojos.geojson.Point;

/**
 * A point feature representing a processed J2735 MSG_SignalRequestMessage.
 * <p>Similar to BSMs, the geometry is the point location of the vehicle that broadcast the SRM.</p>
 * <p>But note that unlike BSMs, the position is optional in J2735 DF_RequestorDescription, so the geometry
 * may be null.</p>
 * <p>The SRM may contain multiple requests that may represent multiple lane connections of the
 * same intersection, or multiple intersections.  These are represented by a list of
 * {@link ProcessedSignalRequest}s in the {@link SrmProperties}</p>
 */
@Generated
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class ProcessedSrm extends BaseFeature<Void, Point, SrmProperties> {
    @JsonCreator
    public ProcessedSrm(@JsonProperty("geometry") Point geometry,
                        @JsonProperty("properties") SrmProperties srmProperties) {
        super(null, geometry, srmProperties);
    }
}
