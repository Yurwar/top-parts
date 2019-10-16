package com.topparts.model.service;

import com.topparts.model.entity.Product;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class CombinedProductService implements ProductService {
    private ProductService productServiceImpl;
    private ProductService priceSupplierProductService;
    private ProductService searchSupplierProductService;

    public CombinedProductService(ProductService productServiceImpl,
                                  ProductService priceSupplierProductService,
                                  ProductService searchSupplierProductService) {
        this.productServiceImpl = productServiceImpl;
        this.priceSupplierProductService = priceSupplierProductService;
        this.searchSupplierProductService = searchSupplierProductService;
    }

    @Override
    public void createProduct(Product product) {
        productServiceImpl.createProduct(product);
    }

    @Override
    public Optional<Product> getProductById(Long id) {
        return productServiceImpl.getProductById(id);
    }

    @Override
    public List<Product> getAllProducts() {
        List<Product> resultProducts = new ArrayList<>();
        resultProducts.addAll(productServiceImpl.getAllProducts());
        resultProducts.addAll(priceSupplierProductService.getAllProducts());
        resultProducts.addAll(searchSupplierProductService.getAllProducts());
        return resultProducts;
    }

    @Override
    public List<Product> getAllProductsBySearchQuery(String query) {
        List<Product> resultProducts = new ArrayList<>();
        resultProducts.addAll(productServiceImpl.getAllProductsBySearchQuery(query));
        resultProducts.addAll(priceSupplierProductService.getAllProductsBySearchQuery(query));
        resultProducts.addAll(searchSupplierProductService.getAllProductsBySearchQuery(query));
        return resultProducts;
    }

    @Override
    public List<Product> getAllProductsByCategory(Long id) {
        return productServiceImpl.getAllProducts();
    }

    @Override
    public void updateProduct(Long id, Product product) {
        productServiceImpl.updateProduct(id, product);
    }

    @Override
    public void deleteProduct(Long id) {
        productServiceImpl.deleteProduct(id);
    }
}
