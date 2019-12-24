package com.topparts.model.resolver.field;

import com.coxautodev.graphql.tools.GraphQLResolver;
import com.topparts.model.OrderEntry;
import com.topparts.model.Product;
import com.topparts.model.Supplier;
import com.topparts.model.service.ProductService;
import org.springframework.stereotype.Component;

@Component
public class OrderEntryFieldResolver implements GraphQLResolver<OrderEntry> {

    private ProductService productService;

    public OrderEntryFieldResolver(ProductService productService) {
        this.productService = productService;
    }

    public Product getProduct(OrderEntry orderEntry) {
        return productService.getProductById(orderEntry.getProductId());
    }

    public Supplier getSupplier(OrderEntry orderEntry) {
        return getProduct(orderEntry).getSupplier();
    }
}
