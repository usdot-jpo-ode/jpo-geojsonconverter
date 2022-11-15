
package us.dot.its.jpo.geojsonconverter.pojos.spat;

import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import us.dot.its.jpo.geojsonconverter.DateJsonMapper;
import us.dot.its.jpo.ode.plugin.j2735.J2735MovementPhaseState;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class MovementEvent {
    private static Logger logger = LoggerFactory.getLogger(MovementEvent.class);

    private J2735MovementPhaseState eventState;
    private TimingChangeDetails timing;
    private double speeds;

    public J2735MovementPhaseState getEventState() {
        return eventState;
    }

    public void setEventState(J2735MovementPhaseState eventState) {
        this.eventState = eventState;
    }

    public TimingChangeDetails getTiming() {
        return timing;
    }

    public void setTiming(TimingChangeDetails timing) {
        this.timing = timing;
    }

    public double getSpeeds() {
        return speeds;
    }

    public void setSpeeds(double speeds) {
        this.speeds = speeds;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof MovementEvent)) {
            return false;
        }
        MovementEvent movementEvent = (MovementEvent) o;
        return Objects.equals(eventState, movementEvent.eventState) && Objects.equals(timing, movementEvent.timing) && speeds == movementEvent.speeds;
    }

    @Override
    public int hashCode() {
        return Objects.hash(eventState, timing, speeds);
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
