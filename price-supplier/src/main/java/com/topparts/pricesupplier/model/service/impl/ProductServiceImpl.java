package com.topparts.pricesupplier.model.service.impl;

import com.topparts.pricesupplier.model.dto.PagedPriceListDTO;
import com.topparts.pricesupplier.model.entity.Product;
import com.topparts.pricesupplier.model.repository.ProductRepository;
import com.topparts.pricesupplier.model.service.ProductService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
@Slf4j
public class ProductServiceImpl implements ProductService {
    private ProductRepository productRepository;

    public ProductServiceImpl(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public PagedPriceListDTO getPriceList(Pageable pageable) {
        log.trace("Trying to get price list");

        Page<Product> page = productRepository.findAll(pageable);

        Map<Long, Double> priceListMap = page.get()
                .collect(Collectors.toMap(Product::getId, Product::getPrice));

        log.trace("Return price list map");

        return PagedPriceListDTO.builder()
                .currentPage((long) pageable.getPageNumber())
                .totalPages((long) page.getTotalPages())
                .pageSize((long) pageable.getPageSize())
                .results(priceListMap)
                .build();
    }

    @Override
    public Product getProductById(Long id) {
        log.trace("Trying to get product by id: {}", id);
        Product product = productRepository.findById(id).orElseThrow(NoSuchElementException::new);
        log.trace("Return product by id");
        return product;
    }
}
