package us.dot.its.jpo.geojsonconverter.pojos.common;

import lombok.Getter;
import org.apache.commons.lang3.StringUtils;

import java.util.Objects;

@Getter
public enum ProcessedTransmissionState {
    NEUTRAL(0, "neutral"),
    PARK(1, "park"),
    FORWARDGEARS(2, "forwardGears"),
    REVERSEGEARS(3, "reverseGears"),
    RESERVED1(4, "reserved1"),
    RESERVED2(5, "reserved2"),
    RESERVED3(6, "reserved3"),
    UNAVAILABLE(7, "unavailable");

    private final int index;
    private final String name;

    private ProcessedTransmissionState(int index, String name) {
        this.index = index;
        this.name = name;
    }

    public static ProcessedTransmissionState fromName(String name) {
        if (StringUtils.isBlank(name)) {
            return null;
        }
        for (ProcessedTransmissionState enumValue : ProcessedTransmissionState.values()) {
            if (Objects.equals(enumValue.getName(), name)) {
                return enumValue;
            }
        }
        throw new IllegalArgumentException(name);
    }
}
