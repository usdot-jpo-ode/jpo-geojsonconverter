package us.dot.its.jpo.geojsonconverter.pojos.geojson.rtcm;

import lombok.Data;
import us.dot.its.jpo.geojsonconverter.validator.JsonValidatorResult;
import us.dot.its.jpo.ode.model.OdeMessageFrameData;

/**
 * Holds an OdeMessageFrameData object received from the ODE with an RTCMcorrections
 * message frame, ODE metadata, and validation results.
 */
@Data
public class DeserializedRawRTCM {
    OdeMessageFrameData odeRTCMMessageFrameData;
    JsonValidatorResult validatorResults;
    boolean validationFailure;
    String failedMessage;
}
