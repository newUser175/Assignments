package com.equation.controller;

import com.equation.dto.StoreEquationRequest;
import com.equation.service.EquationService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(EquationController.class)
class EquationControllerTest {
    
    @Autowired
    private MockMvc mockMvc;
    
    @MockBean
    private EquationService equationService;
    
    @Autowired
    private ObjectMapper objectMapper;
    
    @Test
    void testStoreEquationEndpoint() throws Exception {
        StoreEquationRequest request = new StoreEquationRequest("x + y");
        
        mockMvc.perform(post("/api/equations/store")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());
    }
    
    @Test
    void testStoreEquationWithEmptyEquation() throws Exception {
        StoreEquationRequest request = new StoreEquationRequest("");
        
        mockMvc.perform(post("/api/equations/store")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }
    
    @Test
    void testGetAllEquationsEndpoint() throws Exception {
        mockMvc.perform(get("/api/equations"))
                .andExpect(status().isOk());
    }
    
    @Test
    void testHealthCheckEndpoint() throws Exception {
        mockMvc.perform(get("/api/equations/health"))
                .andExpect(status().isOk())
                .andExpect(content().string("Equation Solver API is running!"));
    }
}