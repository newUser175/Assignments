package com.equation.dto;

import java.util.Map;

/**
 * Response DTO for evaluating an equation
 */
public class EvaluateEquationResponse {
    private String equationId;
    private String equation;
    private Map<String, Double> variables;
    private Double result;
    
    public EvaluateEquationResponse() {}
    
    public EvaluateEquationResponse(String equationId, String equation, 
                                   Map<String, Double> variables, Double result) {
        this.equationId = equationId;
        this.equation = equation;
        this.variables = variables;
        this.result = result;
    }
    
    public String getEquationId() { return equationId; }
    public void setEquationId(String equationId) { this.equationId = equationId; }
    
    public String getEquation() { return equation; }
    public void setEquation(String equation) { this.equation = equation; }
    
    public Map<String, Double> getVariables() { return variables; }
    public void setVariables(Map<String, Double> variables) { this.variables = variables; }
    
    public Double getResult() { return result; }
    public void setResult(Double result) { this.result = result; }
}