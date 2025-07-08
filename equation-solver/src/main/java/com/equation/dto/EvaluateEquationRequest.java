package com.equation.dto;

import jakarta.validation.constraints.NotNull;
import java.util.Map;

/**
 * Request DTO for evaluating an equation
 */
public class EvaluateEquationRequest {
    @NotNull(message = "Variables map cannot be null")
    private Map<String, Double> variables;
    
    public EvaluateEquationRequest() {}
    
    public EvaluateEquationRequest(Map<String, Double> variables) {
        this.variables = variables;
    }
    
    public Map<String, Double> getVariables() { return variables; }
    public void setVariables(Map<String, Double> variables) { this.variables = variables; }
}