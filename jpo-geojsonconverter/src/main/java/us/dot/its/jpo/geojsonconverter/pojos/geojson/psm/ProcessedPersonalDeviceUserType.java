package us.dot.its.jpo.geojsonconverter.pojos.geojson.psm;

import lombok.Getter;
import org.apache.commons.lang3.StringUtils;
import us.dot.its.jpo.geojsonconverter.pojos.common.ProcessedSpeedConfidence;

import java.util.Objects;

@Getter
public enum ProcessedPersonalDeviceUserType {
    UNAVAILABLE(0, "unavailable"),
    APEDESTRIAN(1, "aPEDESTRIAN"),
    APEDALCYCLIST(2, "aPEDALCYCLIST"),
    APUBLICSAFETYWORKER(3, "aPUBLICSAFETYWORKER"),
    ANANIMAL(4, "anANIMAL");

    private final int index;
    private final String name;

    private ProcessedPersonalDeviceUserType(int index, String name) {
        this.index = index;
        this.name = name;
    }

    public static ProcessedPersonalDeviceUserType fromName(String name) {
        if (StringUtils.isBlank(name)) {
            return null;
        }
        for (ProcessedPersonalDeviceUserType enumValue : ProcessedPersonalDeviceUserType.values()) {
            if (Objects.equals(enumValue.getName(), name)) {
                return enumValue;
            }
        }
        throw new IllegalArgumentException(name);
    }
}
