package com.topparts.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class PagedPriceListDTO {
    private Long totalPages;
    private Long currentPage;
    private Long pageSize;

    private Map<Long, Double> results;
}
