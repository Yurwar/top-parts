package com.topparts.model.dto;

import com.topparts.model.entity.OrderEntry;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class OrderEntryDto {
    private Long productId;
    private Long supplierId;
    private Long quantity;

    public OrderEntryDto(OrderEntry orderEntry) {
        this.productId = orderEntry.getProductId();
        this.supplierId = orderEntry.getSupplierId();
        this.quantity = orderEntry.getQuantity();
    }
}
