package com.noam.goren.technical.integration;

import com.noam.goren.technical.model.Hoover;
import com.noam.goren.technical.model.Point;
import com.noam.goren.technical.model.Room;
import com.noam.goren.technical.utils.JSONParser;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@Rollback
@Transactional
public class UserControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Test
    public void testEmptyBody() throws Exception {
        mockMvc.perform(post("/hoover")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isBadRequest())
                .andReturn();
    }

    @Test
    public void testWrongFormat() throws Exception {
        final MvcResult mvcResult = mockMvc.perform(post("/hoover")
                .content("{\"roomSize\": [4, 5],\"coords\": \"3, 3\",\"instructions\": \"NSWE\",\"patches\": [[0, 1],[1, 1],[2, 1]]}")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isBadRequest())
                .andReturn();
        final String responseBody = mvcResult.getResponse().getContentAsString();
        final JSONObject jsonResponse = new JSONObject(responseBody);
        Assert.assertEquals("FORMAT_NOT_VALID", jsonResponse.getString("error_code"));
        Assert.assertEquals("Format of the request is invalid.", jsonResponse.getString("error_message"));
    }

    @Test
    public void testInvalidRoomSize() throws Exception {
        final Point startPosition = new Point(2,2);
        final String instructions = "NSWE";
        final Hoover hoover = new Hoover(startPosition, instructions);

        final List<Point> patches = new ArrayList<>();
        final Room room = new Room(0, 10, patches, hoover);

        final MvcResult mvcResult = mockMvc.perform(post("/hoover")
                .content(JSONParser.getJSONObjectFromModel(room).toString())
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isBadRequest())
                .andReturn();

        final String responseBody = mvcResult.getResponse().getContentAsString();
        final JSONObject jsonResponse = new JSONObject(responseBody);

        Assert.assertEquals("INVALID_ROOM_DIMENSION", jsonResponse.getString("error_code"));
        Assert.assertEquals("Invalid Room Size.", jsonResponse.getString("error_message"));
    }

    @Test
    public void testInvalidStartPosition() throws Exception {
        final Point startPosition = new Point(10,2);
        final String instructions = "NSWE";
        final Hoover hoover = new Hoover(startPosition, instructions);

        final List<Point> patches = new ArrayList<>();
        final Room room = new Room(10, 10, patches, hoover);

        final MvcResult mvcResult = mockMvc.perform(post("/hoover")
                .content(JSONParser.getJSONObjectFromModel(room).toString())
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isBadRequest())
                .andReturn();
        final String responseBody = mvcResult.getResponse().getContentAsString();
        final JSONObject jsonResponse = new JSONObject(responseBody);
        Assert.assertEquals("INVALID_START_POSITION", jsonResponse.getString("error_code"));
        Assert.assertEquals("The initial position is invalid.", jsonResponse.getString("error_message"));
    }

    @Test
    public void testSuccessExample() throws Exception {
        final Point startPosition = new Point(0,0);
        final String instructions = "NENNEEEESWNNW";
        final Hoover hoover = new Hoover(startPosition, instructions);
        final List<Point> patches = new ArrayList<>();
        patches.add(new Point(1,1, true));
        patches.add(new Point(6,2, true));
        patches.add(new Point(1,2, true));
        patches.add(new Point(1,3, true));
        patches.add(new Point(1,7, true));
        patches.add(new Point(9,9, true));
        patches.add(new Point(6,6, true));
        patches.add(new Point(5,5, true));
        patches.add(new Point(0,9, true));
        final Room room = new Room(10, 10, patches, hoover);
        final MvcResult mvcResult = mockMvc.perform(post("/hoover")
                .content(JSONParser.getJSONObjectFromModel(room).toString())
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andReturn();
        final String responseBody = mvcResult.getResponse().getContentAsString();
        final JSONObject jsonResponse = new JSONObject(responseBody);
        final JSONArray endPosition = jsonResponse.getJSONArray("coords");
        Assert.assertEquals(3, endPosition.getInt(0));
        Assert.assertEquals(4, endPosition.getInt(1));
        Assert.assertEquals(3, jsonResponse.getInt("patches"));
    }

    @Test
    public void testExample() throws Exception {
        final Point startPosition = new Point(1,2);
        final String instructions = "NNESEESWNWW";
        final Hoover hoover = new Hoover(startPosition, instructions);
        final List<Point> patches = new ArrayList<>();
        patches.add(new Point(1,0, true));
        patches.add(new Point(2,2, true));
        patches.add(new Point(2,3, true));
        final Room room = new Room(5, 5, patches, hoover);
        final MvcResult mvcResult = mockMvc.perform(post("/hoover")
                .content(JSONParser.getJSONObjectFromModel(room).toString())
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andReturn();
        final String responseBody = mvcResult.getResponse().getContentAsString();
        final JSONObject jsonResponse = new JSONObject(responseBody);
        final JSONArray endPosition = jsonResponse.getJSONArray("coords");
        Assert.assertEquals(1, endPosition.getInt(0));
        Assert.assertEquals(3, endPosition.getInt(1));
        Assert.assertEquals(1, jsonResponse.getInt("patches"));
    }


}
