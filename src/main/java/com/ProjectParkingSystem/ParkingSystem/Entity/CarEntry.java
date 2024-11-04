package com.ProjectParkingSystem.ParkingSystem.Entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Duration;
import java.time.LocalDateTime;

import static com.ProjectParkingSystem.ParkingSystem.Constants.RateMap.RATE_MAP;

@Entity
@Table
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CarEntry {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotEmpty(message = "License Number should not be empty")
    private String licenseNumber;

    @NotEmpty(message = "StreetName should not be empty")
    @Pattern(regexp = "Azure|Jakarta|Java|Springboot",message = "Streetname must be one of these places Azure , Jakarta , Java , Springboot")
    private String streetName;

    private LocalDateTime startTime;

    private LocalDateTime endTime;

    private Long totalFee; // Stored in cents



    public long calculateParkingFee() {
        if (startTime == null || endTime == null) {
            throw new IllegalStateException("Start time or end time is not set");
        }

        long minutes = Duration.between(startTime, endTime).toMinutes();
        int rate = RATE_MAP.getOrDefault(streetName, 0);
        return minutes * rate;
    }
}
