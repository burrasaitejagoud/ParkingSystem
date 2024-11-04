package com.ProjectParkingSystem.ParkingSystem.Service;

import com.ProjectParkingSystem.ParkingSystem.Entity.CarRegistration;
import com.ProjectParkingSystem.ParkingSystem.Entity.Observation;
import com.ProjectParkingSystem.ParkingSystem.Repository.UnregisteredReportEntryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Collections;

import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UnregisteredLicenseCheckTaskTest {

    @Mock
    private ObservationService observationService;

    @Mock
    private CarRegistrationService carRegistrationService;

    @Mock
    private UnregisteredReportEntryRepository reportEntryRepository;

    @InjectMocks
    private UnregisteredLicenseCheckTask unregisteredLicenseCheckTask;

    private Observation observation;

    @BeforeEach
    public void setUp() {
        // Create a sample observation
        observation = new Observation();
        observation.setId(1L);
        observation.setLicenseNumber("XYZ123");
        observation.setStreetName("Main St");
        observation.setDateOfObservation(LocalDate.now());
    }

    @Test
    public void testIdentifyUnregisteredPlates() {
        // Mock behavior
        when(observationService.getAllObservations()).thenReturn(Collections.singletonList(observation));
        when(carRegistrationService.isCarRegistered("XYZ123")).thenReturn(false);  // Unregistered plate
        when(carRegistrationService.getCarRegistrationByLicense("XYZ123")).thenReturn(null); // Car registration not found

        // Call the method directly (simulate scheduled task)
        unregisteredLicenseCheckTask.identifyUnregisteredPlates();

        // Verify that the methods were called
        verify(observationService).getAllObservations();
        verify(carRegistrationService).isCarRegistered("XYZ123");
        verify(reportEntryRepository).saveAll(anyList());  // Verify the report is saved

        // Optionally, verify that the fine notification was sent
        verify(carRegistrationService).getCarRegistrationByLicense("XYZ123");  // Ensure the method was called
    }

    @Test
    public void testSendFineNotification() {
        // Mock car registration
        CarRegistration car = new CarRegistration();
        car.setOwnerName("John Doe");
        car.setAddress("123 Main St");
        when(carRegistrationService.getCarRegistrationByLicense("XYZ123")).thenReturn(car);

        // Call the method directly
        unregisteredLicenseCheckTask.sendFineNotification("XYZ123");

        // Verify logging or any other side effects
        verify(carRegistrationService).getCarRegistrationByLicense("XYZ123");  // Ensure the method was called
    }

    @Test
    public void testNoFineNotificationIfCarNotFound() {
        // Mock car registration as null (unregistered car)
        when(carRegistrationService.getCarRegistrationByLicense("XYZ123")).thenReturn(null);

        // Call the method directly
        unregisteredLicenseCheckTask.sendFineNotification("XYZ123");

        // Verify logging or any other side effects (logging in this case)
        verify(carRegistrationService).getCarRegistrationByLicense("XYZ123");  // Ensure the method was called
    }
}
