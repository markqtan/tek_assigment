package com.markqtan.weather_api.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.markqtan.weather_api.model.Weather;
import com.markqtan.weather_api.model.WeatherResponse;
import com.markqtan.weather_api.util.Constants;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class WeatherService {
    @Value("${weather.service.url}")
    private String url;

    @Value("${weather.service.key}")
    private String apiKey;

    private final RestTemplate restTemplate = new RestTemplate();
    private final CacheManager cacheManager;

    @Cacheable(Constants.WEATHER)
    public Weather getWeatherByZip(String zip) {
        WeatherResponse res = restTemplate.getForObject(url + zip + "?key=" + apiKey, WeatherResponse.class);
        System.out.println("api-call:" + res);
        if (res == null || res.getDays() == null || res.getDays().isEmpty()) {
            return null;
        }

        return res.getDays().get(0);
    }

    public boolean isCached(String zip) {
        Cache cache = cacheManager.getCache(Constants.WEATHER);
        System.out.println("cache:" + cache);
        return (cache != null && cache.get(zip) != null);
    }
}
