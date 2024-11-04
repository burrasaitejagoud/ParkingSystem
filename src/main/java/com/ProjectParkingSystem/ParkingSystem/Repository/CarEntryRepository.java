package com.ProjectParkingSystem.ParkingSystem.Repository;

import com.ProjectParkingSystem.ParkingSystem.Entity.CarEntry;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CarEntryRepository extends JpaRepository<CarEntry , Long> {

    Optional<CarEntry> findByLicenseNumberAndEndTimeIsNull(String licenseNumber);

}
