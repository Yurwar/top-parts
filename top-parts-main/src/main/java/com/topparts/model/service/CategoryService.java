package com.topparts.model.service;

import com.topparts.model.entity.Category;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public interface CategoryService {
    void createCategory(Category category);

    Optional<Category> getCategoryById(Long id);

    List<Category> getAllCategories();

    List<Category> getAllRootCategories();

    void updateCategory(Category category);

    void deleteCategory(Category category);
}
