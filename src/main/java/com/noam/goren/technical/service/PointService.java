package com.noam.goren.technical.service;

import com.noam.goren.technical.model.Point;
import com.noam.goren.technical.repository.PointRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@Transactional
public class PointService {

    private final PointRepository pointRepository;

    @Autowired
    public PointService(final PointRepository pointRepository) {
        this.pointRepository = pointRepository;
    }

    public Point getCoordinate(final int x, final int y, final boolean isDirtPatch) {
        final Point point = pointRepository.findByXAndYAndIsDirtPatch(x, y, isDirtPatch);
        if(point != null)
            return point;
        return createCoordinate(x, y ,isDirtPatch);
    }

    private Point createCoordinate(final int x, final int y, final boolean isDirtPatch) {
        final Point point = new Point(x, y, isDirtPatch);
        return pointRepository.save(point);
    }
}
