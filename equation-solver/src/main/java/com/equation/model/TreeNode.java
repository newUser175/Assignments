package com.equation.model;

/**
 * Represents a node in the postfix expression tree
 */
public class TreeNode {
    private String value;
    private TreeNode left;
    private TreeNode right;
    private NodeType type;
    
    public enum NodeType {
        OPERATOR, OPERAND, VARIABLE
    }
    
    public TreeNode(String value, NodeType type) {
        this.value = value;
        this.type = type;
        this.left = null;
        this.right = null;
    }
    
    public TreeNode(String value, NodeType type, TreeNode left, TreeNode right) {
        this.value = value;
        this.type = type;
        this.left = left;
        this.right = right;
    }
    
    // Getters and Setters
    public String getValue() { return value; }
    public void setValue(String value) { this.value = value; }
    
    public TreeNode getLeft() { return left; }
    public void setLeft(TreeNode left) { this.left = left; }
    
    public TreeNode getRight() { return right; }
    public void setRight(TreeNode right) { this.right = right; }
    
    public NodeType getType() { return type; }
    public void setType(NodeType type) { this.type = type; }
    
    public boolean isOperator() {
        return type == NodeType.OPERATOR;
    }
    
    public boolean isOperand() {
        return type == NodeType.OPERAND;
    }
    
    public boolean isVariable() {
        return type == NodeType.VARIABLE;
    }
}