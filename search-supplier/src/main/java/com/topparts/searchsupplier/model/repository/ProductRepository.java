package com.topparts.searchsupplier.model.repository;

import com.topparts.searchsupplier.model.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findAllByNameContainingOrDescriptionContaining(String nameContains, String descriptionContains);
}
