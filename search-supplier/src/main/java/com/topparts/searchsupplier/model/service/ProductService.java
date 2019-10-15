package com.topparts.searchsupplier.model.service;

import com.topparts.searchsupplier.model.entity.Product;

import java.util.List;

public interface ProductService {
    public List<Product> getAllProducts();

    public List<Product> getAllProductsByQuery(String query);
}
