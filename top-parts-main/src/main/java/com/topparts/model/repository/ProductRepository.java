package com.topparts.model.repository;

import com.topparts.model.entity.Category;
import com.topparts.model.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> getAllByCategoriesContains(Category category);
    
    List<Product> findAllByNameContainingOrDescriptionContaining(String nameContains, String descriptionContains);
}
