package com.topparts.model.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class SearchSupplierProductDTO implements Serializable {
    private Long id;
    private String name;
    private String description;
    private Double price;
}
