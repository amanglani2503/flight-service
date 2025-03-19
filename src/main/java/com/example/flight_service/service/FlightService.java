package com.example.flight_service.service;

import com.example.flight_service.dto.FlightDTO;
import com.example.flight_service.entity.Flight;
import com.example.flight_service.repository.FlightRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

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
}
