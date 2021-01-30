package com.noam.goren.technical.service;

import com.noam.goren.technical.model.Hoover;
import com.noam.goren.technical.model.Point;
import com.noam.goren.technical.model.Room;
import com.noam.goren.technical.repository.HooverRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional
public class HooverService {
    private static final int LOWER_BOUND = 0;
    private final HooverRepository hooverRepository;
    private final PointService pointService;

    @Autowired
    public HooverService(final HooverRepository hooverRepository, final PointService pointRepository) {
        this.hooverRepository = hooverRepository;
        this.pointService = pointRepository;
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

    public Hoover save(final Hoover hoover) {
        if((hoover == null) || (hoover.getEndPosition() == null))
            throw new NullPointerException();
        final Point startPosition = hoover.getStartPosition();
        final Point endPosition = hoover.getEndPosition();
        hoover.setStartPosition(pointService.getCoordinate(startPosition.getX(), startPosition.getY(), false));
        hoover.setEndPosition(pointService.getCoordinate(endPosition.getX(), endPosition.getY(), false));
        return hooverRepository.save(hoover);
    }
}