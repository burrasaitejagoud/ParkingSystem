package com.ProjectParkingSystem.ParkingSystem.Repository;


import com.ProjectParkingSystem.ParkingSystem.Entity.Car;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CarRepository extends JpaRepository<Car, Long> {

}