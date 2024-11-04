package com.ProjectParkingSystem.ParkingSystem.Service;

import com.ProjectParkingSystem.ParkingSystem.Entity.CarEntry;
import com.ProjectParkingSystem.ParkingSystem.Exception.CarEntryNotFoundException;
import com.ProjectParkingSystem.ParkingSystem.Repository.CarEntryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;


@Service
public class CarEntryService {
    @Autowired
    private CarEntryRepository carEntryRepository;

    public CarEntry addCarEntry(CarEntry carEntry) {
        carEntry.setStartTime(LocalDateTime.now()); // Set start time to now
        return carEntryRepository.save(carEntry);
    }

    public CarEntry endParkingSession(String licenseNumber) {
        CarEntry carEntry = carEntryRepository.findByLicenseNumberAndEndTimeIsNull(licenseNumber)
                .orElseThrow(() -> new RuntimeException("Active parking session not found for license: " + licenseNumber));

        carEntry.setEndTime(LocalDateTime.now());  // Set end time to now
        carEntry.setTotalFee(carEntry.calculateParkingFee());
        carEntryRepository.save(carEntry); // Update record with the end time and total fee

        return carEntry;
    }

    public void deleteCarEntryById(Long id) {
        if (!carEntryRepository.existsById(id)) {
            throw new CarEntryNotFoundException("Car entry with ID " + id + " not found");
        }

        carEntryRepository.deleteById(id);
    }

    public List<CarEntry> getAllCarEntries() {
        return carEntryRepository.findAll(); // Retrieve all CarEntry records
    }

}