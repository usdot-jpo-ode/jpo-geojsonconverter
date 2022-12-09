package us.dot.its.jpo.geojsonconverter.pojos.spat;

import java.util.List;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import us.dot.its.jpo.geojsonconverter.DateJsonMapper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class MovementState {
    private static Logger logger = LoggerFactory.getLogger(MovementState.class);

    private String movementName;
    private Integer signalGroup;
    private List<MovementEvent> stateTimeSpeed = null;

    public String getMovementName() {
        return movementName;
    }

    public void setMovementName(String movementName) {
        this.movementName = movementName;
    }

    public Integer getSignalGroup() {
        return signalGroup;
    }

    public void setSignalGroup(Integer signalGroup) {
        this.signalGroup = signalGroup;
    }

    public List<MovementEvent> getStateTimeSpeed() {
        return stateTimeSpeed;
    }

    public void setStateTimeSpeed(List<MovementEvent> stateTimeSpeed) {
        this.stateTimeSpeed = stateTimeSpeed;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof MovementState)) {
            return false;
        }
        MovementState movementState = (MovementState) o;
        return Objects.equals(movementName, movementState.movementName) && signalGroup == movementState.signalGroup && Objects.equals(stateTimeSpeed, movementState.stateTimeSpeed);
    }

    @Override
    public int hashCode() {
        return Objects.hash(movementName, signalGroup, stateTimeSpeed);
    }

    @Override
    public String toString() {
        ObjectMapper mapper = DateJsonMapper.getInstance();
        String testReturn = "";
        try {
            testReturn = (mapper.writeValueAsString(this));
        } catch (JsonProcessingException e) {
            logger.error(e.getMessage(), e);
        }
        return testReturn;
    }

}
