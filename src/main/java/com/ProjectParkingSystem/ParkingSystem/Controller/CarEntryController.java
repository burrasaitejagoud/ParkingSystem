package com.ProjectParkingSystem.ParkingSystem.Controller;

import com.ProjectParkingSystem.ParkingSystem.Dto.ExitRequest;
import com.ProjectParkingSystem.ParkingSystem.Entity.CarEntry;
import com.ProjectParkingSystem.ParkingSystem.Service.CarEntryService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/parking")
public class CarEntryController {
    @Autowired
    private CarEntryService carEntryService;

    /*This method will add the cars to the CarEntry Table*/
    @PostMapping("/enter")
    public ResponseEntity<CarEntry> addCarEntry(@Valid @RequestBody CarEntry carEntry) {
        CarEntry savedEntry = carEntryService.addCarEntry(carEntry);
        return ResponseEntity.ok(savedEntry);
    }
    /*This method will exit the parking session  where license number is used as input in request param- Not good approach*/
    @PostMapping("/exit")
    public ResponseEntity<String> endParkingSession( @RequestParam String licenseNumber) {
        CarEntry carEntry = carEntryService.endParkingSession(licenseNumber);
        long fee = carEntry.getTotalFee();
        return ResponseEntity.ok("Parking ended. Total fee: " + fee + " cents");
    }
    /*This method with exit the parking session where license number is passed as input*/
    @PostMapping("/exit2")
    public ResponseEntity<String> endParkingSession( @RequestBody ExitRequest exitRequest) {
        CarEntry carEntry = carEntryService.endParkingSession(exitRequest.getLicenseNumber());
        long fee = carEntry.getTotalFee();
        return ResponseEntity.ok("Parking ended. Total fee: " + fee + " cents");
    }
    /*This method will delete the car entry manually */
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteCarEntry(@PathVariable Long id) {
        carEntryService.deleteCarEntryById(id);
        return ResponseEntity.ok("Car entry deleted successfully");
    }
    /*This method will get all the carEntries*/
    @GetMapping("/all")
    public ResponseEntity<List<CarEntry>> getAllCarEntries() {
        List<CarEntry> carEntries = carEntryService.getAllCarEntries();
        return ResponseEntity.ok(carEntries);
    }


}