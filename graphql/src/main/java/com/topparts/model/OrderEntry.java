package com.topparts.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderEntry {
    private long id;
    private long productId;
    private long supplierId;
    private int quantity;
    private long orderId;
}
