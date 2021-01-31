package com.noam.goren.technical.service;

import com.noam.goren.technical.model.Hoover;
import com.noam.goren.technical.model.Point;
import com.noam.goren.technical.model.Room;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional
public class HooverService {
    private static final int LOWER_BOUND = 0;

    @Autowired
    public HooverService() {
    }

    public void checkIfPatch(List<Point> patches, Hoover hoover, Point currentPosition) {
        if (patches.contains(currentPosition)) {
            hoover.increasePatchCount();
            patches.remove(currentPosition);
        }
    }

    public void iterateHooverCommands(Room room, List<Point> patches, Hoover hoover, Point currentPosition) {
        for(final char command : hoover.getInstructions().toCharArray()) {
            final String sCommand = String.valueOf(command).toUpperCase();
            moveHoover(room, currentPosition, sCommand);
            checkIfPatch(patches, hoover, currentPosition);
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