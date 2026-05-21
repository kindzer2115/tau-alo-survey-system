package com.ankieta.questions.repository;

import com.ankieta.questions.model.QuestionCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface QuestionCategoryRepository extends JpaRepository<QuestionCategory, Long> {
}