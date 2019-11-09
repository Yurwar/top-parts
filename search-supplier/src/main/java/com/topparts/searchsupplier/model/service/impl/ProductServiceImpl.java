package com.topparts.searchsupplier.model.service.impl;

import com.topparts.searchsupplier.model.entity.Product;
import com.topparts.searchsupplier.model.repository.ProductRepository;
import com.topparts.searchsupplier.model.service.ProductService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Random;

@Service
public class ProductServiceImpl implements ProductService {
    private ProductRepository productRepository;

    public ProductServiceImpl(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public List<Product> getAllProducts() {
        delayResponseInSeconds(15, 20);
        return productRepository.findAll();
    }

    @Override
    public List<Product> getAllProductsByQuery(String query) {
        delayResponseInSeconds(15, 20);
        return productRepository.findAllByNameContainingOrDescriptionContaining(query, query);
    }

    private void delayResponseInSeconds(int minDelay, int maxDelay) {
        try {
            Thread.sleep(generateResponseDelayInSeconds(minDelay, maxDelay) * 1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private int generateResponseDelayInSeconds(int min, int max) {
        Random random = new Random();
        ++max;
        return random.nextInt(max - min) + min;
    }
}
