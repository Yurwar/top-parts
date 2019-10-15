package com.topparts.model.service;

import com.topparts.model.entity.Product;

import java.util.List;
import java.util.Optional;

public interface ProductService {
    void createProduct(Product product);

    Optional<Product> getProductById(Long id);

    List<Product> getAllProducts();

    List<Product> getAllProductsByCategory(Long id);

    void updateProduct(Long id, Product product);

    void deleteProduct(Long id);
}
