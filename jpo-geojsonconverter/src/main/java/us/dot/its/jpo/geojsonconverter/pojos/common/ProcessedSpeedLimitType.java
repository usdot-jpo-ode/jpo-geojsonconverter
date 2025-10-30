package us.dot.its.jpo.geojsonconverter.pojos.common;

import lombok.Getter;
import org.apache.commons.lang3.StringUtils;

import java.util.Objects;

@Getter
public enum ProcessedSpeedLimitType {
    UNKNOWN(0, "unknown"),
    MAXSPEEDINSCHOOLZONE(1, "maxSpeedInSchoolZone"),
    MAXSPEEDINSCHOOLZONEWHENCHILDRENAREPRESENT(2, "maxSpeedInSchoolZoneWhenChildrenArePresent"),
    MAXSPEEDINCONSTRUCTIONZONE(3, "maxSpeedInConstructionZone"),
    VEHICLEMINSPEED(4, "vehicleMinSpeed"),
    VEHICLEMAXSPEED(5, "vehicleMaxSpeed"),
    VEHICLENIGHTMAXSPEED(6, "vehicleNightMaxSpeed"),
    TRUCKMINSPEED(7, "truckMinSpeed"),
    TRUCKMAXSPEED(8, "truckMaxSpeed"),
    TRUCKNIGHTMAXSPEED(9, "truckNightMaxSpeed"),
    VEHICLESWITHTRAILERSMINSPEED(10, "vehiclesWithTrailersMinSpeed"),
    VEHICLESWITHTRAILERSMAXSPEED(11, "vehiclesWithTrailersMaxSpeed"),
    VEHICLESWITHTRAILERSNIGHTMAXSPEED(12, "vehiclesWithTrailersNightMaxSpeed");

    private final int index;
    private final String name;

    private ProcessedSpeedLimitType(int index, String name) {
        this.index = index;
        this.name = name;
    }

    public static ProcessedSpeedLimitType fromName(String name) {
        if (StringUtils.isBlank(name)) {
            return null;
        }
        for (ProcessedSpeedLimitType enumValue : ProcessedSpeedLimitType.values()) {
            if (Objects.equals(enumValue.getName(), name)) {
                return enumValue;
            }
        }
        throw new IllegalArgumentException(name);
    }
}
