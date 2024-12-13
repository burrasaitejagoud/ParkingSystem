package com.ProjectParkingSystem.ParkingSystem.Entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "Observations")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Observation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Pattern(regexp = "^[a-zA-Z0-9]*$", message = "License number must contain only alphanumeric characters")
    private String licenseNumber;

   @NotEmpty(message = "StreetName should not be empty")
   @Pattern(regexp = "Azure|Jakarta|Java|Spring",message = "Streetname must be one of these places Azure , Jakarta , Java , Spring")
   private String streetName;

    @Column(nullable = false)
    @CreationTimestamp
    private LocalDateTime dateOfObservation;



}
