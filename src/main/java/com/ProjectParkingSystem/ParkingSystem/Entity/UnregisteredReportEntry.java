package com.ProjectParkingSystem.ParkingSystem.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "UnregisteredReportEntry")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UnregisteredReportEntry {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String licenseNumber;
    private String streetName;
    private LocalDate observationDate;

}