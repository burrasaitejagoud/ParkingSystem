package com.ProjectParkingSystem.ParkingSystem.Entity;


import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name="Cars")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Car {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "License plate is required")
    @Column
    private String licensePlate;

    @NotNull(message = "Owner name is required")
    @Column
    private String ownerName;
}
