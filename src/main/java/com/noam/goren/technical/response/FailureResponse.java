package com.noam.goren.technical.response;
import com.noam.goren.technical.enums.HooverError;
import org.json.JSONObject;

public class FailureResponse {

    private static final String ERROR_CODE = "error_code";
    private static final String ERROR_MESSAGE = "error_message";

    private final HooverError hooverError;

    public FailureResponse(final HooverError hooverError) {
        if(hooverError == null)
            throw new NullPointerException();

        this.hooverError = hooverError;
    }

    @Override
    public String toString() {
        final JSONObject jsonResponse = new JSONObject();

        jsonResponse.put(ERROR_CODE, hooverError.toString());
        jsonResponse.put(ERROR_MESSAGE, hooverError.getMessage());

        return jsonResponse.toString();
    }
}
