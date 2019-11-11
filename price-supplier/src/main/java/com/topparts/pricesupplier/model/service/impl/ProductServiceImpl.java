package com.topparts.pricesupplier.model.service.impl;

import com.topparts.pricesupplier.model.entity.PriceListRow;
import com.topparts.pricesupplier.model.entity.Product;
import com.topparts.pricesupplier.model.repository.ProductRepository;
import com.topparts.pricesupplier.model.service.ProductService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.util.List;
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
    public  Page<PriceListRow> getPriceList(Pageable pageable) {
        log.trace("Trying to get price list");
        Page<PriceListRow> result;
        Page<Product> page = productRepository.findAll(pageable);

//        Map<Long, Double> priceListMap = productRepository.findAll(pageable)
//                .stream()
//                .collect(Collectors.toMap(Product::getId, Product::getPrice));

        List<PriceListRow> priceListRows = page.get()
                .map(PriceListRow::new)
                .collect(Collectors.toList());

        log.trace("Return price list map");

        System.out.println("Page size: " + pageable.getPageSize());
        System.out.println("Page number: " + pageable.getPageNumber());


        result = new PageImpl<>(priceListRows, pageable, page.getTotalPages());
        return result;
    }

    @Override
    public Product getProductById(Long id) {
        log.trace("Trying to get product by id: {}", id);
        Product product = productRepository.findById(id).orElseThrow(NoSuchElementException::new);
        log.trace("Return product by id");
        return product;
    }
}
