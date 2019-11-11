package com.topparts.pricesupplier.controller;

import com.topparts.pricesupplier.model.dto.ProductDetailsDTO;
import com.topparts.pricesupplier.model.entity.PriceListRow;
import com.topparts.pricesupplier.model.service.ProductService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class ProductController {
    private ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping("/price-list")
    public Page<PriceListRow> getPriceList(@PageableDefault(size = 5000, sort = "id") Pageable pageable) {
        System.out.println("PriceController");
        return productService.getPriceList(pageable);
    }

    @GetMapping("/details/{id}")
    public ProductDetailsDTO getProductDetails(@PathVariable Long id) {
        return new ProductDetailsDTO(productService.getProductById(id));
    }
}
