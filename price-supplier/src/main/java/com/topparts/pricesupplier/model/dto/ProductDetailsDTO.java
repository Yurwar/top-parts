package com.topparts.pricesupplier.model.dto;

import com.topparts.pricesupplier.model.entity.Product;
import lombok.Data;

@Data
public class ProductDetailsDTO {
    private String name;
    private String description;

    public ProductDetailsDTO(Product product) {
        this.name = product.getName();
        this.description = product.getDescription();
    }
}
