package com.noam.goren.technical.unit.response;

import com.noam.goren.technical.enums.HooverError;
import com.noam.goren.technical.response.FailureResponse;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Assert;
import org.junit.Test;

public class FailureResponseTest {

    @Test(expected = NullPointerException.class)
    public void testWithNullError() {
        new FailureResponse(null);
    }

    @Test
    public void testFormat() throws JSONException {
        final FailureResponse response = new FailureResponse(HooverError.INVALID_INSTRUCTION);
        final JSONObject jsonObject = new JSONObject(response.toString());

        Assert.assertEquals("INVALID_INSTRUCTION", jsonObject.getString("error_code"));
        Assert.assertEquals("The string can only contain the following characters: N,S,W,E.", jsonObject.getString("error_message"));
    }
}