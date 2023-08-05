package com.driver.repository;

import com.driver.model.Train;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalTime;
import java.util.Optional;


@Repository
public interface TrainRepository extends JpaRepository<Train,Integer> {

    Train findByDepartureTime(LocalTime departureTime);
}
