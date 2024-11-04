package com.ProjectParkingSystem.ParkingSystem.Service;

import com.ProjectParkingSystem.ParkingSystem.Entity.Car;
import com.ProjectParkingSystem.ParkingSystem.Repository.CarRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
@AllArgsConstructor
public class CarService  {

    @Autowired
    private CarRepository carRepository;


    public Car registerCar(Car car) {

        return carRepository.save(car);
    }


    public void deregisterCar(Long id) {

        carRepository.deleteById(id);
    }
}
