package us.dot.its.jpo.geojsonconverter.pojos.spat;

import lombok.Getter;
import org.apache.commons.lang3.StringUtils;

import java.util.Objects;

@Getter
public enum ProcessedAdvisorySpeedType {
    NONE(0, "none"),
    GREENWAVE(1, "greenwave"),
    ECODRIVE(2, "ecoDrive"),
    TRANSIT(3, "transit");

    private final int index;
    private final String name;

    private ProcessedAdvisorySpeedType(int index, String name) {
        this.index = index;
        this.name = name;
    }

    public static ProcessedAdvisorySpeedType fromName(String name) {
        if (StringUtils.isBlank(name)) {
            return null;
        }
        for (ProcessedAdvisorySpeedType enumValue : ProcessedAdvisorySpeedType.values()) {
            if (Objects.equals(enumValue.getName(), name)) {
                return enumValue;
            }
        }
        throw new IllegalArgumentException(name);
    }
}
