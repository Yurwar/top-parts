package com.topparts.model.entity;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@EqualsAndHashCode

public class Product {
    private Long id;
    private String name;
    private String description;
    private Double price;
    private Supplier supplier;
}
