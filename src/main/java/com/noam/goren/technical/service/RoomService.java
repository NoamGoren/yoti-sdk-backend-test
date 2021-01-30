package com.noam.goren.technical.service;

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

    public Room save(final Room room) {
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
        return roomRepository.save(room);
    }

}
