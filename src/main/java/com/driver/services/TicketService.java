//package com.driver.services;
//
//
//import com.driver.EntryDto.BookTicketEntryDto;
//import com.driver.model.Passenger;
//import com.driver.model.Ticket;
//import com.driver.repository.PassengerRepository;
//import com.driver.repository.TicketRepository;
//import com.driver.repository.TrainRepository;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//import java.util.List;
//
//@Service
//public class TicketService {
//
//    @Autowired
//    TicketRepository ticketRepository;
//
//    @Autowired
//    TrainRepository trainRepository;
//
//    @Autowired
//    PassengerRepository passengerRepository;
//
//
//    public Integer bookTicket(BookTicketEntryDto bookTicketEntryDto)throws Exception{
//
//        //Check for validity
//        Integer passengerId  = bookTicketEntryDto.getBookingPersonId();
//
//        if(passengerId == 0) return 0;
//
//        Integer trainId = bookTicketEntryDto.getTrainId();
//
//        if(trainId ==0) return 0;
//
//        //Use bookedTickets List from the TrainRepository to get bookings done against that train
//        List<Ticket> ticketList = trainRepository.findByBookedTickets(trainId);
//
//      //  int noOfSeats = trainRepository.findByNoOfSeats(trainId);
// int noOfSeats = bookTicketEntryDto.getNoOfSeats();
//
//        // Incase the there are insufficient tickets
//
//
//        // throw new Exception("Less tickets are available");
//        if(ticketList.size()<noOfSeats){
//            throw  new Exception("Less tickets are available");
//        }
//        //otherwise book the ticket, calculate the price and other details
//       List<Integer> passengerList =   bookTicketEntryDto.getPassengerIds();
//        passengerList.add(passengerId);
//
//        Ticket bookedTicket =  new Ticket();
//        bookedTicket.setFromStation(bookTicketEntryDto.getFromStation());
//        bookedTicket.setToStation(bookTicketEntryDto.getToStation());
//
//        //bookedTicket.setPassengersList(bookTicketEntryDto.getPassengerIds());
//
//        ticketRepository.save(bookedTicket);
//
//        //Save the information in corresponding DB Tables
//
//        //Fare System : Check problem statement
//
//        //Incase the train doesn't pass through the requested stations
//        //throw new Exception("Invalid stations");
//
//        String s = bookTicketEntryDto.getToStation().toString() + bookTicketEntryDto.getFromStation().toString();
//        String ro = trainRepository.findByRoute(trainId);
//
//        if(!ro.contains(s)){
//            throw  new Exception("Invalid stations");
//        }
//
//        //Save the bookedTickets in the train Object
//
//
//        //Also in the passenger Entity change the attribute bookedTickets by using the attribute bookingPersonId.
//       //And the end return the ticketId that has come from db
//
//
//       return bookedTicket.getTicketId();
//
//    }
//}

package com.driver.services;


import com.driver.EntryDto.BookTicketEntryDto;
import com.driver.model.Passenger;
import com.driver.model.Station;
import com.driver.model.Ticket;
import com.driver.model.Train;
import com.driver.repository.PassengerRepository;
import com.driver.repository.TicketRepository;
import com.driver.repository.TrainRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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

        Train train=trainRepository.findById(bookTicketEntryDto.getTrainId()).get();
        int bookedSeats=0;
        List<Ticket> bookedList= train.getBookedTickets();
        for (Ticket ticket1:bookedList){
            bookedSeats +=ticket1.getPassengersList().size();
        }
        if (bookedSeats+bookTicketEntryDto.getNoOfSeats() > train.getNoOfSeats()){
            throw new Exception("Less tickets are available");
        }


        // setting passengers
        List<Passenger> passengerList= new ArrayList<>();
        List<Integer> ids= bookTicketEntryDto.getPassengerIds();
        for(int id: ids){
            passengerList.add(passengerRepository.findById(id).get());
        }
        // to find from and to station
        String stations[]= train.getRoute().split(",");
        int from=-1;
        int to=-1;

        for(int i=0;i<stations.length;i++){
            if(bookTicketEntryDto.getFromStation().toString().equals(stations[i])){
                from = i;
                break;
            }
        }
        for(int i=0;i<stations.length;i++){
            if(bookTicketEntryDto.getToStation().toString().equals(stations[i])){
                to = i;
                break;
            }
        }
        if(from==-1 || to==-1 || to-from<0){
            throw new Exception("Invalid stations");
        }

        Ticket ticket= new Ticket();
        ticket.setPassengersList(passengerList);
        ticket.setFromStation(bookTicketEntryDto.getFromStation());
        ticket.setToStation(bookTicketEntryDto.getToStation());

        int fair=0;
        fair=bookTicketEntryDto.getNoOfSeats()*(to-from)*300;

        ticket.setTotalFare(fair);
        ticket.setTrain(train);

        //Save the bookedTickets in the train Object
        train.getBookedTickets().add(ticket);
        train.setNoOfSeats(train.getNoOfSeats()- bookTicketEntryDto.getNoOfSeats());

        //Also in the passenger Entity change the attribute bookedTickets by using the attribute bookingPersonId.
        Passenger passenger= passengerRepository.findById(bookTicketEntryDto.getBookingPersonId()).get();
        passenger.getBookedTickets().add(ticket);

        trainRepository.save(train);
        Ticket ticket1= ticketRepository.save(ticket);
        return ticket1.getTicketId();






        //Check for validity
        //Use bookedTickets List from the TrainRepository to get bookings done against that train
        // Incase the there are insufficient tickets
        // throw new Exception("Less tickets are available");
        //otherwise book the ticket, calculate the price and other details
        //Save the information in corresponding DB Tables
        //Fare System : Check problem statement
        //Incase the train doesn't pass through the requested stations
        //throw new Exception("Invalid stations");
        //Save the bookedTickets in the train Object
        //Also in the passenger Entity change the attribute bookedTickets by using the attribute bookingPersonId.
        //And the end return the ticketId that has come from db


    }
}
