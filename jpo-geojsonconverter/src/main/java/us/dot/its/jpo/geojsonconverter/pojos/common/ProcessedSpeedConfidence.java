package us.dot.its.jpo.geojsonconverter.pojos.common;

import lombok.Getter;
import org.apache.commons.lang3.StringUtils;

import java.util.Objects;

@Getter
public enum ProcessedSpeedConfidence {
    UNAVAILABLE(0, "unavailable"),
    PREC100MS(1, "prec100ms"),
    PREC10MS(2, "prec10ms"),
    PREC5MS(3, "prec5ms"),
    PREC1MS(4, "prec1ms"),
    PREC0_1MS(5, "prec0-1ms"),
    PREC0_05MS(6, "prec0-05ms"),
    PREC0_01MS(7, "prec0-01ms");

    private final int index;
    private final String name;

    private ProcessedSpeedConfidence(int index, String name) {
        this.index = index;
        this.name = name;
    }

    public static ProcessedSpeedConfidence fromName(String name) {
        if (StringUtils.isBlank(name)) {
            return null;
        }
        for (ProcessedSpeedConfidence enumValue : ProcessedSpeedConfidence.values()) {
            if (Objects.equals(enumValue.getName(), name)) {
                return enumValue;
            }
        }
        throw new IllegalArgumentException(name);
    }
}
