package com.ProjectParkingSystem.ParkingSystem.Repository;

import com.ProjectParkingSystem.ParkingSystem.Entity.Observation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ObservationRepository extends JpaRepository<Observation , Long> {
}
