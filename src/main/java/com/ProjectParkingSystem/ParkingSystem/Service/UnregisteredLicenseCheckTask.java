package com.ProjectParkingSystem.ParkingSystem.Service;

import com.ProjectParkingSystem.ParkingSystem.Entity.CarRegistration;
import com.ProjectParkingSystem.ParkingSystem.Entity.Observation;
import com.ProjectParkingSystem.ParkingSystem.Entity.UnregisteredReportEntry;
import com.ProjectParkingSystem.ParkingSystem.Repository.UnregisteredReportEntryRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UnregisteredLicenseCheckTask {

    private static final Logger logger = LoggerFactory.getLogger(UnregisteredLicenseCheckTask.class);

    @Autowired
    private ObservationService observationService;

    @Autowired
    private CarRegistrationService carRegistrationService;

    @Autowired
    private UnregisteredReportEntryRepository reportEntryRepository;

    /**
     * Scheduled task to run daily at midnight to identify unregistered plates,
     * generate report, and send fine notifications.
     */
    @Scheduled(cron = "0 * * * * ?") // Runs daily at midnight
    public void identifyUnregisteredPlates() {
        List<Observation> observations = observationService.getAllObservations();
        List<UnregisteredReportEntry> reportEntries = new ArrayList<>();

        for (Observation observation : observations) {
            if (!carRegistrationService.isCarRegistered(observation.getLicenseNumber())) {
                // Add to report for unregistered plates
                reportEntries.add(new UnregisteredReportEntry(
                        observation.getId(),
                        observation.getLicenseNumber(),
                        observation.getStreetName(),
                        observation.getDateOfObservation()
                ));

                // Send fine notification
                sendFineNotification(observation.getLicenseNumber());
            }
        }


        // Generate report with all unregistered observations
        generateReport(reportEntries);
    }

    /**
     * Simulates sending a fine notification by printing to console.
     * In a real system, this would involve sending a letter or email.
     */
/*    private void sendFineNotification(String licenseNumber) {
        CarRegistration car = carRegistrationService.getCarRegistrationByLicense(licenseNumber);
        if (car != null) {
            System.out.println("Sending fine notification to " + car.getOwnerName() +
                    ", Address: " + car.getAddress() +
                    " for license number: " + licenseNumber);
        } else {
            System.out.println("No address found for license number: " + licenseNumber);
        }
    }*/
    public void sendFineNotification(String licenseNumber) {
        CarRegistration car = carRegistrationService.getCarRegistrationByLicense(licenseNumber);
        if (car != null) {
            // Using parameterized logging
            logger.info("Sending fine notification to {}, Address: {} for license number: {}",
                    car.getOwnerName(), car.getAddress(), licenseNumber);
        } else {
            logger.warn("No address found for license number: {}", licenseNumber);
        }
    }

    /**
     * Generates a report of unregistered plates.
     * In a real application, this might save to a file or send to an external system.
     */
/*    private void generateReport(List<UnregisteredReportEntry> reportEntries) {
        System.out.println("Generating report for unregistered plates...");
        reportEntries.forEach(entry -> {
            System.out.println("License: " + entry.getLicenseNumber() +
                    ", Date: " + entry.getObservationDate() +
                    ", Street: " + entry.getStreetName());
        });
    }*/

    /**
     *  This method generate report of unregistered plates
     *  The data is stored to un-register entry table
     *
     * */
    public void generateReport(List<UnregisteredReportEntry> reportEntries) {
        logger.info("Saving report for unregistered plates to the database...");

        // Save all report entries to the database
        reportEntryRepository.saveAll(reportEntries);

        logger.info("Report saved with {} entries.", reportEntries.size());
    }
}