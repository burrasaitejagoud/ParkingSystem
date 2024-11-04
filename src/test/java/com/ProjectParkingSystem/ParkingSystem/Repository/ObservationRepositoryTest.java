package com.ProjectParkingSystem.ParkingSystem.Repository;

import com.ProjectParkingSystem.ParkingSystem.Entity.Observation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.EmptyResultDataAccessException;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
public class ObservationRepositoryTest {

    @Autowired
    private ObservationRepository observationRepository;

    private Observation testObservation;

    @BeforeEach
    public void setUp() {
        // Initialize a sample Observation to use in tests
        testObservation = new Observation();
        testObservation.setLicenseNumber("ABC123");
        testObservation.setStreetName("Main Street");
        testObservation.setDateOfObservation(LocalDate.now());

        observationRepository.save(testObservation);
    }

    @Test
    public void testSaveObservation() {
        Observation newObservation = new Observation();
        newObservation.setLicenseNumber("XYZ789");
        newObservation.setStreetName("Broadway");
        newObservation.setDateOfObservation(LocalDate.now().minusDays(1));

        Observation savedObservation = observationRepository.save(newObservation);

        assertNotNull(savedObservation.getId());
        assertEquals("XYZ789", savedObservation.getLicenseNumber());
    }

    @Test
    public void testFindById() {
        Optional<Observation> foundObservation = observationRepository.findById(testObservation.getId());

        assertTrue(foundObservation.isPresent());
        assertEquals(testObservation.getLicenseNumber(), foundObservation.get().getLicenseNumber());
    }

    @Test
    public void testDeleteById() {
        observationRepository.deleteById(testObservation.getId());

        Optional<Observation> deletedObservation = observationRepository.findById(testObservation.getId());
        assertFalse(deletedObservation.isPresent());
    }

    @Test
    public void testDeleteById_NotFound() {
        Long nonExistentId = 999L;

        // Verify that the ID does not exist
        boolean exists = observationRepository.existsById(nonExistentId);
        assertFalse(exists, "Expected no Observation with ID: " + nonExistentId);

    }
}
