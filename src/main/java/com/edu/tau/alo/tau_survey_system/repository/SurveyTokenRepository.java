package com.edu.tau.alo.tau_survey_system.repository;

import com.edu.tau.alo.tau_survey_system.model.SurveyToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;
import java.util.List;

@Repository
public interface SurveyTokenRepository extends JpaRepository<SurveyToken, Long> {
    Optional<SurveyToken> findByToken(String token);
    List<SurveyToken> findBySurveyId(Long surveyId);
    boolean existsByUserIdAndSurveyIdAndIsUsedTrue(Long userId, Long surveyId);
}