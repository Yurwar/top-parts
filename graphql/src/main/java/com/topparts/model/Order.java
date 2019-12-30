package com.topparts.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Order {
    private long id;
    private long userId;
    private String dateOfPurchase;
    private List<OrderEntry> entries;
}
