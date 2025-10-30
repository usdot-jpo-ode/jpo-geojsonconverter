package us.dot.its.jpo.geojsonconverter.pojos.common;

import lombok.Getter;
import org.apache.commons.lang3.StringUtils;

import java.util.Objects;

@Getter
public enum ProcessedVehicleType {
    NONE(0, "none"),
    UNKNOWN(1, "unknown"),
    SPECIAL(2, "special"),
    MOTO(3, "moto"),
    CAR(4, "car"),
    CAROTHER(5, "carOther"),
    BUS(6, "bus"),
    AXLECNT2(7, "axleCnt2"),
    AXLECNT3(8, "axleCnt3"),
    AXLECNT4(9, "axleCnt4"),
    AXLECNT4TRAILER(10, "axleCnt4Trailer"),
    AXLECNT5TRAILER(11, "axleCnt5Trailer"),
    AXLECNT6TRAILER(12, "axleCnt6Trailer"),
    AXLECNT5MULTITRAILER(13, "axleCnt5MultiTrailer"),
    AXLECNT6MULTITRAILER(14, "axleCnt6MultiTrailer"),
    AXLECNT7MULTITRAILER(15, "axleCnt7MultiTrailer");

    private final int index;
    private final String name;

    private ProcessedVehicleType(int index, String name) {
        this.index = index;
        this.name = name;
    }

    public static ProcessedVehicleType fromName(String name) {
        if (StringUtils.isBlank(name)) {
            return null;
        }
        for (ProcessedVehicleType enumValue : ProcessedVehicleType.values()) {
            if (Objects.equals(enumValue.getName(), name)) {
                return enumValue;
            }
        }
        throw new IllegalArgumentException(name);
    }
}
