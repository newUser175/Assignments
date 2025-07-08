package com.equation.dto;

/**
 * Response DTO for storing an equation
 */
public class StoreEquationResponse {
    private String message;
    private String equationId;
    
    public StoreEquationResponse() {}
    
    public StoreEquationResponse(String message, String equationId) {
        this.message = message;
        this.equationId = equationId;
    }
    
    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
    
    public String getEquationId() { return equationId; }
    public void setEquationId(String equationId) { this.equationId = equationId; }
}