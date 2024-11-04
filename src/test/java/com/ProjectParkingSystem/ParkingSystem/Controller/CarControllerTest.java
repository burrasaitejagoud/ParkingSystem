package com.ProjectParkingSystem.ParkingSystem.Controller;

import com.ProjectParkingSystem.ParkingSystem.Entity.Car;
import com.ProjectParkingSystem.ParkingSystem.Service.CarService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

@WebMvcTest(CarController.class)
public class CarControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CarService carService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void givenValidCar_whenRegisterCar_thenReturnsCreatedCar() throws Exception {
        // Arrange
        Car car = new Car(null, "ABC123", "John Doe");
        Car savedCar = new Car(1L, "ABC123", "John Doe");

        given(carService.registerCar(Mockito.any(Car.class))).willReturn(savedCar);

        // Act & Assert
        mockMvc.perform(post("/api/car")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(car)))
                .andExpect(status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(1L))
                .andExpect(MockMvcResultMatchers.jsonPath("$.licensePlate").value("ABC123"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.ownerName").value("John Doe"));
    }

    @Test
    public void givenValidId_whenDeregisterCar_thenReturnsSuccessMessage() throws Exception {
        // Arrange
        Long carId = 1L;
        doNothing().when(carService).deregisterCar(carId);

        // Act & Assert
        mockMvc.perform(delete("/api/car/{id}", carId))
                .andExpect(status().isOk())
                .andExpect(content().string("Car deregistered Successfully !"));
    }

    @Test
    public void givenInvalidId_whenDeregisterCar_thenReturnsNotFound() throws Exception {
        // Arrange
        Long invalidCarId = 100L;
        Mockito.doThrow(new RuntimeException("Car not found")).when(carService).deregisterCar(invalidCarId);

        // Act & Assert
        mockMvc.perform(delete("/api/car/{id}", invalidCarId))
                .andExpect(status().isInternalServerError())
                .andExpect(content().string("Car not found"));
    }
}
