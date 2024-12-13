package com.ProjectParkingSystem.ParkingSystem.Service;

import com.ProjectParkingSystem.ParkingSystem.Entity.CarRegistration;
import com.ProjectParkingSystem.ParkingSystem.Entity.Observation;
import com.ProjectParkingSystem.ParkingSystem.Entity.UnregisteredReportEntry;
import com.ProjectParkingSystem.ParkingSystem.Repository.UnregisteredReportEntryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.*;

public class UnregisteredLicenseCheckTaskTest {

    @InjectMocks
    private UnregisteredLicenseCheckTask unregisteredLicenseCheckTask;

    @Mock
    private ObservationService observationService;

    @Mock
    private CarRegistrationService carRegistrationService;

    @Mock
    private CarEntryService carEntryService;

    @Mock
    private UnregisteredReportEntryRepository reportEntryRepository;

    @Mock
    private CarRegistration carRegistration;

    private Observation observation;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);

        // Create a mock observation and set the id to a non-null value
        observation = new Observation();
        observation.setId(1L); // Set the ID to a valid value (non-null)
        observation.setLicenseNumber("XYZ123");
        observation.setStreetName("Main Street");
        observation.setDateOfObservation(LocalDateTime.now());
    }

    @Test
    public void testIdentifyUnregisteredPlates() {
        // Mock behavior
        when(observationService.getAllObservations()).thenReturn(Collections.singletonList(observation));
        when(carEntryService.isCarRegistered("XYZ123")).thenReturn(false); // Mocking return value for unregistered car
        when(carRegistrationService.getCarRegistrationByLicense("XYZ123")).thenReturn(null); // No car registration

        // Call the method
        unregisteredLicenseCheckTask.identifyUnregisteredPlates();

        // Verify that the methods were called
        verify(observationService).getAllObservations();
        verify(carEntryService,times(2)).isCarRegistered("XYZ123"); // Check if the method was called once
        verify(reportEntryRepository).saveAll(anyList());  // Verify report saving

        // Optionally, you can verify logging output if necessary
        // For example, verify if the fine notification logging happens correctly
        //verify(logger).warn("Sending fine notification: Car with license number XYZ123 is unregistered.");
    }


    @Test
    public void testSendFineNotificationWhenCarIsUnregistered() {
        // Test when the car is unregistered (car not found in CarEntry table)
        when(carEntryService.isCarRegistered("XYZ123")).thenReturn(false);

        // Capture logger output
        unregisteredLicenseCheckTask.sendFineNotification("XYZ123");

        // Verify that the fine notification was logged for unregistered car
        verify(carEntryService).isCarRegistered("XYZ123");
        // You can check for logger output using a custom appender, if required
    }

    @Test
    public void testSendFineNotificationWhenCarIsRegistered() {
        // Test when the car is registered
        when(carEntryService.isCarRegistered("XYZ123")).thenReturn(true);

        // Call the method
        unregisteredLicenseCheckTask.sendFineNotification("XYZ123");

        // Verify that the method checks registration
        verify(carEntryService).isCarRegistered("XYZ123");
        // Optionally, verify that no fine notification is sent for registered cars
    }

    @Test
    public void testGenerateReport() {
        // Create mock report entry
        UnregisteredReportEntry reportEntry = new UnregisteredReportEntry(1L, "XYZ123", "Main Street", LocalDateTime.now());

        // Call the method to generate report
        unregisteredLicenseCheckTask.generateReport(Collections.singletonList(reportEntry));

        // Verify that the report entries were saved
        verify(reportEntryRepository).saveAll(anyList());
    }
}
