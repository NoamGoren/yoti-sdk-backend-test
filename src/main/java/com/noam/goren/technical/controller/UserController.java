package com.noam.goren.technical.controller;


import com.noam.goren.technical.enums.HooverError;
import com.noam.goren.technical.exception.HooverException;
import com.noam.goren.technical.model.Room;
import com.noam.goren.technical.response.FailureResponse;

import com.noam.goren.technical.response.SuccessResponse;
import com.noam.goren.technical.service.HooverService;
import com.noam.goren.technical.service.PointService;
import com.noam.goren.technical.service.RoomService;
import com.noam.goren.technical.utils.JSONParser;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {
    private final RoomService roomService;

    @Autowired
    public UserController(RoomService roomService, HooverService hooverService, PointService pointService) {
        this.roomService = roomService;
    }

    @PostMapping(value = "/hoover", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<String> solver(@RequestBody String problemConstraints) {
        Room room;
        try {
            final JSONObject problemConstraintsJSON = new JSONObject(problemConstraints);
            room = JSONParser.getModelFromJSON(problemConstraintsJSON);
        } catch (final HooverException e) {
            return new ResponseEntity<>(new FailureResponse(e.getError()).toString(), HttpStatus.BAD_REQUEST);
        } catch (final JSONException e2) {
            return new ResponseEntity<>(new FailureResponse(HooverError.FORMAT_NOT_VALID).toString(), HttpStatus.BAD_REQUEST);
        }
        roomService.handleRoomService(room);
        return ResponseEntity.ok(new SuccessResponse(room.getHoover()).toString());
    }
}
