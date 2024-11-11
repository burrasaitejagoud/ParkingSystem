package com.ProjectParkingSystem.ParkingSystem.Service;

import com.ProjectParkingSystem.ParkingSystem.Entity.CarEntry;
import com.ProjectParkingSystem.ParkingSystem.Exception.CarEntryNotFoundException;
import com.ProjectParkingSystem.ParkingSystem.Repository.CarEntryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Service
public class CarEntryService {

    @Autowired
    private CarEntryRepository carEntryRepository;

    private static final LocalTime FREE_PARKING_START = LocalTime.of(21, 0);
    private static final LocalTime FREE_PARKING_END = LocalTime.of(8, 0);

    // Method to add a new CarEntry with the current start time
    public CarEntry addCarEntry(CarEntry carEntry) {
        carEntry.setStartTime(LocalDateTime.now()); // Set start time to now
        return carEntryRepository.save(carEntry);
    }

    // Method to end a parking session and calculate the total fee
    public CarEntry endParkingSession(String licenseNumber) {

        CarEntry carEntry = carEntryRepository.findByLicenseNumberAndEndTimeIsNull(licenseNumber)
                .orElseThrow(() -> new RuntimeException("Active parking session not found for license: " + licenseNumber));

        carEntry.setEndTime(LocalDateTime.now());// Set end time to now

        // Apply free parking conditions
        if (isFreeParkingPeriod(carEntry.getStartTime(), carEntry.getEndTime())) {
            carEntry.setTotalFee(0L);  // No charge for free parking periods
        } else {
            carEntry.setTotalFee(carEntry.calculateParkingFee());
        }

        carEntryRepository.save(carEntry); // Update record with the end time and total fee

        return carEntry;
    }

    //  method to check if the parking session falls in a free parking period
    private boolean isFreeParkingPeriod(LocalDateTime startTime, LocalDateTime endTime) {
        // Check if the start and end times are on Sunday
        if (startTime.getDayOfWeek() == DayOfWeek.SUNDAY) {
            return true; // Free parking all day on Sunday
        }

        // Check if parking time falls within daily free hours (21:00 - 08:00)
        LocalTime start = startTime.toLocalTime();
        LocalTime end = endTime.toLocalTime();

        // If session starts before or during free period and ends after, it's considered a free period
        if ((start.isAfter(FREE_PARKING_START) || start.equals(FREE_PARKING_START) || start.isBefore(FREE_PARKING_END)) &&
                (end.isBefore(FREE_PARKING_END) || end.equals(FREE_PARKING_END) || end.isAfter(FREE_PARKING_START))) {
            return true;
        }
        return false;
    }

    // Method to delete a CarEntry by its ID
    public void deleteCarEntryById(Long id) {
        if (!carEntryRepository.existsById(id)) {
            throw new CarEntryNotFoundException("Car entry with ID " + id + " not found");
        }
        carEntryRepository.deleteById(id);
    }

    // Method to retrieve all CarEntry records
    public List<CarEntry> getAllCarEntries() {
        return carEntryRepository.findAll();
    }
}
