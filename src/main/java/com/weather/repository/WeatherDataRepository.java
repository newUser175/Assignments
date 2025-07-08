package com.weather.repository;

import com.weather.model.WeatherData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface WeatherDataRepository extends JpaRepository<WeatherData, Long> {
    
    @Query("SELECT w FROM WeatherData w WHERE w.pincode = ?1 ORDER BY w.timestamp DESC")
    List<WeatherData> findByPincodeOrderByTimestampDesc(String pincode);
    
    @Query("SELECT w FROM WeatherData w WHERE w.pincode = ?1 ORDER BY w.timestamp DESC LIMIT 1")
    Optional<WeatherData> findLatestByPincode(String pincode);
}