package us.dot.its.jpo.geojsonconverter.pojos.spat;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import java.time.ZonedDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Generated;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Data
@Generated
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@Slf4j
public class TimingChangeDetails {
    private ZonedDateTime startTime;
    private ZonedDateTime minEndTime;
    private ZonedDateTime maxEndTime;
    private ZonedDateTime likelyTime;
    private Integer confidence;
    private ZonedDateTime nextTime;
}
