package com.edu.tau.alo.tau_survey_system.controller;

import com.edu.tau.alo.tau_survey_system.model.SurveyToken;
import com.edu.tau.alo.tau_survey_system.service.SurveyTokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/tokens")
@CrossOrigin(origins = "http://localhost:3000")
public class SurveyTokenController {

    @Autowired
    private SurveyTokenService tokenService;

    @PostMapping("/generate")
    public ResponseEntity<List<SurveyToken>> generate(@RequestBody Map<String, Long> body) {
        Long surveyId = body.get("surveyId");
        Long classId = body.get("classId");
        List<SurveyToken> tokens = tokenService.generateTokensForClass(surveyId, classId);
        return ResponseEntity.ok(tokens);
    }

    @GetMapping("/validate/{token}")
    public ResponseEntity<?> validate(@PathVariable String token) {
        try {
            SurveyToken t = tokenService.validateToken(token);
            if (t.getIsUsed()) {
                return ResponseEntity.badRequest().body("Token został już użyty");
            }
            return ResponseEntity.ok(Map.of(
                    "valid", true,
                    "surveyId", t.getSurvey().getId(),
                    "userId", t.getUser().getId()
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Token nie istnieje");
        }
    }

    @PostMapping("/use/{token}")
    public ResponseEntity<?> use(@PathVariable String token) {
        try {
            tokenService.useToken(token);
            return ResponseEntity.ok(Map.of("message", "Token użyty pomyślnie"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/survey/{surveyId}")
    public ResponseEntity<List<SurveyToken>> getBySurvey(@PathVariable Long surveyId) {
        return ResponseEntity.ok(tokenService.getTokensForSurvey(surveyId));
    }
}