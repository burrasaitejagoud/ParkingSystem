package com.ProjectParkingSystem.ParkingSystem.Service;

import com.ProjectParkingSystem.ParkingSystem.Entity.CarEntry;
import com.ProjectParkingSystem.ParkingSystem.Repository.CarEntryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.Optional;


import static org.assertj.core.api.Assertions.assertThat;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

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
        carEntry.setLicenseNumber("XYZ789");
        carEntry.setStreetName("Java");
        carEntry.setStartTime(LocalDateTime.now().minusMinutes(60)); // 1 hour ago

        given(carEntryRepository.findByLicenseNumberAndEndTimeIsNull("XYZ789"))
                .willReturn(Optional.of(carEntry));

        // When
        CarEntry endedEntry = carEntryService.endParkingSession("XYZ789");

        // Then
        assertThat(endedEntry.getEndTime()).isNotNull();
        assertThat(endedEntry.getTotalFee()).isGreaterThan(0);
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
}
