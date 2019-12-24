package com.topparts.model.resolver.query;

import com.coxautodev.graphql.tools.GraphQLQueryResolver;
import com.topparts.model.Product;
import com.topparts.model.service.ProductService;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ProductQueryResolver implements GraphQLQueryResolver {
    private ProductService productService;

    public ProductQueryResolver(ProductService productService) {
        this.productService = productService;
    }

    public List<Product> getProducts() {
        return productService.getAllProducts();
    }

    public Product getProductById(final long id) {
        return productService.getProductById(id);
    }

    public List<Product> getProductsByQuery(final String query) {
        return productService.getAllProductsBySearchQuery(query);
    }
}
