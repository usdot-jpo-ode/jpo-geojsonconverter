package us.dot.its.jpo.geojsonconverter.pojos.geojson.srm;

import lombok.Getter;
import org.apache.commons.lang3.StringUtils;

import java.util.Objects;

@Getter
public enum ProcessedPriorityRequestType {
    PRIORITYREQUESTTYPERESERVED(0, "priorityRequestTypeReserved"),
    PRIORITYREQUEST(1, "priorityRequest"),
    PRIORITYREQUESTUPDATE(2, "priorityRequestUpdate"),
    PRIORITYCANCELLATION(3, "priorityCancellation");

    private final int index;
    private final String name;

    private ProcessedPriorityRequestType(int index, String name) {
        this.index = index;
        this.name = name;
    }

    public static ProcessedPriorityRequestType fromName(String name) {
        if (StringUtils.isBlank(name)) {
            return null;
        }
        for (ProcessedPriorityRequestType enumValue : ProcessedPriorityRequestType.values()) {
            if (Objects.equals(enumValue.getName(), name)) {
                return enumValue;
            }
        }
        throw new IllegalArgumentException(name);
    }
}
