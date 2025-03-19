package com.example.flight_service.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "flights")
public class Flight {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String airline;
    private String departure;
    private String destination;
    private LocalDateTime departureTime;
    private LocalDateTime arrivalTime;
    private int availableSeats;
    private double price;

    @OneToMany(mappedBy = "flight", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference  // Prevents infinite recursion
    private List<Seat> seats = new ArrayList<>();

    @PostPersist
    public void initializeSeats() {
        if (seats == null || seats.isEmpty()) {
            List<Seat> newSeats = new ArrayList<>();
            for (int row = 1; row <= 20; row++) {
                for (char col = 'A'; col <= 'F'; col++) {
                    Seat seat = Seat.builder()
                            .seatNumber(row + String.valueOf(col))
                            .status(SeatStatus.AVAILABLE)
                            .flight(this)  // Set flight reference explicitly
                            .build();
                    newSeats.add(seat);
                }
            }
            this.seats = newSeats;
        }
    }
}
