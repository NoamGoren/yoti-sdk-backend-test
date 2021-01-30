package com.noam.goren.technical.repository;

import org.springframework.stereotype.Repository;
import com.noam.goren.technical.model.Point;
import org.springframework.data.jpa.repository.JpaRepository;

@Repository
public interface PointRepository extends JpaRepository<Point, Long> {
    Point findByXAndYAndIsDirtPatch(final int x, final int y, final boolean isDirtPatch);

}
