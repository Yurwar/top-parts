package com.topparts.model.service.impl;

import com.topparts.model.entity.Product;
import com.topparts.model.repository.ProductRepository;
import com.topparts.model.service.ProductService;
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

    public ProductServiceImpl(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public void createProduct(Product product) {
        productRepository.save(product);
    }

    @Override
    public Optional<Product> getProductById(Long id) {
        return productRepository.findById(id);
    }

    @Override
    public List<Product> getAllProducts() {
        log.trace("Trying to get all products");
        List<Product> products = productRepository.findAll();
        log.trace("Return all products");
        return products;
    }

    @Override
    public List<Product> getAllProductsBySearchQuery(String query) {
        log.trace("Trying to get all products by query: {}", query);
        List<Product> productsByQuery = productRepository
                .findAllByNameContainingOrDescriptionContaining(query, query);
        log.trace("Return all products by query");
        return productsByQuery;
    }

    @Override
    public void updateProduct(Long id, Product product) {
        productRepository.findById(id).orElseThrow(NoSuchElementException::new);
        productRepository.save(product);
    }

    @Override
    public void deleteProduct(Long id) {
        productRepository.deleteById(id);
    }
}
