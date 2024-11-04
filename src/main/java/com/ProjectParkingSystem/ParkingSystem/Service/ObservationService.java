package com.ProjectParkingSystem.ParkingSystem.Service;

import com.ProjectParkingSystem.ParkingSystem.Entity.Observation;
import com.ProjectParkingSystem.ParkingSystem.Repository.ObservationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Service
public class ObservationService {
    @Autowired
    private ObservationRepository observationRepository;

    public  ResponseEntity<String> saveObservations(List<Observation> observations) {
        if (!observations.isEmpty()) {
            observationRepository.saveAll(observations);
        }
        return ResponseEntity.ok("Observations uploaded successfully.");
    }

    public List<Observation> getAllObservations() {
        return observationRepository.findAll();
    }

    public void deleteObservationById(Long observationId) {
        Optional<Observation> observation = observationRepository.findById(observationId);
        if (observation.isEmpty()) {
            // Log and throw a ResponseStatusException
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Observation not found");
        }
        observationRepository.deleteById(observationId);
    }

}