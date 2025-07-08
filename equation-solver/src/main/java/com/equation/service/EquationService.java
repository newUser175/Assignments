package com.equation.service;

import com.equation.dto.*;
import com.equation.exception.EquationNotFoundException;
import com.equation.model.Equation;
import com.equation.model.TreeNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

/**
 * Main service for equation operations
 */
@Service
public class EquationService {
    
    private final Map<String, Equation> equations = new ConcurrentHashMap<>();
    private final AtomicLong idGenerator = new AtomicLong(1);
    
    @Autowired
    private ExpressionParser expressionParser;
    
    @Autowired
    private EquationEvaluator equationEvaluator;
    
    /**
     * Store a new equation
     */
    public StoreEquationResponse storeEquation(StoreEquationRequest request) {
        String equationId = String.valueOf(idGenerator.getAndIncrement());
        String originalEquation = request.getEquation().trim();
        
        TreeNode postfixTree = expressionParser.parseToPostfixTree(originalEquation);
        
        Equation equation = new Equation(equationId, originalEquation, postfixTree);
        equations.put(equationId, equation);
        
        return new StoreEquationResponse("Equation stored successfully", equationId);
    }
    
    /**
     * Retrieve all stored equations
     */
    public EquationListResponse getAllEquations() {
        List<EquationInfo> equationInfos = equations.values().stream()
            .map(eq -> new EquationInfo(eq.getEquationId(), eq.getOriginalEquation()))
            .collect(Collectors.toList());
        
        return new EquationListResponse(equationInfos);
    }
    
    /**
     * Evaluate a specific equation with given variables
     */
    public EvaluateEquationResponse evaluateEquation(String equationId, EvaluateEquationRequest request) {
        Equation equation = equations.get(equationId);
        if (equation == null) {
            throw new EquationNotFoundException("Equation with ID '" + equationId + "' not found");
        }
        
        double result = equationEvaluator.evaluate(equation.getPostfixTree(), request.getVariables());
        
        return new EvaluateEquationResponse(
            equationId,
            equation.getOriginalEquation(),
            request.getVariables(),
            result
        );
    }
    
    /**
     * Get equation by ID (for testing purposes)
     */
    public Equation getEquationById(String equationId) {
        Equation equation = equations.get(equationId);
        if (equation == null) {
            throw new EquationNotFoundException("Equation with ID '" + equationId + "' not found");
        }
        return equation;
    }
    
    /**
     * Clear all equations (for testing purposes)
     */
    public void clearAllEquations() {
        equations.clear();
        idGenerator.set(1);
    }
}