package us.dot.its.jpo.geojsonconverter.pojos.geojson.srm;

import lombok.Getter;
import org.apache.commons.lang3.StringUtils;
import us.dot.its.jpo.geojsonconverter.pojos.common.ProcessedSpeedLimitType;

import java.util.Objects;

@Getter
public enum ProcessedTransitVehicleOccupancy {
    OCCUPANCYUNKNOWN(0, "occupancyUnknown"),
    OCCUPANCYEMPTY(1, "occupancyEmpty"),
    OCCUPANCYVERYLOW(2, "occupancyVeryLow"),
    OCCUPANCYLOW(3, "occupancyLow"),
    OCCUPANCYMED(4, "occupancyMed"),
    OCCUPANCYHIGH(5, "occupancyHigh"),
    OCCUPANCYNEARLYFULL(6, "occupancyNearlyFull"),
    OCCUPANCYFULL(7, "occupancyFull");

    private final int index;
    private final String name;

    private ProcessedTransitVehicleOccupancy(int index, String name) {
        this.index = index;
        this.name = name;
    }

    public static ProcessedTransitVehicleOccupancy fromName(String name) {
        if (StringUtils.isBlank(name)) {
            return null;
        }
        for (ProcessedTransitVehicleOccupancy enumValue : ProcessedTransitVehicleOccupancy.values()) {
            if (Objects.equals(enumValue.getName(), name)) {
                return enumValue;
            }
        }
        throw new IllegalArgumentException(name);
    }
}
