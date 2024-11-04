package com.ProjectParkingSystem.ParkingSystem.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "CarRegistration")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CarRegistration {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(unique = true, nullable = false)
    private String licenseNumber;
    private String ownerName;
    private String address;


}
