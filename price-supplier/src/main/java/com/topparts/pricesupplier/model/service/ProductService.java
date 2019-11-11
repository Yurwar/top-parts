package com.topparts.pricesupplier.model.service;

import com.topparts.pricesupplier.model.dto.PagedPriceListDTO;
import com.topparts.pricesupplier.model.entity.Product;
import org.springframework.data.domain.Pageable;

public interface ProductService {
    PagedPriceListDTO getPriceList(Pageable pageable);

    Product getProductById(Long id);
}
