package com.equation.service;

import com.equation.exception.EvaluationException;
import com.equation.model.TreeNode;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * Service for evaluating postfix trees with variable substitution
 */
@Service
public class EquationEvaluator {
    
    /**
     * Evaluate a postfix tree with given variable values
     */
    public double evaluate(TreeNode root, Map<String, Double> variables) {
        if (root == null) {
            throw new EvaluationException("Cannot evaluate null expression tree");
        }
        
        try {
            return evaluateNode(root, variables);
        } catch (Exception e) {
            throw new EvaluationException("Error during evaluation: " + e.getMessage(), e);
        }
    }
    
    private double evaluateNode(TreeNode node, Map<String, Double> variables) {
        if (node.isOperand()) {
            return Double.parseDouble(node.getValue());
        }
        
        if (node.isVariable()) {
            String varName = node.getValue();
            if (!variables.containsKey(varName)) {
                throw new EvaluationException("Variable '" + varName + "' not provided in variables map");
            }
            return variables.get(varName);
        }
        
        if (node.isOperator()) {
            double leftValue = evaluateNode(node.getLeft(), variables);
            double rightValue = evaluateNode(node.getRight(), variables);
            
            return applyOperator(node.getValue(), leftValue, rightValue);
        }
        
        throw new EvaluationException("Unknown node type: " + node.getType());
    }
    
    private double applyOperator(String operator, double left, double right) {
        switch (operator) {
            case "+":
                return left + right;
            case "-":
                return left - right;
            case "*":
                return left * right;
            case "/":
                if (right == 0) {
                    throw new EvaluationException("Division by zero");
                }
                return left / right;
            case "^":
                return Math.pow(left, right);
            default:
                throw new EvaluationException("Unknown operator: " + operator);
        }
    }
}