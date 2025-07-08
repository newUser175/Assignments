package com.equation.model;

/**
 * Represents a stored algebraic equation
 */
public class Equation {
    private String equationId;
    private String originalEquation;
    private TreeNode postfixTree;
    
    public Equation() {}
    
    public Equation(String equationId, String originalEquation, TreeNode postfixTree) {
        this.equationId = equationId;
        this.originalEquation = originalEquation;
        this.postfixTree = postfixTree;
    }
    
    // Getters and Setters
    public String getEquationId() { return equationId; }
    public void setEquationId(String equationId) { this.equationId = equationId; }
    
    public String getOriginalEquation() { return originalEquation; }
    public void setOriginalEquation(String originalEquation) { this.originalEquation = originalEquation; }
    
    public TreeNode getPostfixTree() { return postfixTree; }
    public void setPostfixTree(TreeNode postfixTree) { this.postfixTree = postfixTree; }
}