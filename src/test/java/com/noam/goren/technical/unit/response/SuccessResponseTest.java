package com.noam.goren.technical.unit.response;

import com.noam.goren.technical.model.Hoover;
import com.noam.goren.technical.model.Point;
import com.noam.goren.technical.response.SuccessResponse;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Assert;
import org.junit.Test;

public class SuccessResponseTest {
    @Test(expected = NullPointerException.class)
    public void testWithNullError() {
        new SuccessResponse(null);
    }

    @Test
    public void testNumberOfPatchesCleaned() throws JSONException {
        final Point startPosition = new Point(1,1);
        final Point endPosition = new Point(3,3);
        final String instructions = "NSWE";
        final Hoover hoover = new Hoover(startPosition, instructions);
        hoover.setCleanedPatchesCount(1);
        hoover.setEndPosition(endPosition);
        final SuccessResponse response = new SuccessResponse(hoover);
        final JSONObject jsonResponse = new JSONObject(response.toString());
        Assert.assertEquals(1, jsonResponse.getInt("patches"));
    }

    @Test
    public void testFinalPosition() throws JSONException {
        final Point startPosition = new Point(1,1);
        final Point endPosition = new Point(3,5);
        final String instructions = "NSWE";
        final Hoover hoover = new Hoover(startPosition, instructions);
        hoover.setCleanedPatchesCount(1);
        hoover.setEndPosition(endPosition);
        final SuccessResponse response = new SuccessResponse(hoover);
        final JSONObject jsonResponse = new JSONObject(response.toString());
        final JSONArray finalPositionArray = jsonResponse.getJSONArray("coords");
        Assert.assertEquals(3, finalPositionArray.getInt(0));
        Assert.assertEquals(5, finalPositionArray.getInt(1));
    }
}
