package com.ProjectParkingSystem.ParkingSystem.CircuitBreaker;

import com.ProjectParkingSystem.ParkingSystem.Repository.CarEntryRepository;
import com.ProjectParkingSystem.ParkingSystem.Repository.CarRepository;
import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.dao.DataAccessResourceFailureException;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(properties = {
        "spring.datasource.url=jdbc:h2:mem:circuitbreakertest;DB_CLOSE_DELAY=-1",
        "spring.datasource.driver-class-name=org.h2.Driver",
        "spring.datasource.username=sa",
        "spring.datasource.password=",
        "spring.jpa.hibernate.ddl-auto=create-drop"
})
@AutoConfigureMockMvc
class CircuitBreakerIntegrationTest {

    private static final int MINIMUM_NUMBER_OF_CALLS = 5;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private CircuitBreakerRegistry circuitBreakerRegistry;

    @MockBean
    private CarRepository carRepository;

    @MockBean
    private CarEntryRepository carEntryRepository;

    @BeforeEach
    void resetCircuitBreakers() {
        circuitBreakerRegistry.getAllCircuitBreakers().forEach(CircuitBreaker::reset);
    }

    @Test
    void givenRepeatedDatabaseFailures_whenCallingApi_thenCircuitOpensAndReturns503() throws Exception {
        willThrow(new DataAccessResourceFailureException("Database down"))
                .given(carRepository).deleteById(anyLong());

        for (int i = 0; i < MINIMUM_NUMBER_OF_CALLS; i++) {
            mockMvc.perform(delete("/api/car/1")).andExpect(status().isInternalServerError());
        }

        assertEquals(CircuitBreaker.State.OPEN, circuitBreakerRegistry.circuitBreaker("carService").getState());

        mockMvc.perform(delete("/api/car/1")).andExpect(status().isServiceUnavailable());
        verify(carRepository, times(MINIMUM_NUMBER_OF_CALLS)).deleteById(anyLong());
    }

    @Test
    void givenBusinessErrors_whenCallingApi_thenCircuitStaysClosed() throws Exception {
        given(carEntryRepository.findByLicenseNumberAndEndTimeIsNull(anyString())).willReturn(Optional.empty());

        for (int i = 0; i < MINIMUM_NUMBER_OF_CALLS * 2; i++) {
            mockMvc.perform(post("/api/parking/exit").param("licenseNumber", "UNKNOWN"))
                    .andExpect(status().isInternalServerError());
        }

        assertEquals(CircuitBreaker.State.CLOSED, circuitBreakerRegistry.circuitBreaker("carEntryService").getState());
    }

    @Test
    void givenOpenCarServiceCircuit_whenCallingOtherApis_thenTheyAreUnaffected() throws Exception {
        circuitBreakerRegistry.circuitBreaker("carService").transitionToOpenState();

        mockMvc.perform(delete("/api/car/1")).andExpect(status().isServiceUnavailable());
        mockMvc.perform(delete("/api/parking/1")).andExpect(status().isInternalServerError());
        assertEquals(CircuitBreaker.State.CLOSED, circuitBreakerRegistry.circuitBreaker("carEntryService").getState());
    }
}
