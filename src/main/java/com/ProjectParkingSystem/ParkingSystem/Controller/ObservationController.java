package com.ProjectParkingSystem.ParkingSystem.Controller;

import com.ProjectParkingSystem.ParkingSystem.Entity.Observation;
import com.ProjectParkingSystem.ParkingSystem.Service.ObservationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/observations")
public class ObservationController {
    @Autowired
    private ObservationService observationService;

    /*This Method will upload the list of oberservations at a time*/
    @PostMapping("/upload")
    public ResponseEntity<String> uploadObservations(@RequestBody List<Observation> observations) {
        observationService.saveObservations(observations);
        return ResponseEntity.ok("Observations uploaded successfully.");
    }
    /*This Method will upload the single obervation*/
    @PostMapping("/uploading")
    public ResponseEntity<String> uploadObservation(@RequestBody Observation observation) {
        observationService.saveObservations(List.of(observation)); // Convert single observation to list
        return ResponseEntity.ok("Observation uploaded successfully.");
    }
    /*This method will get all the observations from observations table*/
    @GetMapping("/all")
    public ResponseEntity<List<Observation>> getAllObservations() {
        List<Observation> observations = observationService.getAllObservations();
        return ResponseEntity.ok(observations);
    }
    /*This method will delete the observation using id as input*/
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteObservationById(@PathVariable Long id) {
        observationService.deleteObservationById(id);
        return ResponseEntity.ok("Observation deleted successfully.");
    }


}