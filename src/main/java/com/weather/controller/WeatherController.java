package com.weather.controller;

import com.weather.dto.WeatherResponse;
import com.weather.model.WeatherData;
import com.weather.service.WeatherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/weather")
@CrossOrigin(origins = "*")
public class WeatherController {
    
    @Autowired
    private WeatherService weatherService;
    
    @GetMapping("/{pincode}")
    public ResponseEntity<WeatherResponse> getWeather(@PathVariable String pincode) {
        WeatherResponse response = weatherService.getWeatherByPincode(pincode);
        
        if ("error".equals(response.getStatus())) {
            return ResponseEntity.badRequest().body(response);
        }
        
        return ResponseEntity.ok(response);
    }
    
    @GetMapping("/{pincode}/history")
    public ResponseEntity<List<WeatherData>> getWeatherHistory(@PathVariable String pincode) {
        List<WeatherData> history = weatherService.getWeatherHistory(pincode);
        return ResponseEntity.ok(history);
    }
    
    @GetMapping("/health")
    public ResponseEntity<String> healthCheck() {
        return ResponseEntity.ok("Weather API is running!");
    }
}