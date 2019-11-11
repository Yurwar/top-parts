package com.topparts.pricesupplier.model.service;

import com.topparts.pricesupplier.model.entity.PriceListRow;
import com.topparts.pricesupplier.model.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Map;

public interface ProductService {
    Page<PriceListRow> getPriceList(Pageable pageable);

    Product getProductById(Long id);
}
