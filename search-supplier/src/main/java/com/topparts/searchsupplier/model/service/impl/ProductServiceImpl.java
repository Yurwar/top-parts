package com.topparts.searchsupplier.model.service.impl;

import com.topparts.searchsupplier.model.entity.Product;
import com.topparts.searchsupplier.model.repository.ProductRepository;
import com.topparts.searchsupplier.model.service.ProductService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductServiceImpl implements ProductService {
    private ProductRepository productRepository;

    public ProductServiceImpl(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    @Override
    public List<Product> getAllProductsByQuery(String query) {
        return productRepository.findALlByNameContainingOrDescriptionContaining(query, query);
    }
}
