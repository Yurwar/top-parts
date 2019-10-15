package com.topparts.pricesupplier.model.service.impl;

import com.topparts.pricesupplier.model.entity.Product;
import com.topparts.pricesupplier.model.repository.ProductRepository;
import com.topparts.pricesupplier.model.service.ProductService;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class ProductServiceImpl implements ProductService {
    private ProductRepository productRepository;

    public ProductServiceImpl(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public Map<String, Double> getPriceList() {
        //TODO a lot of things!!!
        return null;
    }

    @Override
    public Product getProductById(Long id) {
        return productRepository.getOne(id);
    }
}
