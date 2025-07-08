package com.equation.service;

import com.equation.dto.*;
import com.equation.exception.EquationNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class EquationServiceTest {
    
    @Autowired
    private EquationService equationService;
    
    @BeforeEach
    void setUp() {
        equationService.clearAllEquations();
    }
    
    @Test
    void testStoreEquation() {
        StoreEquationRequest request = new StoreEquationRequest("x + y");
        StoreEquationResponse response = equationService.storeEquation(request);
        
        assertNotNull(response);
        assertEquals("Equation stored successfully", response.getMessage());
        assertNotNull(response.getEquationId());
    }
    
    @Test
    void testGetAllEquations() {
        // Store some equations first
        equationService.storeEquation(new StoreEquationRequest("x + y"));
        equationService.storeEquation(new StoreEquationRequest("a * b"));
        
        EquationListResponse response = equationService.getAllEquations();
        
        assertNotNull(response);
        assertEquals(2, response.getEquations().size());
    }
    
    @Test
    void testEvaluateEquation() {
        // Store an equation first
        StoreEquationResponse storeResponse = equationService.storeEquation(
            new StoreEquationRequest("3x + 2y - z"));
        
        // Evaluate it
        EvaluateEquationRequest evalRequest = new EvaluateEquationRequest(
            Map.of("x", 2.0, "y", 3.0, "z", 1.0));
        
        EvaluateEquationResponse evalResponse = equationService.evaluateEquation(
            storeResponse.getEquationId(), evalRequest);
        
        assertNotNull(evalResponse);
        assertEquals(storeResponse.getEquationId(), evalResponse.getEquationId());
        assertEquals(11.0, evalResponse.getResult(), 0.001);
    }
    
    @Test
    void testEvaluateNonExistentEquation() {
        EvaluateEquationRequest request = new EvaluateEquationRequest(
            Map.of("x", 2.0));
        
        assertThrows(EquationNotFoundException.class, () -> {
            equationService.evaluateEquation("999", request);
        });
    }
    
    @Test
    void testGetEquationById() {
        StoreEquationResponse storeResponse = equationService.storeEquation(
            new StoreEquationRequest("x + y"));
        
        var equation = equationService.getEquationById(storeResponse.getEquationId());
        
        assertNotNull(equation);
        assertEquals("x + y", equation.getOriginalEquation());
    }
    
    @Test
    void testGetNonExistentEquationById() {
        assertThrows(EquationNotFoundException.class, () -> {
            equationService.getEquationById("999");
        });
    }
}