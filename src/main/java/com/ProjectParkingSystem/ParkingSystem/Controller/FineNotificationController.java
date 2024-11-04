package com.ProjectParkingSystem.ParkingSystem.Controller;


import com.ProjectParkingSystem.ParkingSystem.Service.UnregisteredLicenseCheckTask;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/finenotification")
public class FineNotificationController {

    @Autowired
    private UnregisteredLicenseCheckTask unregisteredLicenseCheckTask;  // Assume this service contains sendFineNotification

    @PostMapping("/send")
    public ResponseEntity<String> sendFineNotification(@RequestParam String licenseNumber) {
        unregisteredLicenseCheckTask.sendFineNotification(licenseNumber);
        return ResponseEntity.ok("Fine notification sent for license number: " + licenseNumber);
    }
}