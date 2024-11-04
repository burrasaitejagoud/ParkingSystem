package com.ProjectParkingSystem.ParkingSystem.Service;

import com.ProjectParkingSystem.ParkingSystem.Entity.CarRegistration;
import com.ProjectParkingSystem.ParkingSystem.Entity.Observation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import static org.mockito.Mockito.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

class UnregisteredLicenseCheckTaskTest {

    @Mock
    private ObservationService observationService;

    @Mock
    private CarRegistrationService carRegistrationService;

    @InjectMocks
    private UnregisteredLicenseCheckTask unregisteredLicenseCheckTask;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testIdentifyUnregisteredPlates() {
        // Mock data for observations
        Observation observation1 = new Observation(123L,"ABC123", "Main St", LocalDate.now());
        Observation observation2 = new Observation(123L,"XYZ789", "Broadway", LocalDate.now());
        List<Observation> observations = Arrays.asList(observation1, observation2);

        // Mock ObservationService to return observations
        when(observationService.getAllObservations()).thenReturn(observations);

        // Mock CarRegistrationService to show one car is registered, one is not
        when(carRegistrationService.isCarRegistered("ABC123")).thenReturn(true);
        when(carRegistrationService.isCarRegistered("XYZ789")).thenReturn(false);

        // Mock the car registration lookup for the fine notification
        CarRegistration unregisteredCar = new CarRegistration(123L,"XYZ789", "123 Elm St", "John Doe");
        when(carRegistrationService.getCarRegistrationByLicense("XYZ789")).thenReturn(unregisteredCar);

        // Run the scheduled task
        unregisteredLicenseCheckTask.identifyUnregisteredPlates();

        // Verify that a report entry was created only for the unregistered car
        verify(carRegistrationService, times(1)).getCarRegistrationByLicense("XYZ789");
        verify(carRegistrationService, never()).getCarRegistrationByLicense("ABC123");
    }
}
