package com.ProjectParkingSystem.ParkingSystem.Dto;


import lombok.Data;

@Data
public class ExitRequest {
    private String licenseNumber;

    // Getters and Setters
    public String getLicenseNumber() {
        return licenseNumber;
    }

    public void setLicenseNumber(String licenseNumber) {
        this.licenseNumber = licenseNumber;
    }
}