package com.topparts.pricesupplier.model.service.impl;

import com.topparts.pricesupplier.model.entity.Product;
import com.topparts.pricesupplier.model.repository.ProductRepository;
import com.topparts.pricesupplier.model.service.ProductService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
@Slf4j
public class ProductServiceImpl implements ProductService {
    private ProductRepository productRepository;

    public ProductServiceImpl(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public Map<Long, Double> getPriceList() {
        log.trace("Trying to get price list");
        Map<Long, Double> priceListMap = productRepository.findAll()
                .stream()
                .collect(Collectors.toMap(Product::getId, Product::getPrice));
        log.trace("Return price list map");
        return priceListMap;
    }

    @Override
    public Product getProductById(Long id) {
        log.trace("Trying to get product by id: {}", id);
        Product product = productRepository.findById(id).orElseThrow(NoSuchElementException::new);
        log.trace("Return product by id");
        return product;
    }
}
