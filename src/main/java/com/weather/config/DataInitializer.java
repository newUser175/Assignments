package com.weather.config;

import com.weather.model.PincodeLocation;
import com.weather.repository.PincodeLocationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {
    
    @Autowired
    private PincodeLocationRepository pincodeLocationRepository;
    
    @Override
    public void run(String... args) throws Exception {
        // Initialize with some sample pincode data
        if (pincodeLocationRepository.count() == 0) {
            pincodeLocationRepository.save(new PincodeLocation("400001", "Mumbai", "Maharashtra", 19.0760, 72.8777));
            pincodeLocationRepository.save(new PincodeLocation("110001", "Delhi", "Delhi", 28.6139, 77.2090));
            pincodeLocationRepository.save(new PincodeLocation("560001", "Bangalore", "Karnataka", 12.9716, 77.5946));
            pincodeLocationRepository.save(new PincodeLocation("600001", "Chennai", "Tamil Nadu", 13.0827, 80.2707));
            pincodeLocationRepository.save(new PincodeLocation("700001", "Kolkata", "West Bengal", 22.5726, 88.3639));
            pincodeLocationRepository.save(new PincodeLocation("411001", "Pune", "Maharashtra", 18.5204, 73.8567));
            pincodeLocationRepository.save(new PincodeLocation("500001", "Hyderabad", "Telangana", 17.3850, 78.4867));
            pincodeLocationRepository.save(new PincodeLocation("302001", "Jaipur", "Rajasthan", 26.9124, 75.7873));
            pincodeLocationRepository.save(new PincodeLocation("226001", "Lucknow", "Uttar Pradesh", 26.8467, 80.9462));
            pincodeLocationRepository.save(new PincodeLocation("171001", "Shimla", "Himachal Pradesh", 31.1048, 77.1734));
        }
    }
}