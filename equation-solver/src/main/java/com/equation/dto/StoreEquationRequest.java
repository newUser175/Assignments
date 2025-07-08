package com.equation.dto;

import jakarta.validation.constraints.NotBlank;

/**
 * Request DTO for storing an equation
 */
public class StoreEquationRequest {
    @NotBlank(message = "Equation cannot be empty")
    private String equation;
    
    public StoreEquationRequest() {}
    
    public StoreEquationRequest(String equation) {
        this.equation = equation;
    }
    
    public String getEquation() { return equation; }
    public void setEquation(String equation) { this.equation = equation; }
}