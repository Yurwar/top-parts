package com.topparts.pricesupplier.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PagedPriceListDTO {
    private Long totalPages;
    private Long currentPage;
    private Long pageSize;

    private Map<Long, Double> results;
}
