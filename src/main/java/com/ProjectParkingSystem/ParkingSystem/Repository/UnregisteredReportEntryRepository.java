package com.ProjectParkingSystem.ParkingSystem.Repository;

import com.ProjectParkingSystem.ParkingSystem.Entity.UnregisteredReportEntry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UnregisteredReportEntryRepository extends JpaRepository<UnregisteredReportEntry, Long> {
}