package com.equation.service;

import com.equation.exception.InvalidEquationException;
import com.equation.model.TreeNode;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ExpressionParserTest {
    
    private ExpressionParser expressionParser;
    
    @BeforeEach
    void setUp() {
        expressionParser = new ExpressionParser();
    }
    
    @Test
    void testSimpleAddition() {
        TreeNode tree = expressionParser.parseToPostfixTree("x + y");
        assertNotNull(tree);
        assertEquals("+", tree.getValue());
        assertEquals("x", tree.getLeft().getValue());
        assertEquals("y", tree.getRight().getValue());
    }
    
    @Test
    void testComplexExpression() {
        TreeNode tree = expressionParser.parseToPostfixTree("3x + 2y - z");
        assertNotNull(tree);
        assertEquals("-", tree.getValue());
        assertEquals("+", tree.getLeft().getValue());
        assertEquals("z", tree.getRight().getValue());
    }
    
    @Test
    void testExponentiation() {
        TreeNode tree = expressionParser.parseToPostfixTree("x^2 + y^2");
        assertNotNull(tree);
        assertEquals("+", tree.getValue());
        assertEquals("^", tree.getLeft().getValue());
        assertEquals("^", tree.getRight().getValue());
    }
    
    @Test
    void testParentheses() {
        TreeNode tree = expressionParser.parseToPostfixTree("(x + y) * z");
        assertNotNull(tree);
        assertEquals("*", tree.getValue());
        assertEquals("+", tree.getLeft().getValue());
        assertEquals("z", tree.getRight().getValue());
    }
    
    @Test
    void testTreeToInfix() {
        TreeNode tree = expressionParser.parseToPostfixTree("3x + 2y - z");
        String infix = expressionParser.treeToInfix(tree);
        assertNotNull(infix);
        assertTrue(infix.contains("x"));
        assertTrue(infix.contains("y"));
        assertTrue(infix.contains("z"));
    }
    
    @Test
    void testInvalidExpression() {
        assertThrows(InvalidEquationException.class, () -> {
            expressionParser.parseToPostfixTree("x + + y");
        });
    }
    
    @Test
    void testMismatchedParentheses() {
        assertThrows(InvalidEquationException.class, () -> {
            expressionParser.parseToPostfixTree("(x + y");
        });
    }
    
    @Test
    void testEmptyExpression() {
        assertThrows(InvalidEquationException.class, () -> {
            expressionParser.parseToPostfixTree("");
        });
    }
}