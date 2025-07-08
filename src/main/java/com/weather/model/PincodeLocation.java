package com.weather.model;

import jakarta.persistence.*;

@Entity
@Table(name = "pincode_locations")
public class PincodeLocation {
    @Id
    private String pincode;
    
    @Column(nullable = false)
    private String city;
    
    @Column(nullable = false)
    private String state;
    
    @Column(nullable = false)
    private Double latitude;
    
    @Column(nullable = false)
    private Double longitude;
    
    // Constructors
    public PincodeLocation() {}
    
    public PincodeLocation(String pincode, String city, String state, Double latitude, Double longitude) {
        this.pincode = pincode;
        this.city = city;
        this.state = state;
        this.latitude = latitude;
        this.longitude = longitude;
    }
    
    // Getters and Setters
    public String getPincode() { return pincode; }
    public void setPincode(String pincode) { this.pincode = pincode; }
    
    public String getCity() { return city; }
    public void setCity(String city) { this.city = city; }
    
    public String getState() { return state; }
    public void setState(String state) { this.state = state; }
    
    public Double getLatitude() { return latitude; }
    public void setLatitude(Double latitude) { this.latitude = latitude; }
    
    public Double getLongitude() { return longitude; }
    public void setLongitude(Double longitude) { this.longitude = longitude; }
}