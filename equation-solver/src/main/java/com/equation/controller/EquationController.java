package com.equation.controller;

import com.equation.dto.*;
import com.equation.service.EquationService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * REST Controller for equation operations
 */
@RestController
@RequestMapping("/api/equations")
@CrossOrigin(origins = "*")
public class EquationController {
    
    @Autowired
    private EquationService equationService;
    
    /**
     * Store an algebraic equation
     */
    @PostMapping("/store")
    public ResponseEntity<StoreEquationResponse> storeEquation(@Valid @RequestBody StoreEquationRequest request) {
        StoreEquationResponse response = equationService.storeEquation(request);
        return ResponseEntity.ok(response);
    }
    
    /**
     * Retrieve all stored equations
     */
    @GetMapping
    public ResponseEntity<EquationListResponse> getAllEquations() {
        EquationListResponse response = equationService.getAllEquations();
        return ResponseEntity.ok(response);
    }
    
    /**
     * Evaluate a specific equation
     */
    @PostMapping("/{equationId}/evaluate")
    public ResponseEntity<EvaluateEquationResponse> evaluateEquation(
            @PathVariable String equationId,
            @Valid @RequestBody EvaluateEquationRequest request) {
        EvaluateEquationResponse response = equationService.evaluateEquation(equationId, request);
        return ResponseEntity.ok(response);
    }
    
    /**
     * Health check endpoint
     */
    @GetMapping("/health")
    public ResponseEntity<String> healthCheck() {
        return ResponseEntity.ok("Equation Solver API is running!");
    }
}