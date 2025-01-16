package us.dot.its.jpo.geojsonconverter.partitioner;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import us.dot.its.jpo.ode.plugin.j2735.J2735PersonalDeviceUserType;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RsuTypeIdKey implements RsuIdKey {
    private String rsuId;
    private J2735PersonalDeviceUserType pedestrianType;
    private String psmId;
}
