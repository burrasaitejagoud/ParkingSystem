package com.ProjectParkingSystem.ParkingSystem.Exception;


public class CarEntryNotFoundException extends RuntimeException {
    public CarEntryNotFoundException(String message) {
        super(message);
    }
}