package us.dot.its.jpo.geojsonconverter.partitioner;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RsuPsmIdKey implements RsuIdKey {
    private String rsuId;
    private String psmId;
}
