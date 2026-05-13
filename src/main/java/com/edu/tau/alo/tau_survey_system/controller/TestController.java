package com.edu.tau.alo.tau_survey_system.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.CrossOrigin;

@RestController
@RequestMapping("/api")
// Pozwalamy Twojemu Reactowi na dostęp do tego konkretnego kontrolera
@CrossOrigin(origins = "http://localhost:3000")
public class TestController {

    @GetMapping("/test")
    public String test() {
        // Ta wiadomość pojawi się w alercie w React
        return "Backend połączony pomyślnie! Uwierzytalnianie Azure ID działa.";
    }
}