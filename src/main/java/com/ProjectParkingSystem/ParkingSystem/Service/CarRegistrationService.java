package com.ProjectParkingSystem.ParkingSystem.Service;

import com.ProjectParkingSystem.ParkingSystem.Entity.CarRegistration;
import org.springframework.stereotype.Service;
import java.util.HashMap;
import java.util.Map;

@Service
public class CarRegistrationService {
    private final Map<String, CarRegistration> registeredCars = new HashMap<>();

    public boolean isCarRegistered(String licenseNumber) {
        return registeredCars.containsKey(licenseNumber);
    }

    public CarRegistration getCarRegistrationByLicense(String licenseNumber) {
        return registeredCars.get(licenseNumber);
    }

/*    public void registerCar(CarRegistration carRegistration) {
        registeredCars.put(carRegistration.getLicenseNumber(), carRegistration);
    }*/
}