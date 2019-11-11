package com.topparts.pricesupplier.model.entity;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
public class PriceListRow {
    private Long id;
    private Double price;

    public PriceListRow() {};

    public PriceListRow(Product product) {
        this.id = product.getId();
        this.price = product.getPrice();
    }
}
