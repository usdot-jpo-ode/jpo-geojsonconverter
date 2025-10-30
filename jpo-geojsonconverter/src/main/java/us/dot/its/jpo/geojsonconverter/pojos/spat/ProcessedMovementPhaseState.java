package us.dot.its.jpo.geojsonconverter.pojos.spat;

import lombok.Getter;
import org.apache.commons.lang3.StringUtils;

import java.util.Objects;

/**
 * To represent MovementPhaseState for Conflict Monitor Events.
 * <p>Corresponds to the ASN.1 Enumerated type {@link us.dot.its.jpo.asn.j2735.r2024.SPAT.MovementPhaseState} but
 *     serializes with the normal Java convention, all uppercase.</p>
 */
@Getter
public enum ProcessedMovementPhaseState {
    UNAVAILABLE(0, "unavailable"),
    DARK(1, "dark"),
    STOP_THEN_PROCEED(2, "stop-Then-Proceed"),
    STOP_AND_REMAIN(3, "stop-And-Remain"),
    PRE_MOVEMENT(4, "pre-Movement"),
    PERMISSIVE_MOVEMENT_ALLOWED(5, "permissive-Movement-Allowed"),
    PROTECTED_MOVEMENT_ALLOWED(6, "protected-Movement-Allowed"),
    PERMISSIVE_CLEARANCE(7, "permissive-clearance"),
    PROTECTED_CLEARANCE(8, "protected-clearance"),
    CAUTION_CONFLICTING_TRAFFIC(9, "caution-Conflicting-Traffic");

    private final int index;
    private final String name;

    private ProcessedMovementPhaseState(int index, String name) {
        this.index = index;
        this.name = name;
    }

    public static ProcessedMovementPhaseState fromName(String name) {
        if (StringUtils.isBlank(name)) {
            return null;
        }
        for (ProcessedMovementPhaseState enumValue : ProcessedMovementPhaseState.values()) {
            if (Objects.equals(enumValue.getName(), name)) {
                return enumValue;
            }
        }
        throw new IllegalArgumentException(name);
    }
}
