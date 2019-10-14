package com.topparts.model.repository;

import com.topparts.model.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
    List<Category> getAllCategoriesByParentCategoryIsNull();
}
