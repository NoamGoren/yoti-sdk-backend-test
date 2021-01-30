package com.noam.goren.technical.controller;


import com.noam.goren.technical.enums.HooverError;
import com.noam.goren.technical.exception.HooverException;
import com.noam.goren.technical.model.Hoover;
import com.noam.goren.technical.model.Point;
import com.noam.goren.technical.model.Room;
import com.noam.goren.technical.response.FailureResponse;

import com.noam.goren.technical.response.SuccessResponse;
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

import java.util.List;


@RestController
public class UserController {
    private static final int LOWER_BOUND = 0;
    private final RoomService roomService;

    @Autowired
    public UserController(RoomService roomService) {
        this.roomService = roomService;
    }

    @PostMapping(value = "/hoover", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<String> solver(@RequestBody String problemConstraints) {
        Room room = null;
        try {
            final JSONObject problemConstraintsJSON = new JSONObject(problemConstraints);
            room = JSONParser.getModelFromJSON(problemConstraintsJSON);
        } catch (final HooverException e) {
            return new ResponseEntity<>(new FailureResponse(e.getError()).toString(), HttpStatus.BAD_REQUEST);
        } catch (final JSONException e2) {
            return new ResponseEntity<>(new FailureResponse(HooverError.FORMAT_NOT_VALID).toString(), HttpStatus.BAD_REQUEST);
        }
        final List<Point> patches = room.getDirtPatchesList();
        final Hoover hoover = room.getHoover();
        final Point currentPosition = new Point(hoover.getStartPosition());
        checkIfPatch(patches, hoover,currentPosition);
        iterateHooverCommands(room, patches, hoover, currentPosition);
        hoover.setEndPosition(currentPosition);
        roomService.save(room);
        return ResponseEntity.ok(new SuccessResponse(room.getHoover()).toString());
    }

    private void iterateHooverCommands(Room room, List<Point> patches, Hoover hoover, Point currentPosition) {
        for(final char command : hoover.getInstructions().toCharArray()) {
            final String sCommand = String.valueOf(command).toUpperCase();
            moveHoover(room, currentPosition, sCommand);
            checkIfPatch(patches, hoover, currentPosition);
        }
    }

    private void checkIfPatch(List<Point> patches, Hoover hoover, Point currentPosition) {
        if (patches.contains(currentPosition)) {
            hoover.increasePatchCount();
            patches.remove(currentPosition);
        }
    }

    private void moveHoover(Room room, Point currentPosition, String sCommand) {
        int x = currentPosition.getX();
        int y = currentPosition.getY();
        switch (sCommand) {
            case Hoover.NORTH:
                if(y < (room.getHeight() - 1))
                    y++;
                break;
            case Hoover.SOUTH:
                if(y > LOWER_BOUND)
                    y--;
                break;
            case Hoover.WEST:
                if(x > LOWER_BOUND)
                    x--;
                break;
            default:
                if(x < (room.getWidth() - 1))
                    x++;
                break;
        }
        currentPosition.setX(x);
        currentPosition.setY(y);
    }
}
