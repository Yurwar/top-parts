package com.topparts.model.dto;

import com.topparts.model.entity.Product;
import lombok.Data;

@Data
public class PriceListRowDTO {
    private Long id;
    private Double price;

    public PriceListRowDTO(Product product) {
        this.id = product.getId();
        this.price = product.getPrice();
    }
}
