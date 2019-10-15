package com.topparts.model.service.impl;

import com.topparts.model.entity.Product;
import com.topparts.model.repository.CategoryRepository;
import com.topparts.model.repository.ProductRepository;
import com.topparts.model.service.ProductService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class ProductServiceImpl implements ProductService {
    private ProductRepository productRepository;
    private CategoryRepository categoryRepository;

    public ProductServiceImpl(ProductRepository productRepository, CategoryRepository categoryRepository) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
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

    public List<Product> getAllProductsByCategory(Long id) {
        return productRepository.getAllByCategoriesContains(categoryRepository.findById(id).orElseThrow(NoSuchElementException::new));
    }

    public void updateProduct(Long id, Product product) {
        productRepository.findById(id).orElseThrow(NoSuchElementException::new);
        productRepository.save(product);
    }

    public void deleteProduct(Long id) {
        productRepository.deleteById(id);
    }
}
