package com.ProjectParkingSystem.ParkingSystem.Repository;

import com.ProjectParkingSystem.ParkingSystem.Entity.Car;
import com.ProjectParkingSystem.ParkingSystem.Repository.CarRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class CarRepositoryTest {

    @Autowired
    private CarRepository carRepository;

    @Test
    @DisplayName("Test saving a car")
    @Rollback(false)
    public void givenCar_whenSave_thenReturnSavedCar() {
        // Arrange
        Car car = new Car();
        car.setLicensePlate("XYZ789");
        car.setOwnerName("Jane Doe");

        // Act
        Car savedCar = carRepository.save(car);

        // Assert
        assertThat(savedCar).isNotNull();
        assertThat(savedCar.getId()).isNotNull();
        assertThat(savedCar.getLicensePlate()).isEqualTo("XYZ789");
    }

    @Test
    @DisplayName("Test finding a car by ID")
    public void givenCarId_whenFindById_thenReturnCar() {
        // Arrange
        Car car = new Car();
        car.setLicensePlate("LMN123");
        car.setOwnerName("Jack Sparrow");
        car = carRepository.save(car);

        // Act
        Optional<Car> foundCar = carRepository.findById(car.getId());

        // Assert
        assertThat(foundCar).isPresent();
        assertThat(foundCar.get().getLicensePlate()).isEqualTo("LMN123");
    }

    @Test
    @DisplayName("Test deleting a car")
    public void givenCar_whenDelete_thenCarIsDeleted() {
        // Arrange
        Car car = new Car();
        car.setLicensePlate("JKL456");
        car.setOwnerName("Clark Kent");
        car = carRepository.save(car);

        // Act
        carRepository.deleteById(car.getId());
        Optional<Car> deletedCar = carRepository.findById(car.getId());

        // Assert
        assertThat(deletedCar).isNotPresent();
    }
}