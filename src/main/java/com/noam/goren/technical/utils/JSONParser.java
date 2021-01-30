package com.noam.goren.technical.utils;

import com.noam.goren.technical.enums.HooverError;
import com.noam.goren.technical.exception.HooverException;
import com.noam.goren.technical.model.Hoover;
import com.noam.goren.technical.model.Point;
import com.noam.goren.technical.model.Room;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class JSONParser {

    private static final String ROOM_SIZE = "roomSize";
    private static final String START_POSITION = "coords";
    private static final String INSTRUCTIONS = "instructions";
    private static final String PATCHES = "patches";
    private static final int X_INDEX = 0;
    private static final int Y_INDEX = 1;
    private static final int MINIMUM_ROOM_AXIS_SIZE = 1;
    private static final int LOWER_AXIAL_POSITION = 0;
    private static final int EXPECTED_NUMBER_OF_AXIS = 2;

    private JSONParser() {}

    public static Room getModelFromJSON(final JSONObject jsonObject) throws JSONException, HooverException {
        if(jsonObject == null)
            throw new NullPointerException();
        validateJSONFormat(jsonObject);
        final JSONArray roomSizeArray = jsonObject.getJSONArray(ROOM_SIZE);
        validatePatchPosition(roomSizeArray);
        final int width = roomSizeArray.getInt(X_INDEX);
        final int height = roomSizeArray.getInt(Y_INDEX);
        validateWidthAndHeight(width, height);
        final JSONArray startPositionJson = jsonObject.getJSONArray(START_POSITION);
        validatePatchPosition(startPositionJson);
        final Point startPosition = new Point(startPositionJson.getInt(X_INDEX),
                startPositionJson.getInt(Y_INDEX),
                false);
        validateHooverStartPosition(width, height, startPosition);
        final String instructions = validateInstructions(jsonObject);
        final Hoover hoover = new Hoover(startPosition, instructions);
        final List<Point> patches = new ArrayList<>();
        final JSONArray patchesArray = jsonObject.getJSONArray(PATCHES);
        iteratePatchesArray(width, height, patches, patchesArray);
        return new Room(width, height, patches, hoover);
    }

    private static void validateWidthAndHeight(int width, int height) throws HooverException {
        if((width < MINIMUM_ROOM_AXIS_SIZE) || (height < MINIMUM_ROOM_AXIS_SIZE))
            throw new HooverException(HooverError.INVALID_ROOM_DIMENSION);
    }

    private static void validateHooverStartPosition(int width, int height, Point startPosition) throws HooverException {
        if(((startPosition.getX() < LOWER_AXIAL_POSITION) || (startPosition.getX() >= width)) ||
                ((startPosition.getY() < LOWER_AXIAL_POSITION) || (startPosition.getY() >= height)))
            throw new HooverException(HooverError.INVALID_START_POSITION);
    }

    private static void iteratePatchesArray(int width, int height, List<Point> patches, JSONArray patchesArray) throws HooverException {
        for(int i = 0; i < patchesArray.length(); i++) {
            final JSONArray patchCoordinateArray = patchesArray.getJSONArray(i);
            validatePatchPosition(patchCoordinateArray);
            final Point patchCoordinate = new Point(patchCoordinateArray.getInt(X_INDEX),
                    patchCoordinateArray.getInt(Y_INDEX),
                    true);
            validatePatchPosition(width, height, patchCoordinate);

            patches.add(patchCoordinate);
        }
    }

    private static void validatePatchPosition(JSONArray patchCoordinateArray) throws HooverException {
        if(patchCoordinateArray.length() != EXPECTED_NUMBER_OF_AXIS)
            throw new HooverException(HooverError.FORMAT_NOT_VALID);
    }

    private static void validatePatchPosition(int width, int height, Point patchCoordinate) throws HooverException {
        if(((patchCoordinate.getX() < LOWER_AXIAL_POSITION) || (patchCoordinate.getX() >= width)) ||
                ((patchCoordinate.getY() < LOWER_AXIAL_POSITION) || (patchCoordinate.getY() >= height)))
            throw new HooverException(HooverError.INVALID_PATCH_POSITION);
    }

    private static void validateJSONFormat(JSONObject jsonObject) throws HooverException {
        if(!jsonObject.has(START_POSITION) ||
                !jsonObject.has(INSTRUCTIONS) ||
                !jsonObject.has(PATCHES) ||
                !jsonObject.has(ROOM_SIZE))
            throw new HooverException(HooverError.FORMAT_NOT_VALID);
    }

    public static JSONObject getJSONObjectFromModel(final Room room) throws JSONException {
        if(room == null)
            throw new NullPointerException();
        final JSONArray roomSizeArray = new JSONArray();
        roomSizeArray.put(room.getWidth());
        roomSizeArray.put(room.getHeight());
        final JSONArray hooverStartPositionArray = createHooverStartPositionArray(room);
        final JSONArray patchesArray = createPatchesArray(room);
        return createJsonObject(room, roomSizeArray, hooverStartPositionArray, patchesArray);
    }

    private static JSONArray createHooverStartPositionArray(Room room) {
        final JSONArray hooverStartPositionArray = new JSONArray();
        hooverStartPositionArray.put(room.getHoover().getStartPosition().getX());
        hooverStartPositionArray.put(room.getHoover().getStartPosition().getY());
        return hooverStartPositionArray;
    }

    private static JSONObject createJsonObject(Room room, JSONArray roomSizeArray, JSONArray hooverStartPositionArray, JSONArray patchesArray) {
        final JSONObject jsonRoom = new JSONObject();
        jsonRoom.put(ROOM_SIZE, roomSizeArray);
        jsonRoom.put(START_POSITION, hooverStartPositionArray);
        jsonRoom.put(INSTRUCTIONS, room.getHoover().getInstructions());
        jsonRoom.put(PATCHES, patchesArray);
        return jsonRoom;
    }

    private static JSONArray createPatchesArray(Room room) {
        final JSONArray patchesArray = new JSONArray();
        for(final Point c : room.getDirtPatchesList()) {
            final JSONArray patchArray = new JSONArray();
            patchArray.put(c.getX());
            patchArray.put(c.getY());

            patchesArray.put(patchArray);
        }
        return patchesArray;
    }

    private static String validateInstructions(final JSONObject jsonObject) throws HooverException {
        String instructions = jsonObject.getString(INSTRUCTIONS);
        String instructionsUpperCase = instructions.toUpperCase();
        instructionsUpperCase = instructionsUpperCase.replace(Hoover.NORTH, "");
        instructionsUpperCase = instructionsUpperCase.replace(Hoover.SOUTH, "");
        instructionsUpperCase = instructionsUpperCase.replace(Hoover.WEST, "");
        instructionsUpperCase = instructionsUpperCase.replace(Hoover.EAST, "");
        if(!instructionsUpperCase.isEmpty())
            throw new HooverException(HooverError.INVALID_INSTRUCTION);
        return instructions;
    }
}
