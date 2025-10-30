package us.dot.its.jpo.geojsonconverter.pojos.common;

import lombok.Getter;
import org.apache.commons.lang3.StringUtils;

import java.util.Objects;

@Getter
public enum ProcessedStabilityControlStatus {
    UNAVAILABLE(0, "unavailable"),
    OFF(1, "off"),
    ON(2, "on"),
    ENGAGED(3, "engaged");

    private final int index;
    private final String name;

    private ProcessedStabilityControlStatus(int index, String name) {
        this.index = index;
        this.name = name;
    }

    public static ProcessedStabilityControlStatus fromName(String name) {
        if (StringUtils.isBlank(name)) {
            return null;
        }
        for (ProcessedStabilityControlStatus enumValue : ProcessedStabilityControlStatus.values()) {
            if (Objects.equals(enumValue.getName(), name)) {
                return enumValue;
            }
        }
        throw new IllegalArgumentException(name);
    }
}
