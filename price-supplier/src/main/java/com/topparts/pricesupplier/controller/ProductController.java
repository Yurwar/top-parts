package com.topparts.pricesupplier.controller;

import com.topparts.pricesupplier.model.dto.ProductDetailsDTO;
import com.topparts.pricesupplier.model.entity.Product;
import com.topparts.pricesupplier.model.service.ProductService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/product")
public class ProductController {
    private ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping("/price-list")
    public Map<String, Double> getPriceList() {
        return productService.getPriceList();
    }

    @GetMapping("/details/{id}")
    public ProductDetailsDTO getProductDetails(@PathVariable Long id) {
        Product product = productService.getProductById(id);
        return new ProductDetailsDTO(product);
    }
}
