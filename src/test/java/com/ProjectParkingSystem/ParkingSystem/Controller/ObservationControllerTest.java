package com.ProjectParkingSystem.ParkingSystem.Controller;

import com.ProjectParkingSystem.ParkingSystem.Entity.Observation;
import com.ProjectParkingSystem.ParkingSystem.Service.ObservationService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ObservationController.class)
public class ObservationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ObservationService observationService;

    @Autowired
    private ObjectMapper objectMapper;

    private Observation sampleObservation;

    @BeforeEach
    public void setup() {
        sampleObservation = new Observation(123456L,"DEF456", "Spring", LocalDate.now());
    }

    @Test
    public void testUploadObservations() throws Exception {
        // Given
        List<Observation> observations = List.of(sampleObservation);

        when(observationService.saveObservations(anyList())).thenReturn(ResponseEntity.ok("Observation uploaded successfully."));


        // When & Then
        mockMvc.perform(post("/api/observations/upload")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(observations)))
                .andExpect(status().isOk())
                .andExpect(content().string("Observations uploaded successfully."));
    }

    @Test
    public void testUploadSingleObservation() throws Exception {
        // Given
        Observation sampleObservation = new Observation(123L,"DEF456", "Spring", LocalDate.now());
       // doNothing().when(observationService).saveObservations(anyList());
        when(observationService.saveObservations(anyList())).thenReturn(ResponseEntity.ok("Observation uploaded successfully."));


        // When & Then
        mockMvc.perform(post("/api/observations/uploading")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(sampleObservation)))
                .andExpect(status().isOk())
                .andExpect(content().string("Observation uploaded successfully."));
    }

    @Test
    public void testGetAllObservations() throws Exception {
        // Given
        List<Observation> observations = List.of(sampleObservation);
        given(observationService.getAllObservations()).willReturn(observations);

        // When & Then
        mockMvc.perform(get("/api/observations/all"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].licenseNumber").value("DEF456"))
                .andExpect(jsonPath("$[0].streetName").value("Spring"));
    }

    @Test
    public void testDeleteObservationById() throws Exception {
        // Given
        long observationId = 1L;
        doNothing().when(observationService).deleteObservationById(observationId);

        // When & Then
        mockMvc.perform(delete("/api/observations/{id}", observationId))
                .andExpect(status().isOk())
                .andExpect(content().string("Observation deleted successfully."));
    }
}
