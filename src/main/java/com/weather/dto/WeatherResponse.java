package com.weather.dto;

import java.time.LocalDateTime;

public class WeatherResponse {
    private String pincode;
    private String city;
    private String state;
    private Double temperature;
    private String description;
    private Integer humidity;
    private Double windSpeed;
    private LocalDateTime timestamp;
    private String status;
    private String message;
    
    // Constructors
    public WeatherResponse() {}
    
    public WeatherResponse(String pincode, String city, String state, Double temperature, 
                          String description, Integer humidity, Double windSpeed, LocalDateTime timestamp) {
        this.pincode = pincode;
        this.city = city;
        this.state = state;
        this.temperature = temperature;
        this.description = description;
        this.humidity = humidity;
        this.windSpeed = windSpeed;
        this.timestamp = timestamp;
        this.status = "success";
    }
    
    public static WeatherResponse error(String message) {
        WeatherResponse response = new WeatherResponse();
        response.status = "error";
        response.message = message;
        return response;
    }
    
    // Getters and Setters
    public String getPincode() { return pincode; }
    public void setPincode(String pincode) { this.pincode = pincode; }
    
    public String getCity() { return city; }
    public void setCity(String city) { this.city = city; }
    
    public String getState() { return state; }
    public void setState(String state) { this.state = state; }
    
    public Double getTemperature() { return temperature; }
    public void setTemperature(Double temperature) { this.temperature = temperature; }
    
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    
    public Integer getHumidity() { return humidity; }
    public void setHumidity(Integer humidity) { this.humidity = humidity; }
    
    public Double getWindSpeed() { return windSpeed; }
    public void setWindSpeed(Double windSpeed) { this.windSpeed = windSpeed; }
    
    public LocalDateTime getTimestamp() { return timestamp; }
    public void setTimestamp(LocalDateTime timestamp) { this.timestamp = timestamp; }
    
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    
    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
}