package com.topparts.model.dto;

import com.topparts.model.entity.Order;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class OrderDto {
    private Long id;
    private Long userId;
    private Set<OrderEntryDto> entries;
    private LocalDateTime dateOfPurchase;

    public OrderDto(Order order) {
        this.id = order.getId();
        this.userId = order.getUserId();
        this.entries = order.getEntries()
                .stream()
                .map(OrderEntryDto::new)
                .collect(Collectors.toSet());
        this.dateOfPurchase = order.getDateOfPurchase();
    }
}
