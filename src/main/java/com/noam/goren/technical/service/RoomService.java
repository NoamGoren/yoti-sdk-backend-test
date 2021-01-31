package com.noam.goren.technical.service;

import com.noam.goren.technical.model.Hoover;
import com.noam.goren.technical.model.Point;
import com.noam.goren.technical.model.Room;
import com.noam.goren.technical.repository.RoomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class RoomService {
    private final RoomRepository roomRepository;
    private final HooverService hooverService;
    private final PointService pointService;

    @Autowired
    public RoomService(final RoomRepository roomRepository,
                       final HooverService hooverService,
                       final PointService pointService) {

        this.roomRepository = roomRepository;
        this.hooverService = hooverService;
        this.pointService = pointService;
    }

    public void handleRoomService(Room room) {
        final List<Point> patches = room.getDirtPatchesList();
        final Hoover hoover = room.getHoover();
        final Point currentPosition = new Point(hoover.getStartPosition());
        hooverService.checkIfPatch(patches, hoover,currentPosition);
        hooverService.iterateHooverCommands(room, patches, hoover, currentPosition);
        hoover.setEndPosition(currentPosition);
        save(room);
    }


    public void save(final Room room) {
        if(room == null)
            throw new NullPointerException();
        if(room.getHoover() == null)
            throw new NullPointerException();
        if(room.getDirtPatchesList() == null)
            throw new NullPointerException();
        final List<Point> cachedPatches = new ArrayList<>();
        for(final Point point : room.getDirtPatchesList()) {
            cachedPatches.add(pointService.getCoordinate(point.getX(), point.getY(), true));
        }
        room.setDirtPatchesList(cachedPatches);
        roomRepository.save(room);
    }

}
