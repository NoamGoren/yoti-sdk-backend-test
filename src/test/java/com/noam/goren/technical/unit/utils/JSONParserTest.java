package com.noam.goren.technical.unit.utils;

import com.noam.goren.technical.enums.HooverError;
import com.noam.goren.technical.exception.HooverException;
import com.noam.goren.technical.model.Hoover;
import com.noam.goren.technical.model.Point;
import com.noam.goren.technical.model.Room;
import com.noam.goren.technical.utils.JSONParser;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class JSONParserTest {
    @Test(expected = NullPointerException.class)
    public void testGetModelFromJSONWithNullJSON() throws Exception {
        JSONParser.getModelFromJSON(null);
    }

    @Test(expected = JSONException.class)
    public void testGetModelFromJSONWithInvalidTypeRoomSize() throws Exception {
        final String sampleJSON = "{\"roomSize\": \"4, 5\",\"coords\": [3, 3],\"instructions\": \"NSWE\",\"patches\": [[0, 1],[1, 1],[2, 1]]}";

        final JSONObject jsonObject = new JSONObject(sampleJSON);
        JSONParser.getModelFromJSON(jsonObject);
    }

    @Test(expected = JSONException.class)
    public void testGetModelFromJSONWithInvalidTypeStartPosition() throws Exception {
        final String sampleJSON = "{\"roomSize\": [4, 5],\"coords\": \"3, 3\",\"instructions\": \"NSWE\",\"patches\": [[0, 1],[1, 1],[2, 1]]}";

        final JSONObject jsonObject = new JSONObject(sampleJSON);
        JSONParser.getModelFromJSON(jsonObject);
    }

    @Test(expected = HooverException.class)
    public void testGetModelFromJSONWithInvalidTypeInstructions() throws Exception {
        final String sampleJSON = "{\"roomSize\": [4, 5],\"coords\": [3, 3],\"instructions\": 1,\"patches\": [[0, 1],[1, 1],[2, 1]]}";
        final JSONObject jsonObject = new JSONObject(sampleJSON);
        JSONParser.getModelFromJSON(jsonObject);
    }

    @Test(expected = JSONException.class)
    public void testGetModelFromJSONWithInvalidTypePatches() throws Exception {
        final String sampleJSON = "{\"roomSize\": [4, 5],\"coords\": [3, 3],\"instructions\": \"NSWE\",\"patches\": 1";
        final JSONObject jsonObject = new JSONObject(sampleJSON);
        JSONParser.getModelFromJSON(jsonObject);
    }

    @Test(expected = JSONException.class)
    public void testGetModelFromJSONWithInvalidTypePatch() throws Exception {
        final String sampleJSON = "{\"roomSize\": [4, 5],\"coords\": [3, 3],\"instructions\": \"NSWE\",\"patches\": [1,2,3]}";
        final JSONObject jsonObject = new JSONObject(sampleJSON);
        JSONParser.getModelFromJSON(jsonObject);
    }

    @Test
    public void testGetModelFromJSONWithInvalidRoomSizeWidth() throws Exception {
        final String sampleJSON = "{\"roomSize\": [0, 5],\"coords\": [3, 3],\"instructions\": \"NSWE\",\"patches\": [[0, 1],[1, 1],[2, 1]]}";

        final JSONObject jsonObject = new JSONObject(sampleJSON);

        try {
            JSONParser.getModelFromJSON(jsonObject);
            Assert.fail();
        } catch(final HooverException e) {
            Assert.assertEquals(HooverError.INVALID_ROOM_DIMENSION, e.getError());
        }
    }

    @Test
    public void testGetModelFromJSONWithInvalidRoomSizeHeight() throws Exception {
        final String sampleJSON = "{\"roomSize\": [4, 0],\"coords\": [3, 3],\"instructions\": \"NSWE\",\"patches\": [[0, 1],[1, 1],[2, 1]]}";

        final JSONObject jsonObject = new JSONObject(sampleJSON);

        try {
            JSONParser.getModelFromJSON(jsonObject);
            Assert.fail();
        } catch(final HooverException e) {
            Assert.assertEquals(HooverError.INVALID_ROOM_DIMENSION, e.getError());
        }
    }

    @Test
    public void testGetModelFromJSONWithInvalidStartPositionX() throws Exception {
        final String sampleJSON = "{\"roomSize\": [4, 5],\"coords\": [4, 3],\"instructions\": \"NSWE\",\"patches\": [[0, 1],[1, 1],[2, 1]]}";

        final JSONObject jsonObject = new JSONObject(sampleJSON);

        try {
            JSONParser.getModelFromJSON(jsonObject);
            Assert.fail();
        } catch(final HooverException e) {
            Assert.assertEquals(HooverError.INVALID_START_POSITION, e.getError());
        }
    }

    @Test
    public void testGetModelFromJSONWithInvalidStartPositionY() throws Exception {
        final String sampleJSON = "{\"roomSize\": [4, 5],\"coords\": [2, 5],\"instructions\": \"NSWE\",\"patches\": [[0, 1],[1, 1],[2, 1]]}";

        final JSONObject jsonObject = new JSONObject(sampleJSON);

        try {
            JSONParser.getModelFromJSON(jsonObject);
            Assert.fail();
        } catch(final HooverException e) {
            Assert.assertEquals(HooverError.INVALID_START_POSITION, e.getError());
        }
    }

    @Test
    public void testGetModelFromJSONWithInvalidInstructionSet() throws Exception {
        final String sampleJSON = "{\"roomSize\": [4, 5],\"coords\": [2, 2],\"instructions\": \"NSOE\",\"patches\": [[0, 1],[1, 1],[2, 1]]}";

        final JSONObject jsonObject = new JSONObject(sampleJSON);

        try {
            JSONParser.getModelFromJSON(jsonObject);
            Assert.fail();
        } catch(final HooverException e) {
            Assert.assertEquals(HooverError.INVALID_INSTRUCTION, e.getError());
        }
    }

    @Test
    public void testGetModelFromJSONWithInvalidPatchPositionX() throws Exception {
        final String sampleJSON = "{\"roomSize\": [4, 5],\"coords\": [2, 2],\"instructions\": \"NSWE\",\"patches\": [[0, 1],[4, 1],[2, 1]]}";

        final JSONObject jsonObject = new JSONObject(sampleJSON);

        try {
            JSONParser.getModelFromJSON(jsonObject);
            Assert.fail();
        } catch(final HooverException e) {
            Assert.assertEquals(HooverError.INVALID_PATCH_POSITION, e.getError());
        }
    }

    @Test
    public void testGetModelFromJSONWithInvalidPatchPositionY() throws Exception {
        final String sampleJSON = "{\"roomSize\": [4, 5],\"coords\": [2, 2],\"instructions\": \"NSWE\",\"patches\": [[0, 1],[1, 5],[2, 1]]}";

        final JSONObject jsonObject = new JSONObject(sampleJSON);

        try {
            JSONParser.getModelFromJSON(jsonObject);
            Assert.fail();
        } catch(final HooverException e) {
            Assert.assertEquals(HooverError.INVALID_PATCH_POSITION, e.getError());
        }
    }

    @Test
    public void testGetModelFromJSON() throws Exception {
        final String sampleJSON = "{\"roomSize\": [20, 25],\"coords\": [12, 11],\"instructions\": \"NSWENNWSSS\",\"patches\": [[0, 1],[2, 3],[4, 5]]}";
        final JSONObject jsonObject = new JSONObject(sampleJSON);
        final Room room = JSONParser.getModelFromJSON(jsonObject);
        Assert.assertEquals(20, room.getWidth());
        Assert.assertEquals(25, room.getHeight());
        Assert.assertEquals(12, room.getHoover().getStartPosition().getX());
        Assert.assertEquals(11, room.getHoover().getStartPosition().getY());
        Assert.assertFalse(room.getHoover().getStartPosition().isDirtPatch());
        Assert.assertEquals("NSWENNWSSS", room.getHoover().getInstructions());
        Assert.assertEquals(0, room.getDirtPatchesList().get(0).getX());
        Assert.assertEquals(1,  room.getDirtPatchesList().get(0).getY());
        Assert.assertTrue( room.getDirtPatchesList().get(0).isDirtPatch());
        Assert.assertEquals(2,  room.getDirtPatchesList().get(1).getX());
        Assert.assertEquals(3,  room.getDirtPatchesList().get(1).getY());
        Assert.assertTrue( room.getDirtPatchesList().get(1).isDirtPatch());
        Assert.assertEquals(4,  room.getDirtPatchesList().get(2).getX());
        Assert.assertEquals(5,  room.getDirtPatchesList().get(2).getY());
        Assert.assertTrue( room.getDirtPatchesList().get(2).isDirtPatch());
    }

    @Test(expected = NullPointerException.class)
    public void testGetJSONObjectFromModelWithRoomNull() throws JSONException {
        JSONParser.getJSONObjectFromModel(null);
    }

    @Test
    public void testGetJSONObjectFromModel() throws JSONException {
        final Point startPosition = new Point(1,2);
        final String instructions = "NSWENNSS";
        final Hoover hoover = new Hoover(startPosition, instructions);
        final List<Point> patches = new ArrayList<>();
        patches.add(new Point(2,3, true));
        patches.add(new Point(4,5, true));
        patches.add(new Point(6,7, true));
        final Room room = new Room(18,20, patches, hoover);
        final JSONObject jsonObject = JSONParser.getJSONObjectFromModel(room);
        Assert.assertEquals("{\"instructions\":\"NSWENNSS\",\"roomSize\":[18,20],\"patches\":[[2,3],[4,5],[6,7]],\"coords\":[1,2]}", jsonObject.toString());
    }
}
