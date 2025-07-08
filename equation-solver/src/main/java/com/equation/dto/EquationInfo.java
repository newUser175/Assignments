package com.equation.dto;

/**
 * DTO for equation information in list responses
 */
public class EquationInfo {
    private String equationId;
    private String equation;
    
    public EquationInfo() {}
    
    public EquationInfo(String equationId, String equation) {
        this.equationId = equationId;
        this.equation = equation;
    }
    
    public String getEquationId() { return equationId; }
    public void setEquationId(String equationId) { this.equationId = equationId; }
    
    public String getEquation() { return equation; }
    public void setEquation(String equation) { this.equation = equation; }
}