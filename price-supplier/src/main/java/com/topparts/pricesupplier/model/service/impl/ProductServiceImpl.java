package com.topparts.pricesupplier.model.service.impl;

import com.topparts.pricesupplier.model.entity.Product;
import com.topparts.pricesupplier.model.repository.ProductRepository;
import com.topparts.pricesupplier.model.service.ProductService;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
public class ProductServiceImpl implements ProductService {
    private ProductRepository productRepository;

    public ProductServiceImpl(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public Map<Long, Double> getPriceList() {
        return productRepository.findAll()
                .stream()
                .collect(Collectors.toMap(Product::getId, Product::getPrice));
    }

    @Override
    public Product getProductById(Long id) {
        return productRepository.findById(id).orElseThrow(NoSuchElementException::new);
    }
}
