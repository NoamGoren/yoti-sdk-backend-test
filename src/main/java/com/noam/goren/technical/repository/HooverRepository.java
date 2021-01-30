package com.noam.goren.technical.repository;

import com.noam.goren.technical.model.Hoover;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface HooverRepository extends JpaRepository<Hoover, Long> {

}
