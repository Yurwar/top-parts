package com.topparts.model.service;

import com.topparts.model.entity.Category;
import com.topparts.model.entity.Product;

import java.util.List;
import java.util.Optional;

public interface ProductService {
    void createProduct(Product product);

    Optional<Product> getProductById(Long id);

    List<Product> getAllProducts();

    List<Product> getAllProductsByCategory(Category category);

    void updateProduct(Product product);

    void deleteProduct(Product product);
}
