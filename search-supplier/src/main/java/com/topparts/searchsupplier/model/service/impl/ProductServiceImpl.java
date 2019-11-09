package com.topparts.searchsupplier.model.service.impl;

import com.topparts.searchsupplier.model.entity.Product;
import com.topparts.searchsupplier.model.repository.ProductRepository;
import com.topparts.searchsupplier.model.service.ProductService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Random;

@Service
@Slf4j
public class ProductServiceImpl implements ProductService {
    private ProductRepository productRepository;

    public ProductServiceImpl(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public List<Product> getAllProducts() {
        log.trace("Trying to get all products");
        delayResponseInSeconds(15, 20);
        List<Product> products = productRepository.findAll();
        log.trace("Return all products");
        return products;
    }

    @Override
    public List<Product> getAllProductsByQuery(String query) {
        log.trace("Trying to get all products by query: {}", query);
        delayResponseInSeconds(15, 20);
        List<Product> productsByQuery = productRepository.findAllByNameContainingOrDescriptionContaining(query, query);
        log.trace("Return all products by query");
        return productsByQuery;
    }

    private void delayResponseInSeconds(int minDelay, int maxDelay) {
        try {
            Thread.sleep(generateResponseDelayInSeconds(minDelay, maxDelay) * 1000);
        } catch (InterruptedException e) {
            log.error("Artificial response delay is interrupted", e);
        }
    }

    private int generateResponseDelayInSeconds(int min, int max) {
        Random random = new Random();
        ++max;
        int responseDelay = random.nextInt(max - min) + min;
        log.trace("Artificial response delay is: {}", responseDelay);
        return responseDelay;
    }
}
