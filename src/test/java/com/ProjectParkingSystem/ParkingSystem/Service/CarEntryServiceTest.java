package com.ProjectParkingSystem.ParkingSystem.Service;

import com.ProjectParkingSystem.ParkingSystem.Entity.CarEntry;
import com.ProjectParkingSystem.ParkingSystem.Exception.CarEntryNotFoundException;
import com.ProjectParkingSystem.ParkingSystem.Repository.CarEntryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

public class CarEntryServiceTest {

    @Mock
    private CarEntryRepository carEntryRepository;

    @InjectMocks
    private CarEntryService carEntryService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void givenNewCarEntry_whenAddCarEntry_thenReturnSavedEntry() {
        // Given
        CarEntry carEntry = new CarEntry();
        carEntry.setLicenseNumber("ABC123");
        carEntry.setStreetName("Java");
        carEntry.setStartTime(LocalDateTime.now());

        given(carEntryRepository.save(carEntry)).willReturn(carEntry);

        // When
        CarEntry savedEntry = carEntryService.addCarEntry(carEntry);

        // Then
        assertThat(savedEntry).isEqualTo(carEntry);
        verify(carEntryRepository, times(1)).save(carEntry);
    }

    @Test
    public void givenActiveSession_whenEndParkingSession_thenCalculateFeeAndReturnEntry() {
        // Given
        CarEntry carEntry = new CarEntry();
        carEntry.setId(1L);
        carEntry.setLicenseNumber("XYZ789");
        carEntry.setStreetName("Java");
        carEntry.setStartTime(LocalDateTime.now().minusMinutes(60)); // 1 hour ago

        given(carEntryRepository.findByLicenseNumberAndEndTimeIsNull("XYZ789"))
                .willReturn(Optional.of(carEntry));

        // When
        CarEntry endedEntry = carEntryService.endParkingSession("XYZ789");

        // Then
        assertThat(endedEntry.getEndTime()).isNotNull();
        assertThat(endedEntry.getTotalFee()).isGreaterThanOrEqualTo(0);
        verify(carEntryRepository, times(1)).save(endedEntry);
    }

    @Test
    public void givenNoActiveSession_whenEndParkingSession_thenThrowException() {
        // Given
        String licenseNumber = "XYZ789";
        given(carEntryRepository.findByLicenseNumberAndEndTimeIsNull(licenseNumber))
                .willReturn(Optional.empty());

        // When & Then
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            carEntryService.endParkingSession(licenseNumber);
        });

        assertThat(exception.getMessage()).isEqualTo("Active parking session not found for license: " + licenseNumber);
    }

    @Test
    public void givenCarEntryWithDuration_whenCalculateTotalFee_thenReturnCorrectFee() {
        // Given
        CarEntry carEntry = new CarEntry();
        carEntry.setId(1L);
        carEntry.setLicenseNumber("DEF456");
        carEntry.setStreetName("Java");
        carEntry.setStartTime(LocalDateTime.now().minusMinutes(30)); // 30 minutes ago
        carEntry.setEndTime(LocalDateTime.now());

        // Calculate expected fee: 30 minutes * 15 cents/min = 450 cents
        long expectedFee = 30 * 15;
        long actualFee = carEntry.calculateParkingFee();

        // Then
        assertThat(actualFee).isEqualTo(expectedFee);
    }
    @Test
    public void testEndParkingSessionNotFound() {
        when(carEntryRepository.findByLicenseNumberAndEndTimeIsNull("XYZ789")).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> carEntryService.endParkingSession("XYZ789"));
        assertEquals("Active parking session not found for license: XYZ789", exception.getMessage());
    }
/*    @Test
    public void testEndParkingSessionDuringFreeHours() {
        CarEntry carEntry = new CarEntry();
        carEntry.setId(1L); // Set the primary key to avoid NullPointerException
        carEntry.setLicenseNumber("ABC123");
        carEntry.setStartTime(LocalDateTime.of(2024, 11, 5, 22, 0)); // Start at 22:00

        when(carEntryRepository.findByLicenseNumberAndEndTimeIsNull("ABC123"))
                .thenReturn(Optional.of(carEntry));
        when(carEntryRepository.save(any(CarEntry.class)))
                .thenReturn(carEntry);

        CarEntry endedEntry = carEntryService.endParkingSession("ABC123");

        assertEquals(0L, endedEntry.getTotalFee()); // Expect free parking
        assertNotNull(endedEntry.getEndTime());
        verify(carEntryRepository, times(1)).save(any(CarEntry.class));
    }*/

    @Test
    public void testEndParkingSessionOnSunday() {
        // Test case for free parking on Sunday
        CarEntry carEntry = new CarEntry();

        carEntry.setLicenseNumber("ABC123");
        carEntry.setStartTime(LocalDateTime.of(2024, 11, 3, 10, 0)); // Start on Sunday at 10:00
        when(carEntryRepository.findByLicenseNumberAndEndTimeIsNull("ABC123")).thenReturn(Optional.of(carEntry));
        when(carEntryRepository.save(any(CarEntry.class))).thenReturn(carEntry);

        CarEntry endedEntry = carEntryService.endParkingSession("ABC123");

        assertEquals(0L, endedEntry.getTotalFee()); // Expect free parking
        assertNotNull(endedEntry.getEndTime());
        verify(carEntryRepository, times(1)).save(any(CarEntry.class));
    }

    @Test
    public void testDeleteCarEntryById_EntryExists() {
        Long id = 1L;
        when(carEntryRepository.existsById(id)).thenReturn(true);
        doNothing().when(carEntryRepository).deleteById(id);

        assertDoesNotThrow(() -> carEntryService.deleteCarEntryById(id));
        verify(carEntryRepository, times(1)).deleteById(id);
    }

    @Test
    public void testDeleteCarEntryById_EntryNotFound() {
        Long id = 1L;
        when(carEntryRepository.existsById(id)).thenReturn(false);

        assertThrows(CarEntryNotFoundException.class, () -> carEntryService.deleteCarEntryById(id));
        verify(carEntryRepository, never()).deleteById(id);
    }
}
