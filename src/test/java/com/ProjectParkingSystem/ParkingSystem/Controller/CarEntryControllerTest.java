package com.ProjectParkingSystem.ParkingSystem.Controller;


import com.ProjectParkingSystem.ParkingSystem.Entity.CarEntry;
import com.ProjectParkingSystem.ParkingSystem.Service.CarEntryService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;


import static org.hamcrest.Matchers.containsString;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CarEntryController.class)
public class CarEntryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CarEntryService carEntryService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void givenValidCarEntry_whenEnterParking_thenReturnSavedEntry() throws Exception {
        // Given
        CarEntry carEntry = new CarEntry();
        carEntry.setLicenseNumber("ABC123");
        carEntry.setStreetName("Java");
        carEntry.setStartTime(LocalDateTime.now());

        given(carEntryService.addCarEntry(Mockito.any(CarEntry.class))).willReturn(carEntry);

        // When & Then
        mockMvc.perform(post("/api/parking/enter")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(carEntry)))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(carEntry)));
    }

    @Test
    public void givenValidLicenseNumber_whenExitParking_thenReturnTotalFee() throws Exception {
        // Given
        String licenseNumber = "ABC123";
        CarEntry carEntry = new CarEntry();
        carEntry.setLicenseNumber(licenseNumber);
        carEntry.setStreetName("Java");
        carEntry.setStartTime(LocalDateTime.now().minusMinutes(30));
        carEntry.setEndTime(LocalDateTime.now());
        carEntry.setTotalFee(450L); // Assuming 15 cents per minute for "Java"

        given(carEntryService.endParkingSession(licenseNumber)).willReturn(carEntry);

        // When & Then
        mockMvc.perform(post("/api/parking/exit2")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"licenseNumber\":\"" + licenseNumber + "\"}"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Total fee: 450 cents")));
    }

    @Test
    public void givenNoActiveSession_whenExitParking_thenReturnErrorMessage() throws Exception {
        // Given
        String licenseNumber = "Ts01FC2001";

        given(carEntryService.endParkingSession(licenseNumber)).willThrow(new RuntimeException("Active parking session not found for license: " + licenseNumber));

        // When & Then
        mockMvc.perform(post("/api/parking/exit2")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"licenseNumber\":\"" + licenseNumber + "\"}"))
                .andExpect(status().isInternalServerError())
                .andExpect(content().string(containsString("Active parking session not found for license: " + licenseNumber)));
    }
}

