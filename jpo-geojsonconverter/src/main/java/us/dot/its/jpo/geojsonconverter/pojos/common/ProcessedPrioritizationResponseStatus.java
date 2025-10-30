package us.dot.its.jpo.geojsonconverter.pojos.common;

import lombok.Getter;
import org.apache.commons.lang3.StringUtils;

import java.util.Objects;

@Getter
public enum ProcessedPrioritizationResponseStatus {
    UNKNOWN(0, "unknown"),
    REQUESTED(1, "requested"),
    PROCESSING(2, "processing"),
    WATCHOTHERTRAFFIC(3, "watchOtherTraffic"),
    GRANTED(4, "granted"),
    REJECTED(5, "rejected"),
    MAXPRESENCE(6, "maxPresence"),
    RESERVICELOCKED(7, "reserviceLocked");

    private final int index;
    private final String name;

    private ProcessedPrioritizationResponseStatus(int index, String name) {
        this.index = index;
        this.name = name;
    }

    public static ProcessedPrioritizationResponseStatus fromName(String name) {
        if (StringUtils.isBlank(name)) {
            return null;
        }
        for (ProcessedPrioritizationResponseStatus enumValue : ProcessedPrioritizationResponseStatus.values()) {
            if (Objects.equals(enumValue.getName(), name)) {
                return enumValue;
            }
        }
        throw new IllegalArgumentException(name);
    }
}
