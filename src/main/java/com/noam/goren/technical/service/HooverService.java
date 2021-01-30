package com.noam.goren.technical.service;

import com.noam.goren.technical.model.Hoover;
import com.noam.goren.technical.model.Point;
import com.noam.goren.technical.repository.HooverRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@Transactional
public class HooverService {

    private final HooverRepository hooverRepository;
    private final PointService pointService;

    @Autowired
    public HooverService(final HooverRepository hooverRepository, final PointService pointRepository) {
        this.hooverRepository = hooverRepository;
        this.pointService = pointRepository;
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