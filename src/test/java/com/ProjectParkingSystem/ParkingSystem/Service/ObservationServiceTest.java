package com.ProjectParkingSystem.ParkingSystem.Service;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

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

/*    @Test
    public void testDeleteObservationById() {
        Long existingId = 1L; // replace with a real existing ID or use a mock repository
        observationService.deleteObservationById(existingId);

        Long nonExistingId = 999L; // replace with an ID that doesn't exist
        assertThrows(ResponseStatusException.class, () -> observationService.deleteObservationById(nonExistingId));
    }*/

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
    @Test
    public void testDeleteObservationById_ExistingObservation() {
        Long id = 1L;
        when(observationRepository.existsById(id)).thenReturn(true);

        observationService.deleteObservationById(id);

        verify(observationRepository, times(1)).deleteById(id);
    }

    @Test
    public void testDeleteObservationById_NonExistingObservation() {
        Long id = 2L;
        when(observationRepository.existsById(id)).thenReturn(false);

        assertThrows(ResponseStatusException.class, () -> {
            observationService.deleteObservationById(id);
        });
    }

}



