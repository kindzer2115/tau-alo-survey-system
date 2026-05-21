package com.ankieta.questions.repository;

import com.ankieta.questions.model.Question;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface QuestionRepository extends JpaRepository<Question, String> {
    List<Question> findByIsActiveTrue();
    List<Question> findByIsActiveTrueAndCategoryId(Long categoryId);
}