package com.ProjectParkingSystem.ParkingSystem.Repository;

import com.ProjectParkingSystem.ParkingSystem.Entity.CarEntry;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;


@DataJpaTest // Use @DataJpaTest for JPA repositories
public class CarEntryRepositoryTest {

    @Autowired
    private CarEntryRepository carEntryRepository;

    @BeforeEach
    public void setUp() {
        // Optional: You can set up any necessary data or configurations before each test
    }

    @Test
    public void givenCarEntry_whenSave_thenRetrieveSuccessfully() {
        // Given
        CarEntry carEntry = new CarEntry();
        carEntry.setLicenseNumber("ABC123");
        carEntry.setStreetName("Java");
        carEntry.setStartTime(LocalDateTime.now());

        // When
        CarEntry savedEntry = carEntryRepository.save(carEntry);

        // Then
        assertThat(savedEntry).isNotNull();
        assertThat(savedEntry.getId()).isNotNull();
        assertThat(savedEntry.getLicenseNumber()).isEqualTo("ABC123");
    }

    @Test
    public void givenLicenseNumber_whenFindByLicenseNumberAndEndTimeIsNull_thenReturnCarEntry() {
        // Given
        CarEntry carEntry = new CarEntry();
        carEntry.setLicenseNumber("XYZ789");
        carEntry.setStreetName("Java");
        carEntry.setStartTime(LocalDateTime.now());
        carEntryRepository.save(carEntry);

        // When
        Optional<CarEntry> foundEntry = carEntryRepository.findByLicenseNumberAndEndTimeIsNull("XYZ789");

        // Then
        assertThat(foundEntry).isPresent();
        assertThat(foundEntry.get().getLicenseNumber()).isEqualTo("XYZ789");
    }

    @Test
    public void givenNoActiveSession_whenFindByLicenseNumberAndEndTimeIsNull_thenReturnEmpty() {
        // Given
        CarEntry carEntry = new CarEntry();
        carEntry.setLicenseNumber("ABC123");
        carEntry.setStreetName("Java");
        carEntry.setStartTime(LocalDateTime.now());
        carEntry.setEndTime(LocalDateTime.now()); // Marking as ended
        carEntryRepository.save(carEntry);

        // When
        Optional<CarEntry> foundEntry = carEntryRepository.findByLicenseNumberAndEndTimeIsNull("ABC123");

        // Then
        assertThat(foundEntry).isNotPresent();
    }
}