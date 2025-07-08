package com.equation.exception;

/**
 * Exception thrown when an equation is not found
 */
public class EquationNotFoundException extends RuntimeException {
    public EquationNotFoundException(String message) {
        super(message);
    }
}