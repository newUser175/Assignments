package com.equation.exception;

/**
 * Exception thrown when an equation is invalid
 */
public class InvalidEquationException extends RuntimeException {
    public InvalidEquationException(String message) {
        super(message);
    }
    
    public InvalidEquationException(String message, Throwable cause) {
        super(message, cause);
    }
}