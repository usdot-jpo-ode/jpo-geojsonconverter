package us.dot.its.jpo.geojsonconverter.pojos.common;

import lombok.Getter;
import org.apache.commons.lang3.StringUtils;

import java.util.Objects;

@Getter
public enum ProcessedBasicVehicleRole {
    BASICVEHICLE(0, "basicVehicle"),
    PUBLICTRANSPORT(1, "publicTransport"),
    SPECIALTRANSPORT(2, "specialTransport"),
    DANGEROUSGOODS(3, "dangerousGoods"),
    ROADWORK(4, "roadWork"),
    ROADRESCUE(5, "roadRescue"),
    EMERGENCY(6, "emergency"),
    SAFETYCAR(7, "safetyCar"),
    NONE_UNKNOWN(8, "none-unknown"),
    TRUCK(9, "truck"),
    MOTORCYCLE(10, "motorcycle"),
    ROADSIDESOURCE(11, "roadSideSource"),
    POLICE(12, "police"),
    FIRE(13, "fire"),
    AMBULANCE(14, "ambulance"),
    DOT(15, "dot"),
    TRANSIT(16, "transit"),
    SLOWMOVING(17, "slowMoving"),
    STOPNGO(18, "stopNgo"),
    CYCLIST(19, "cyclist"),
    PEDESTRIAN(20, "pedestrian"),
    NONMOTORIZED(21, "nonMotorized"),
    MILITARY(22, "military");

    private final int index;
    private final String name;

    private ProcessedBasicVehicleRole(int index, String name) {
        this.index = index;
        this.name = name;
    }

    public static ProcessedBasicVehicleRole fromName(String name) {
        if (StringUtils.isBlank(name)) {
            return null;
        }
        for (ProcessedBasicVehicleRole enumValue : ProcessedBasicVehicleRole.values()) {
            if (Objects.equals(enumValue.getName(), name)) {
                return enumValue;
            }
        }
        throw new IllegalArgumentException(name);
    }
}
