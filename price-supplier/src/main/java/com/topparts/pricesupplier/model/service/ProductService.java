package com.topparts.pricesupplier.model.service;

import com.topparts.pricesupplier.model.entity.Product;

import java.util.Map;

public interface ProductService {
    Map<String, Double> getPriceList();

    Product getProductById(Long id);
}
