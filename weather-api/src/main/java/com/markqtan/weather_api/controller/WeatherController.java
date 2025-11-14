package com.markqtan.weather_api.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpClientErrorException;

import com.markqtan.weather_api.model.Weather;
import com.markqtan.weather_api.service.WeatherService;

import lombok.RequiredArgsConstructor;


@RestController
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:5173")
public class WeatherController {
    private final WeatherService weatherService;

    @GetMapping("/weather/{zip}")
    public ResponseEntity<Weather> getWeatherByZip(@PathVariable(name = "zip") String zip) {
        try {
            Weather weather = null;
            boolean isCached = weatherService.isCached(zip);
            weather = weatherService.getWeatherByZip(zip);
            weather.setCached(isCached);
            return new ResponseEntity<>(weather, HttpStatus.OK);
        }catch(HttpClientErrorException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST); 
        }
    }
    
}
