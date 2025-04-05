package com.example.flight_service.controller;

import com.example.flight_service.dto.FlightDTO;
import com.example.flight_service.entity.Flight;
import com.example.flight_service.entity.FlightDetails;
import com.example.flight_service.service.FlightService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/flights")
public class FlightController {

    @Autowired
    private FlightService flightService;

    // adding new flight
    @PostMapping("add")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Flight> addFlight(@RequestBody Flight flight) {
        Flight savedFlight = flightService.addFlight(flight);
        return new ResponseEntity<>(savedFlight, HttpStatus.CREATED);
    }

    // updating flight details (Admin Only)
    @PutMapping("update/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Flight> updateFlight(@PathVariable Integer id, @RequestBody Flight flightDetails) {
        Flight updatedFlight = flightService.updateFlight(id, flightDetails);
        return new ResponseEntity<>(updatedFlight, HttpStatus.OK);
    }

    // removing a flight (Admin Only)
    @DeleteMapping("delete/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Flight> deleteFlight(@PathVariable Integer id) {
        Flight deletedFlight = flightService.deleteFlight(id);
        return new ResponseEntity<>(deletedFlight, HttpStatus.OK);
    }

    // fetch available flights - having available seats
    @GetMapping("available")
    public ResponseEntity<List<FlightDTO>> getAvailableFlights() {
        List<FlightDTO> availableFlights = flightService.getAvailableFlights();
        return new ResponseEntity<>(availableFlights, HttpStatus.OK);
    }

    // fetch all flights
    @GetMapping("/all")
    public ResponseEntity<List<FlightDTO>> getAllFlights() {
        List<FlightDTO> flights = flightService.getAllFlights();
        return new ResponseEntity<>(flights, HttpStatus.OK);
    }

    @GetMapping("/check-availability")
    @PreAuthorize("hasRole('PASSENGER')")
    public ResponseEntity<Boolean> isSeatAvailable(@RequestParam Integer flightId) {
        System.out.println("Reached availability");
        boolean available = flightService.isSeatAvailable(flightId);
        return ResponseEntity.ok(available);
    }

    @PutMapping("/book-seats")
    @PreAuthorize("hasRole('PASSENGER')")
    public ResponseEntity<FlightDetails> bookSeat(@RequestParam Integer flightId) {

        return ResponseEntity.ok(flightService.bookSeat(flightId));
    }

    @PutMapping("/cancel-seat")
    public ResponseEntity<String> cancelSeat(@RequestParam Integer flightId, @RequestParam String seatNumber){
        try {
            flightService.cancelSeat(flightId, seatNumber);
            return ResponseEntity.ok("Seat cancellation successful!");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("getDetails")
    public ResponseEntity<FlightDetails> getDetails(@RequestParam Integer flightId) {
        FlightDetails details = flightService.getFlightDetailsById(flightId);
        return ResponseEntity.ok(details);
    }

}