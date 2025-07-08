package com.equation.service;

import com.equation.exception.InvalidEquationException;
import com.equation.model.TreeNode;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Service for parsing algebraic expressions into postfix trees
 */
@Service
public class ExpressionParser {
    
    private static final Map<String, Integer> OPERATOR_PRECEDENCE = Map.of(
        "+", 1,
        "-", 1,
        "*", 2,
        "/", 2,
        "^", 3
    );
    
    private static final Set<String> RIGHT_ASSOCIATIVE = Set.of("^");
    
    /**
     * Parse an infix expression into a postfix tree
     */
    public TreeNode parseToPostfixTree(String expression) {
        try {
            List<String> tokens = tokenize(expression);
            List<String> postfix = infixToPostfix(tokens);
            return buildPostfixTree(postfix);
        } catch (Exception e) {
            throw new InvalidEquationException("Failed to parse expression: " + expression, e);
        }
    }
    
    /**
     * Tokenize the expression into operators, operands, and variables
     */
    private List<String> tokenize(String expression) {
        List<String> tokens = new ArrayList<>();
        Pattern pattern = Pattern.compile("([a-zA-Z]+|\\d+(?:\\.\\d+)?|[+\\-*/^()]|\\s+)");
        Matcher matcher = pattern.matcher(expression.replaceAll("\\s+", ""));
        
        String processedExpression = preprocessExpression(expression);
        pattern = Pattern.compile("([a-zA-Z]+|\\d+(?:\\.\\d+)?|[+\\-*/^()])");
        matcher = pattern.matcher(processedExpression);
        
        while (matcher.find()) {
            String token = matcher.group(1);
            if (!token.trim().isEmpty()) {
                tokens.add(token);
            }
        }
        
        return tokens;
    }
    
    /**
     * Preprocess expression to handle implicit multiplication
     */
    private String preprocessExpression(String expression) {
        expression = expression.replaceAll("\\s+", "");
        
        // Handle implicit multiplication like 3x -> 3*x
        expression = expression.replaceAll("(\\d)([a-zA-Z])", "$1*$2");
        
        // Handle cases like x2 -> x*2 (though less common)
        expression = expression.replaceAll("([a-zA-Z])(\\d)", "$1*$2");
        
        // Handle cases like (x)(y) -> (x)*(y)
        expression = expression.replaceAll("\\)\\(", ")*(");
        
        // Handle cases like x(y) -> x*(y)
        expression = expression.replaceAll("([a-zA-Z])\\(", "$1*(");
        
        // Handle cases like 2(x) -> 2*(x)
        expression = expression.replaceAll("(\\d)\\(", "$1*(");
        
        return expression;
    }
    
    /**
     * Convert infix tokens to postfix notation using Shunting Yard algorithm
     */
    private List<String> infixToPostfix(List<String> tokens) {
        List<String> output = new ArrayList<>();
        Stack<String> operators = new Stack<>();
        
        for (String token : tokens) {
            if (isOperand(token) || isVariable(token)) {
                output.add(token);
            } else if (token.equals("(")) {
                operators.push(token);
            } else if (token.equals(")")) {
                while (!operators.isEmpty() && !operators.peek().equals("(")) {
                    output.add(operators.pop());
                }
                if (operators.isEmpty()) {
                    throw new InvalidEquationException("Mismatched parentheses");
                }
                operators.pop(); // Remove the '('
            } else if (isOperator(token)) {
                while (!operators.isEmpty() && 
                       !operators.peek().equals("(") &&
                       (OPERATOR_PRECEDENCE.get(operators.peek()) > OPERATOR_PRECEDENCE.get(token) ||
                        (OPERATOR_PRECEDENCE.get(operators.peek()).equals(OPERATOR_PRECEDENCE.get(token)) &&
                         !RIGHT_ASSOCIATIVE.contains(token)))) {
                    output.add(operators.pop());
                }
                operators.push(token);
            } else {
                throw new InvalidEquationException("Invalid token: " + token);
            }
        }
        
        while (!operators.isEmpty()) {
            String op = operators.pop();
            if (op.equals("(") || op.equals(")")) {
                throw new InvalidEquationException("Mismatched parentheses");
            }
            output.add(op);
        }
        
        return output;
    }
    
    /**
     * Build a postfix tree from postfix notation
     */
    private TreeNode buildPostfixTree(List<String> postfix) {
        Stack<TreeNode> stack = new Stack<>();
        
        for (String token : postfix) {
            if (isOperator(token)) {
                if (stack.size() < 2) {
                    throw new InvalidEquationException("Invalid expression: insufficient operands for operator " + token);
                }
                TreeNode right = stack.pop();
                TreeNode left = stack.pop();
                TreeNode node = new TreeNode(token, TreeNode.NodeType.OPERATOR, left, right);
                stack.push(node);
            } else if (isOperand(token)) {
                stack.push(new TreeNode(token, TreeNode.NodeType.OPERAND));
            } else if (isVariable(token)) {
                stack.push(new TreeNode(token, TreeNode.NodeType.VARIABLE));
            } else {
                throw new InvalidEquationException("Invalid token in postfix expression: " + token);
            }
        }
        
        if (stack.size() != 1) {
            throw new InvalidEquationException("Invalid expression: malformed postfix notation");
        }
        
        return stack.pop();
    }
    
    /**
     * Reconstruct infix expression from postfix tree
     */
    public String treeToInfix(TreeNode root) {
        if (root == null) {
            return "";
        }
        
        if (root.isOperand() || root.isVariable()) {
            return root.getValue();
        }
        
        String left = treeToInfix(root.getLeft());
        String right = treeToInfix(root.getRight());
        
        // Add parentheses for clarity, but optimize for readability
        if (needsParentheses(root.getLeft(), root.getValue(), true)) {
            left = "(" + left + ")";
        }
        if (needsParentheses(root.getRight(), root.getValue(), false)) {
            right = "(" + right + ")";
        }
        
        return left + " " + root.getValue() + " " + right;
    }
    
    /**
     * Determine if parentheses are needed for a subexpression
     */
    private boolean needsParentheses(TreeNode child, String parentOp, boolean isLeft) {
        if (child == null || !child.isOperator()) {
            return false;
        }
        
        String childOp = child.getValue();
        int parentPrec = OPERATOR_PRECEDENCE.get(parentOp);
        int childPrec = OPERATOR_PRECEDENCE.get(childOp);
        
        if (childPrec < parentPrec) {
            return true;
        }
        
        if (childPrec == parentPrec) {
            // For same precedence, check associativity
            if (!isLeft && !RIGHT_ASSOCIATIVE.contains(parentOp)) {
                return true;
            }
            if (isLeft && RIGHT_ASSOCIATIVE.contains(parentOp)) {
                return true;
            }
        }
        
        return false;
    }
    
    private boolean isOperator(String token) {
        return OPERATOR_PRECEDENCE.containsKey(token);
    }
    
    private boolean isOperand(String token) {
        try {
            Double.parseDouble(token);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
    
    private boolean isVariable(String token) {
        return token.matches("[a-zA-Z]+");
    }
}