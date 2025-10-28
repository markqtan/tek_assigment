package com.markqtan.weather_api.model;

import lombok.Data;

@Data
public class Weather {
    private boolean cached;
    private double temp;
    private double tempmin;
    private double tempmax;
}

