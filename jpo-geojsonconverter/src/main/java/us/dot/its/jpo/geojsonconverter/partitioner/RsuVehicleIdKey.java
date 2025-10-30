package us.dot.its.jpo.geojsonconverter.partitioner;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class RsuVehicleIdKey implements RsuIdKey {

    private String rsuId;
    private String vehicleId;

    @Override
    public String getRsuId() {
        return rsuId;
    }
}
