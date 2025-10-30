package us.dot.its.jpo.geojsonconverter.pojos.common;

import lombok.Getter;
import org.apache.commons.lang3.StringUtils;

import java.util.Objects;

@Getter
public enum ProcessedAuxiliaryBrakeStatus {
    UNAVAILABLE(0, "unavailable"),
    OFF(1, "off"),
    ON(2, "on"),
    RESERVED(3, "reserved");

    private final int index;
    private final String name;

    private ProcessedAuxiliaryBrakeStatus(int index, String name) {
        this.index = index;
        this.name = name;
    }

    public static ProcessedAuxiliaryBrakeStatus fromName(String name) {
        if (StringUtils.isBlank(name)) {
            return null;
        }
        for (ProcessedAuxiliaryBrakeStatus enumValue : ProcessedAuxiliaryBrakeStatus.values()) {
            if (Objects.equals(enumValue.getName(), name)) {
                return enumValue;
            }
        }
        throw new IllegalArgumentException(name);
    }
}
