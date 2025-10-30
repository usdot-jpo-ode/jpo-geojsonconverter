package us.dot.its.jpo.geojsonconverter.pojos.common;

import lombok.Getter;
import org.apache.commons.lang3.StringUtils;

import java.util.Objects;

@Getter
public enum ProcessedRequestSubRole {
    REQUESTSUBROLEUNKNOWN(0, "requestSubRoleUnKnown"),
    REQUESTSUBROLE1(1, "requestSubRole1"),
    REQUESTSUBROLE2(2, "requestSubRole2"),
    REQUESTSUBROLE3(3, "requestSubRole3"),
    REQUESTSUBROLE4(4, "requestSubRole4"),
    REQUESTSUBROLE5(5, "requestSubRole5"),
    REQUESTSUBROLE6(6, "requestSubRole6"),
    REQUESTSUBROLE7(7, "requestSubRole7"),
    REQUESTSUBROLE8(8, "requestSubRole8"),
    REQUESTSUBROLE9(9, "requestSubRole9"),
    REQUESTSUBROLE10(10, "requestSubRole10"),
    REQUESTSUBROLE11(11, "requestSubRole11"),
    REQUESTSUBROLE12(12, "requestSubRole12"),
    REQUESTSUBROLE13(13, "requestSubRole13"),
    REQUESTSUBROLE14(14, "requestSubRole14"),
    REQUESTSUBROLERESERVED(15, "requestSubRoleReserved");

    private final int index;
    private final String name;

    private ProcessedRequestSubRole(int index, String name) {
        this.index = index;
        this.name = name;
    }

    public static ProcessedRequestSubRole fromName(String name) {
        if (StringUtils.isBlank(name)) {
            return null;
        }
        for (ProcessedRequestSubRole enumValue : ProcessedRequestSubRole.values()) {
            if (Objects.equals(enumValue.getName(), name)) {
                return enumValue;
            }
        }
        throw new IllegalArgumentException(name);
    }
}
