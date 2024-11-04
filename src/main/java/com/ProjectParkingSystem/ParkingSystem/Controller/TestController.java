package com.ProjectParkingSystem.ParkingSystem.Controller;

import com.ProjectParkingSystem.ParkingSystem.Service.UnregisteredLicenseCheckTask;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    @Autowired
    private UnregisteredLicenseCheckTask task;

    @GetMapping("/run-task")
    public String runTask() {
        task.identifyUnregisteredPlates();
        return "Task executed";
    }
}