package com.equation.dto;

import java.util.List;

/**
 * Response DTO for listing equations
 */
public class EquationListResponse {
    private List<EquationInfo> equations;
    
    public EquationListResponse() {}
    
    public EquationListResponse(List<EquationInfo> equations) {
        this.equations = equations;
    }
    
    public List<EquationInfo> getEquations() { return equations; }
    public void setEquations(List<EquationInfo> equations) { this.equations = equations; }
}