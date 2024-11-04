package com.ProjectParkingSystem.ParkingSystem.Constants;


import lombok.Data;

import java.util.Map;

@Data
public class RateMap {

    public static final Map<String, Integer> RATE_MAP = Map.of(
            "Java", 15,
            "Jakarta", 13,
            "Spring", 10,
            "Azure", 10
    );
}


