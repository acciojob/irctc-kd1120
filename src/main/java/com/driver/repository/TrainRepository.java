package com.driver.repository;

import com.driver.model.Ticket;
import com.driver.model.Train;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalTime;
import java.util.List;


@Repository
public interface TrainRepository extends JpaRepository<Train,Integer> {

    Train findByDepartureTime(LocalTime departureTime);

    List<Ticket> findByBookedTickets(Integer trainId);

    Integer findByNoOfSeats(Integer trainId);

    String findByRoute(Integer trainId);
}
