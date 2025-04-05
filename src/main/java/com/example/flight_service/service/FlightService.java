package com.example.flight_service.service;

import com.example.flight_service.dto.FlightDTO;
import com.example.flight_service.entity.Flight;
import com.example.flight_service.entity.FlightDetails;
import com.example.flight_service.entity.Seat;
import com.example.flight_service.entity.SeatStatus;
import com.example.flight_service.repository.FlightRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class FlightService {
    @Autowired
    private FlightRepository flightRepository;

    public Flight addFlight(Flight flight) {
        return flightRepository.save(flight);
    }

    public Flight updateFlight(Integer id, Flight flightDetails) {
        return flightRepository.findById(id)
                .map(flight -> {
                    flight.setAirline(flightDetails.getAirline());
                    flight.setDeparture(flightDetails.getDeparture());
                    flight.setDestination(flightDetails.getDestination());
                    flight.setDepartureTime(flightDetails.getDepartureTime());
                    flight.setArrivalTime(flightDetails.getArrivalTime());
                    flight.setAvailableSeats(flightDetails.getAvailableSeats());
                    flight.setPrice(flightDetails.getPrice());
                    return flightRepository.save(flight);
                })
                .orElseThrow(() -> new RuntimeException("Flight not found with ID: " + id));
    }

    public Flight deleteFlight(Integer id) {
        Flight flight = flightRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Flight not found with ID: " + id));

        flightRepository.deleteById(id);
        return flight;
    }

    public List<FlightDTO> getAvailableFlights() {
        List<Flight> availableFlights = flightRepository.findByAvailableSeatsGreaterThan(0); // Fetch only flights with available seats

        return availableFlights.stream()
                .map(flight -> FlightDTO.builder()
                        .id(flight.getId())
                        .airline(flight.getAirline())
                        .departure(flight.getDeparture())
                        .destination(flight.getDestination())
                        .departureTime(flight.getDepartureTime())
                        .arrivalTime(flight.getArrivalTime())
                        .availableSeats(flight.getAvailableSeats())
                        .price(flight.getPrice())
                        .build())
                .toList();
    }


    public List<FlightDTO> getAllFlights() {
        List<Flight> availableFlights = flightRepository.findAll();

        return availableFlights.stream()
                .map(flight -> FlightDTO.builder()
                        .id(flight.getId())
                        .airline(flight.getAirline())
                        .departure(flight.getDeparture())
                        .destination(flight.getDestination())
                        .departureTime(flight.getDepartureTime())
                        .arrivalTime(flight.getArrivalTime())
                        .availableSeats(flight.getAvailableSeats())
                        .price(flight.getPrice())
                        .build())
                .toList();
    }

    public boolean isSeatAvailable(Integer flightId) {
        Optional<Flight> flightOpt = flightRepository.findById(flightId);
        return flightOpt.map(f -> f.getSeats().stream()
                        .anyMatch(seat -> seat.getStatus() == SeatStatus.AVAILABLE))
                .orElse(false);
    }


    public FlightDetails bookSeat(Integer flightId) {
        Optional<Flight> flightOpt = flightRepository.findById(flightId);

        if (flightOpt.isEmpty()) {
            throw new RuntimeException("Flight not found!");
        }

        Flight flight = flightOpt.get();

        // Find and book the first available seat
        Optional<Seat> seatToBook = flight.getSeats().stream()
                .filter(seat -> seat.getStatus() == SeatStatus.AVAILABLE)
                .findFirst();

        if (seatToBook.isPresent()) {
            Seat seat = seatToBook.get();
            seat.setStatus(SeatStatus.BOOKED); // Mark seat as booked

            // Update availableSeats count
            long updatedCount = flight.getSeats().stream()
                    .filter(s -> s.getStatus() == SeatStatus.AVAILABLE)
                    .count();
            flight.setAvailableSeats((int) updatedCount);

            flightRepository.save(flight); // Save updated flight details

            // Return FlightDetails with booked seat information
            return new FlightDetails(flight.getAirline(), seat.getSeatNumber(), flight.getDeparture(), flight.getDestination(), flight.getDepartureTime(), flight.getArrivalTime(), flight.getPrice());
        } else {
            throw new RuntimeException("No available seats in this flight!");
        }
    }


    public void cancelSeat(Integer flightId, String seatNumber) {
        // Find the flight
        Flight flight = flightRepository.findById(flightId)
                .orElseThrow(() -> new RuntimeException("Flight not found with ID: " + flightId));

        // Find the seat by seat number
        Optional<Seat> seatOpt = flight.getSeats().stream()
                .filter(seat -> seat.getSeatNumber().equals(seatNumber) && seat.getStatus() == SeatStatus.BOOKED)
                .findFirst();

        if (seatOpt.isPresent()) {
            Seat seat = seatOpt.get();
            seat.setStatus(SeatStatus.AVAILABLE); // Mark seat as available

            // Update availableSeats count
            flight.setAvailableSeats(flight.getAvailableSeats() + 1);

            // Save the updated flight
            flightRepository.save(flight);
        } else {
            throw new RuntimeException("Seat not found or already available!");
        }
    }

    public FlightDetails getFlightDetailsById(Integer flightId) {
        Flight flight = flightRepository.findById(flightId)
                .orElseThrow(() -> new RuntimeException("Flight not found with ID: " + flightId));

        return new FlightDetails(
                flight.getAirline(),
                null, // seat number is not needed here
                flight.getDeparture(),
                flight.getDestination(),
                flight.getDepartureTime(),
                flight.getArrivalTime(),
                flight.getPrice()
        );
    }

}
