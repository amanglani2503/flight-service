package com.example.flight_service.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/flights")
public class FlightController {

    // adding new flight
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Map<String, String>> addFlight() {
        Map<String, String> response = new HashMap<>();
        response.put("message", "Flight added successfully!");
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    // updating flight details (Admin Only)
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Map<String, String>> updateFlight(@PathVariable String id) {
        Map<String, String> response = new HashMap<>();
        response.put("message", "Flight with ID " + id + " updated successfully!");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    // removing a flight (Admin Only)
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Map<String, String>> deleteFlight(@PathVariable String id) {
        Map<String, String> response = new HashMap<>();
        response.put("message", "Flight with ID " + id + " deleted successfully!");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    // fetch available flights
    @GetMapping
    public ResponseEntity<Map<String, Object>> getAvailableFlights() {
        Map<String, Object> response = new HashMap<>();
        response.put("flights", new String[]{"Flight A", "Flight B", "Flight C"});
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}