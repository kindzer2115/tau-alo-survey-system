package com.edu.tau.alo.tau_survey_system.service;

import com.edu.tau.alo.tau_survey_system.model.*;
import com.edu.tau.alo.tau_survey_system.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class SurveyTokenService {

    @Autowired
    private SurveyTokenRepository tokenRepository;

    @Autowired
    private SurveyRepository surveyRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ClassRepository classRepository;

    public List<SurveyToken> generateTokensForClass(Long surveyId, Long classId) {
        Survey survey = surveyRepository.findById(surveyId)
                .orElseThrow(() -> new RuntimeException("Ankieta nie znaleziona"));

        classRepository.findById(classId)
                .orElseThrow(() -> new RuntimeException("Klasa nie znaleziona"));

        List<User> students = userRepository.findStudentsByClassId(classId);

        return students.stream().map(student -> {
            SurveyToken token = new SurveyToken();
            token.setToken(UUID.randomUUID().toString());
            token.setUser(student);
            token.setSurvey(survey);
            token.setIsUsed(false);
            token.setCreatedAt(LocalDateTime.now());
            return tokenRepository.save(token);
        }).toList();
    }

    public SurveyToken validateToken(String token) {
        return tokenRepository.findByToken(token)
                .orElseThrow(() -> new RuntimeException("Token nie istnieje"));
    }

    public void useToken(String token) {
        SurveyToken surveyToken = tokenRepository.findByToken(token)
                .orElseThrow(() -> new RuntimeException("Token nie istnieje"));

        if (surveyToken.getIsUsed()) {
            throw new RuntimeException("Token został już użyty");
        }

        surveyToken.setIsUsed(true);
        surveyToken.setUsedAt(LocalDateTime.now());
        tokenRepository.save(surveyToken);
    }

    public List<SurveyToken> getTokensForSurvey(Long surveyId) {
        return tokenRepository.findBySurveyId(surveyId);
    }
}