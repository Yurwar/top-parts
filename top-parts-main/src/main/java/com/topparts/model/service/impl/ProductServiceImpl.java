package com.topparts.model.service.impl;

import com.topparts.model.entity.Product;
import com.topparts.model.repository.CategoryRepository;
import com.topparts.model.repository.ProductRepository;
import com.topparts.model.service.ProductService;
import lombok.extern.log4j.Log4j2;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@Slf4j
public class ProductServiceImpl implements ProductService {
    private ProductRepository productRepository;
    private CategoryRepository categoryRepository;

    public ProductServiceImpl(ProductRepository productRepository, CategoryRepository categoryRepository) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
    }

    @Override
    @CachePut(value = "products", key = "#product.getId()")
    public void createProduct(Product product) {
        productRepository.save(product);
    }

    @Override
    @Cacheable(value = "products", key = "#id")
    public Optional<Product> getProductById(Long id) {
        return productRepository.findById(id);
    }

    @Override
    @Cacheable(value = "products")
    public List<Product> getAllProducts() {
        log.trace("Trying to get all products");
        List<Product> products = productRepository.findAll();
        log.trace("Return all products");
        return products;
    }

    @Override
    @Cacheable(value = "products", key = "#query.toLowerCase().trim()")
    public List<Product> getAllProductsBySearchQuery(String query) {
        log.trace("Trying to get all products by query: {}", query);
        List<Product> productsByQuery = productRepository
                .findAllByNameContainingOrDescriptionContaining(query, query);
        log.trace("Return all products by query");
        return productsByQuery;
    }

    @Override
    public List<Product> getAllProductsByCategory(Long id) {
        return productRepository.getAllByCategoriesContains(categoryRepository.findById(id).orElseThrow(NoSuchElementException::new));
    }

    @Override
    @CachePut(value = "products", key = "#id")
    public void updateProduct(Long id, Product product) {
        productRepository.findById(id).orElseThrow(NoSuchElementException::new);
        productRepository.save(product);
    }

    @Override
    @CacheEvict(value = "products", key = "#id")
    public void deleteProduct(Long id) {
        productRepository.deleteById(id);
    }
}
