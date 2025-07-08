package com.equation.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Controller for serving the frontend
 */
@Controller
public class HomeController {
    
    @GetMapping("/")
    public String home() {
        return "index";
    }
}