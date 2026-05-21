package com.ankieta.questions.controller;

import com.ankieta.questions.model.QuestionCategory;
import com.ankieta.questions.repository.QuestionCategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/categories")
@CrossOrigin(origins = "http://localhost:3000")
public class CategoryController {

    @Autowired
    private QuestionCategoryRepository categoryRepository;

    @GetMapping
    public List<QuestionCategory> getAll() {
        return categoryRepository.findAll();
    }

    @PostMapping
    public QuestionCategory create(@RequestBody QuestionCategory category) {
        return categoryRepository.save(category);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        categoryRepository.deleteById(id);
    }
}