package com.ProjectParkingSystem.ParkingSystem.Service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.ProjectParkingSystem.ParkingSystem.Entity.Observation;
import com.ProjectParkingSystem.ParkingSystem.Repository.ObservationRepository;
import com.ProjectParkingSystem.ParkingSystem.Service.ObservationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class ObservationServiceTest {

    @InjectMocks
    private ObservationService observationService;

    @Mock
    private ObservationRepository observationRepository;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testSaveObservations() {
        // Given
        Observation observation = new Observation(123L,"DEF456", "Spring", LocalDate.now());
        List<Observation> observations = Collections.singletonList(observation);

        // When
        ResponseEntity<String> response = observationService.saveObservations(observations);

        // Then
        verify(observationRepository, times(1)).saveAll(observations);
        assertEquals("Observations uploaded successfully.", response.getBody());
    }

    @Test
    public void testGetAllObservations() {
        // Given
        Observation observation1 = new Observation(123L,"DEF456", "Spring", LocalDate.now());
        Observation observation2 = new Observation(123L,"XYZ789", "Java", LocalDate.now());
        List<Observation> observations = List.of(observation1, observation2);

        when(observationRepository.findAll()).thenReturn(observations);

        // When
        List<Observation> result = observationService.getAllObservations();

        // Then
        assertThat(result).hasSize(2);
        assertThat(result).contains(observation1, observation2);
    }

    @Test
    public void testDeleteObservationById() {
        Long nonExistentId = 999L; // This ID should correspond to an actual record in the database
        assertThrows(ResponseStatusException.class, () -> {
            observationService.deleteObservationById(nonExistentId);
        });
    }

/*    @Test
    public void testSaveObservations_withEmptyList() {
        // Given
        List<Observation> observations = Collections.emptyList();

        // When
        ResponseEntity<String> response = observationService.saveObservations(observations);

        // Then
        verify(observationRepository, times(0)).saveAll(any());
        assertEquals("Observations uploaded successfully.", response.getBody());
    }*/
    @Test
    public void testSaveObservations_withEmptyList() {
    List<Observation> emptyList = new ArrayList<>();

    observationService.saveObservations(emptyList);

    verify(observationRepository, never()).saveAll(emptyList);
}
/*    @Test
    public void testDeleteObservationById_ExistingObservation() {
        Long observationId = 1L; // Check if this ID actually exists
        observationService.deleteObservationById(observationId);
        Optional<Observation> deletedObservation = observationRepository.findById(observationId);
        assertTrue(deletedObservation.isEmpty()); // Verify the observation was deleted
    }*/

    @Test
    public void testDeleteObservationById_NonExistingObservation() {
        Long id = 2L;
        when(observationRepository.existsById(id)).thenReturn(false);

        assertThrows(ResponseStatusException.class, () -> {
            observationService.deleteObservationById(id);
        });
    }

}



