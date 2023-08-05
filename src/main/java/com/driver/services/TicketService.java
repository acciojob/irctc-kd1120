package com.driver.services;


import com.driver.EntryDto.BookTicketEntryDto;
import com.driver.model.Passenger;
import com.driver.model.Ticket;
import com.driver.repository.PassengerRepository;
import com.driver.repository.TicketRepository;
import com.driver.repository.TrainRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TicketService {

    @Autowired
    TicketRepository ticketRepository;

    @Autowired
    TrainRepository trainRepository;

    @Autowired
    PassengerRepository passengerRepository;


    public Integer bookTicket(BookTicketEntryDto bookTicketEntryDto)throws Exception{

        //Check for validity
        Integer passengerId  = bookTicketEntryDto.getBookingPersonId();

        if(passengerId == 0) return 0;

        Integer trainId = bookTicketEntryDto.getTrainId();

        if(trainId ==0) return 0;

        //Use bookedTickets List from the TrainRepository to get bookings done against that train
        List<Ticket> ticketList = trainRepository.findByBookedTickets(trainId);

      //  int noOfSeats = trainRepository.findByNoOfSeats(trainId);
 int noOfSeats = bookTicketEntryDto.getNoOfSeats();

        // Incase the there are insufficient tickets


        // throw new Exception("Less tickets are available");
        if(ticketList.size()<noOfSeats){
            throw  new Exception("Less tickets are available");
        }
        //otherwise book the ticket, calculate the price and other details
       List<Integer> passengerList =   bookTicketEntryDto.getPassengerIds();
        passengerList.add(passengerId);

        Ticket bookedTicket =  new Ticket();
        bookedTicket.setFromStation(bookTicketEntryDto.getFromStation());
        bookedTicket.setToStation(bookTicketEntryDto.getToStation());

        //bookedTicket.setPassengersList(bookTicketEntryDto.getPassengerIds());

        ticketRepository.save(bookedTicket);

        //Save the information in corresponding DB Tables

        //Fare System : Check problem statement

        //Incase the train doesn't pass through the requested stations
        //throw new Exception("Invalid stations");

        String s = bookTicketEntryDto.getToStation().toString() + bookTicketEntryDto.getFromStation().toString();
        String ro = trainRepository.findByRoute(trainId);

        if(!ro.contains(s)){
            throw  new Exception("Invalid stations");
        }

        //Save the bookedTickets in the train Object


        //Also in the passenger Entity change the attribute bookedTickets by using the attribute bookingPersonId.
       //And the end return the ticketId that has come from db


       return bookedTicket.getTicketId();

    }
}
