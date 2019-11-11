package com.topparts.pricesupplier.controller;

import com.topparts.pricesupplier.model.dto.PagedPriceListDTO;
import com.topparts.pricesupplier.model.dto.ProductDetailsDTO;
import com.topparts.pricesupplier.model.service.ProductService;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class ProductController {
    private ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping("/price-list")
    public PagedPriceListDTO getPriceList(@PageableDefault(size = 5000, sort = "id") Pageable pageable) {
        Pageable modifiedPageable = PageRequest.of(pageable.getPageNumber(), 5000);
        System.out.println("PriceController");
        return productService.getPriceList(modifiedPageable);
    }

    @GetMapping("/details/{id}")
    public ProductDetailsDTO getProductDetails(@PathVariable Long id) {
        return new ProductDetailsDTO(productService.getProductById(id));
    }
}
