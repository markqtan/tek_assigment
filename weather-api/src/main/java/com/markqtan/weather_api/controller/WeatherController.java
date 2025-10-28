package com.markqtan.weather_api.controller;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpClientErrorException;

import com.markqtan.weather_api.model.Weather;
import com.markqtan.weather_api.service.WeatherService;

import jakarta.websocket.server.PathParam;
import lombok.RequiredArgsConstructor;

import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.Cache.ValueWrapper;
import org.springframework.cache.support.SimpleValueWrapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;


@RestController
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:5173")
public class WeatherController {
    private final WeatherService weatherService;
    private final CacheManager cacheManager;

    @GetMapping("/weather/{zip}")
    public ResponseEntity<Weather> getMethodName(@PathVariable(name = "zip") String zip) {
        try {
            Weather weather = null;
            Cache cache = cacheManager.getCache("weather");
            System.out.println("cache:"+cache);
            if(cache != null && cache.get(zip) != null) {
                ValueWrapper wrapper = cache.get(zip);
                weather = (Weather) wrapper.get();
                weather.setCached(true);
            }
            if(weather == null) {
                weather = weatherService.getWeatherByZip(zip);
            }
            return new ResponseEntity<>(weather, HttpStatus.OK);
        }catch(HttpClientErrorException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST); 
        }
    }
    
}
