package com.ProjectParkingSystem.ParkingSystem.Service;

import com.ProjectParkingSystem.ParkingSystem.Entity.Car;
import com.ProjectParkingSystem.ParkingSystem.Repository.CarRepository;
import com.ProjectParkingSystem.ParkingSystem.Service.CarService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CarServiceTest {

    @Mock
    private CarRepository carRepository;

    @InjectMocks
    private CarService carService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void givenCar_whenRegisterCar_thenReturnSavedCar() {
        // Arrange
        Car car = new Car();
        car.setLicensePlate("ABC123");
        car.setOwnerName("John Doe");

        when(carRepository.save(car)).thenReturn(car);

        // Act
        Car savedCar = carService.registerCar(car);

        // Assert
        assertThat(savedCar).isNotNull();
        assertThat(savedCar.getLicensePlate()).isEqualTo("ABC123");
        verify(carRepository, times(1)).save(car);
    }

    @Test
    public void givenCarId_whenDeregisterCar_thenCarIsDeleted() {
        // Arrange
        Long carId = 1L;
        doNothing().when(carRepository).deleteById(carId);

        // Act
        carService.deregisterCar(carId);

        // Assert
        verify(carRepository, times(1)).deleteById(carId);
    }

    @Test
    public void givenNonExistentCarId_whenDeregisterCar_thenThrowException() {
        // Arrange
        Long nonExistentCarId = 999L;
        doThrow(new RuntimeException("Car not found")).when(carRepository).deleteById(nonExistentCarId);

        // Act & Assert
        assertThrows(RuntimeException.class, () -> carService.deregisterCar(nonExistentCarId));
    }
}

