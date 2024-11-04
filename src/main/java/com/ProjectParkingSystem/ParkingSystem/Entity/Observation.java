package com.ProjectParkingSystem.ParkingSystem.Entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Table(name = "Observations")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Observation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String licenseNumber;

   @NotEmpty(message = "StreetName should not be empty")
    private String streetName;

    private LocalDate dateOfObservation;



}
