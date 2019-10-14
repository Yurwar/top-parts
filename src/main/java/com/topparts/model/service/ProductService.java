package com.topparts.model.service;

import com.topparts.model.entity.Category;
import com.topparts.model.entity.Product;
import com.topparts.model.repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductService {
    private ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public void createProduct(Product product) {
        productRepository.save(product);
    }

    public Optional<Product> getProductById(Long id) {
        return productRepository.findById(id);
    }

    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    public List<Product> getAllProductsByCategory(Category category) {
        return productRepository.getAllByCategoriesContains(category);
    }

    public void updateProduct(Product product) {
        productRepository.save(product);
    }

    public void deleteProduct(Product product) {
        productRepository.delete(product);
    }
}
