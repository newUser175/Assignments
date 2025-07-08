package com.equation.service;

import com.equation.exception.EvaluationException;
import com.equation.model.TreeNode;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class EquationEvaluatorTest {
    
    private EquationEvaluator evaluator;
    private ExpressionParser parser;
    
    @BeforeEach
    void setUp() {
        evaluator = new EquationEvaluator();
        parser = new ExpressionParser();
    }
    
    @Test
    void testSimpleEvaluation() {
        TreeNode tree = parser.parseToPostfixTree("x + y");
        Map<String, Double> variables = Map.of("x", 2.0, "y", 3.0);
        
        double result = evaluator.evaluate(tree, variables);
        assertEquals(5.0, result, 0.001);
    }
    
    @Test
    void testComplexEvaluation() {
        TreeNode tree = parser.parseToPostfixTree("3x + 2y - z");
        Map<String, Double> variables = Map.of("x", 2.0, "y", 3.0, "z", 1.0);
        
        double result = evaluator.evaluate(tree, variables);
        assertEquals(11.0, result, 0.001); // 3*2 + 2*3 - 1 = 6 + 6 - 1 = 11
    }
    
    @Test
    void testExponentiation() {
        TreeNode tree = parser.parseToPostfixTree("x^2 + y^2");
        Map<String, Double> variables = Map.of("x", 3.0, "y", 4.0);
        
        double result = evaluator.evaluate(tree, variables);
        assertEquals(25.0, result, 0.001); // 3^2 + 4^2 = 9 + 16 = 25
    }
    
    @Test
    void testDivision() {
        TreeNode tree = parser.parseToPostfixTree("x / y");
        Map<String, Double> variables = Map.of("x", 10.0, "y", 2.0);
        
        double result = evaluator.evaluate(tree, variables);
        assertEquals(5.0, result, 0.001);
    }
    
    @Test
    void testDivisionByZero() {
        TreeNode tree = parser.parseToPostfixTree("x / y");
        Map<String, Double> variables = Map.of("x", 10.0, "y", 0.0);
        
        assertThrows(EvaluationException.class, () -> {
            evaluator.evaluate(tree, variables);
        });
    }
    
    @Test
    void testMissingVariable() {
        TreeNode tree = parser.parseToPostfixTree("x + y");
        Map<String, Double> variables = Map.of("x", 2.0); // Missing y
        
        assertThrows(EvaluationException.class, () -> {
            evaluator.evaluate(tree, variables);
        });
    }
    
    @Test
    void testNullTree() {
        Map<String, Double> variables = Map.of("x", 2.0);
        
        assertThrows(EvaluationException.class, () -> {
            evaluator.evaluate(null, variables);
        });
    }
}