package com.ProjectParkingSystem.ParkingSystem.Controller;


import com.ProjectParkingSystem.ParkingSystem.Entity.Car;
import com.ProjectParkingSystem.ParkingSystem.Service.CarService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping("api/car")
public class CarController {

    @Autowired
    private CarService carService;

    //Register Api
    @PostMapping
    public ResponseEntity<Car> registerCar(@RequestBody Car car){
       Car registeredCar = carService.registerCar(car);
       return new ResponseEntity<>(registeredCar, HttpStatus.CREATED);
    }

    //Deregister Api
    @DeleteMapping("{id}")
    public ResponseEntity<String> deregisterCar(@PathVariable("id") Long id){
        carService.deregisterCar(id);
        return new ResponseEntity<>("Car deregistered Successfully !",HttpStatus.OK);
    }

}
