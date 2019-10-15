package com.topparts.pricesupplier.model.dto;

import com.topparts.pricesupplier.model.entity.Product;
import lombok.Data;

@Data
public class ProductDetailsDTO {
    private Long id;
    private String description;

    public ProductDetailsDTO(Product product) {

    }
}
