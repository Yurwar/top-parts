package com.topparts.searchsupplier.controller;

import com.topparts.searchsupplier.model.entity.Product;
import com.topparts.searchsupplier.model.service.ProductService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/search")
public class SearchController {
    private ProductService productService;

    public SearchController(ProductService productService) {
        this.productService = productService;
    }

    @RequestMapping
    public List<Product> getProductsByQuery(@RequestParam String query) {
        return productService.getAllProductsByQuery(query);
    }
}
