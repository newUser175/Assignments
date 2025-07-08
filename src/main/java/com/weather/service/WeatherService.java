package com.weather.service;

import com.weather.dto.WeatherResponse;
import com.weather.model.PincodeLocation;
import com.weather.model.WeatherData;
import com.weather.repository.PincodeLocationRepository;
import com.weather.repository.WeatherDataRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Random;

@Service
public class WeatherService {
    
    @Autowired
    private WeatherDataRepository weatherDataRepository;
    
    @Autowired
    private PincodeLocationRepository pincodeLocationRepository;
    
    private final Random random = new Random();
    
    public WeatherResponse getWeatherByPincode(String pincode) {
        try {
            // Validate pincode format
            if (!isValidPincode(pincode)) {
                return WeatherResponse.error("Invalid pincode format. Please enter a 6-digit pincode.");
            }
            
            // Get location details for pincode
            Optional<PincodeLocation> locationOpt = pincodeLocationRepository.findById(pincode);
            if (locationOpt.isEmpty()) {
                return WeatherResponse.error("Pincode not found. Please check the pincode and try again.");
            }
            
            PincodeLocation location = locationOpt.get();
            
            // Check if we have recent weather data (within last hour)
            Optional<WeatherData> recentData = weatherDataRepository.findLatestByPincode(pincode);
            if (recentData.isPresent() && 
                recentData.get().getTimestamp().isAfter(LocalDateTime.now().minusHours(1))) {
                WeatherData data = recentData.get();
                return new WeatherResponse(data.getPincode(), data.getCity(), data.getState(),
                                         data.getTemperature(), data.getDescription(), 
                                         data.getHumidity(), data.getWindSpeed(), data.getTimestamp());
            }
            
            // Generate mock weather data (in real implementation, call external weather API)
            WeatherData weatherData = generateMockWeatherData(location);
            weatherDataRepository.save(weatherData);
            
            return new WeatherResponse(weatherData.getPincode(), weatherData.getCity(), 
                                     weatherData.getState(), weatherData.getTemperature(),
                                     weatherData.getDescription(), weatherData.getHumidity(),
                                     weatherData.getWindSpeed(), weatherData.getTimestamp());
            
        } catch (Exception e) {
            return WeatherResponse.error("Error fetching weather data: " + e.getMessage());
        }
    }
    
    public List<WeatherData> getWeatherHistory(String pincode) {
        return weatherDataRepository.findByPincodeOrderByTimestampDesc(pincode);
    }
    
    private boolean isValidPincode(String pincode) {
        return pincode != null && pincode.matches("\\d{6}");
    }
    
    private WeatherData generateMockWeatherData(PincodeLocation location) {
        // Generate realistic weather data based on location
        double baseTemp = getBaseTemperatureForLocation(location.getCity());
        double temperature = baseTemp + (random.nextGaussian() * 5); // ±5°C variation
        
        String[] descriptions = {"Clear sky", "Few clouds", "Scattered clouds", "Broken clouds", 
                               "Light rain", "Moderate rain", "Thunderstorm", "Mist", "Haze"};
        String description = descriptions[random.nextInt(descriptions.length)];
        
        int humidity = 30 + random.nextInt(50); // 30-80%
        double windSpeed = 2 + (random.nextDouble() * 15); // 2-17 km/h
        
        return new WeatherData(location.getPincode(), location.getCity(), location.getState(),
                              Math.round(temperature * 10.0) / 10.0, description, humidity,
                              Math.round(windSpeed * 10.0) / 10.0);
    }
    
    private double getBaseTemperatureForLocation(String city) {
        // Simple logic to assign base temperatures based on city
        switch (city.toLowerCase()) {
            case "mumbai": case "chennai": case "kolkata": return 28.0;
            case "delhi": case "jaipur": case "lucknow": return 25.0;
            case "bangalore": case "pune": case "hyderabad": return 24.0;
            case "shimla": case "manali": case "dehradun": return 15.0;
            default: return 26.0;
        }
    }
}