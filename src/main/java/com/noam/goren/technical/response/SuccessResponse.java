package com.noam.goren.technical.response;

import com.noam.goren.technical.model.Hoover;
import org.json.JSONArray;
import org.json.JSONObject;

public class SuccessResponse {

    private static final String FINAL_HOOVER_POSITION = "coords";
    private static final String NUMBER_OF_PATCHES_CLEANED = "patches";

    final private Hoover hoover;

    public SuccessResponse(final Hoover hoover) {
        if(hoover == null)
            throw new NullPointerException();

        this.hoover = hoover;
    }

    @Override
    public String toString() {
        final JSONObject jsonResponse = new JSONObject();
        final JSONArray finalPositionArray = new JSONArray();
        finalPositionArray.put(hoover.getEndPosition().getX());
        finalPositionArray.put(hoover.getEndPosition().getY());
        jsonResponse.put(FINAL_HOOVER_POSITION, finalPositionArray);
        jsonResponse.put(NUMBER_OF_PATCHES_CLEANED, hoover.getCleanedPatchesCount());

        return jsonResponse.toString();
    }
}
