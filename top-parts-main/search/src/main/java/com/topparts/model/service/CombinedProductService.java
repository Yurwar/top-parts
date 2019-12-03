package com.topparts.model.service;

import com.topparts.model.entity.Product;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

@Service
@Slf4j
public class CombinedProductService implements ProductService {
    private final ThreadPoolTaskExecutor executor;
    private final ProductService productServiceImpl;
    private final ProductService priceSupplierProductService;
    private final ProductService searchSupplierProductService;

    public CombinedProductService(ProductService productServiceImpl,
                                  ProductService priceSupplierProductService,
                                  ProductService searchSupplierProductService, ThreadPoolTaskExecutor executor) {
        this.productServiceImpl = productServiceImpl;
        this.priceSupplierProductService = priceSupplierProductService;
        this.searchSupplierProductService = searchSupplierProductService;
        this.executor = executor;
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
        long startTime = System.currentTimeMillis();

        Future<List<Product>> productServiceFuture = executor
                .submit(productServiceImpl::getAllProducts);
        Future<List<Product>> priceSupplierFuture = executor
                .submit(priceSupplierProductService::getAllProducts);
        Future<List<Product>> searchSupplierFuture = executor
                .submit(searchSupplierProductService::getAllProducts);

        List<Product> resultProducts = getProductsFromFutures(List.of(productServiceFuture, priceSupplierFuture, searchSupplierFuture));

        long elapsedTime = System.currentTimeMillis() - startTime;
        log.debug("getAllProducts working time - {} millis", elapsedTime);
        return resultProducts;
    }

    @Override
    public List<Product> getAllProductsBySearchQuery(String query) {
        long startTime = System.currentTimeMillis();

        Future<List<Product>> productServiceFuture = executor
                .submit(() -> productServiceImpl.getAllProductsBySearchQuery(query));
        Future<List<Product>> priceSupplierFuture = executor
                .submit(() -> priceSupplierProductService.getAllProductsBySearchQuery(query));
        Future<List<Product>> searchSupplierFuture = executor
                .submit(() -> searchSupplierProductService.getAllProductsBySearchQuery(query));

        List<Product> resultProducts = getProductsFromFutures(List.of(productServiceFuture, priceSupplierFuture, searchSupplierFuture));

        long elapsedTime = System.currentTimeMillis() - startTime;
        log.debug("getAllProductsBySearchQuery working time - {} millis", elapsedTime);
        return resultProducts;
    }

    @Override
    public void updateProduct(Long id, Product product) {
        productServiceImpl.updateProduct(id, product);
    }

    @Override
    public void deleteProduct(Long id) {
        productServiceImpl.deleteProduct(id);
    }

    private List<Product> getProductsFromFutures(List<Future<List<Product>>> productsFuture) {
        List<Product> resultProducts = new ArrayList<>();
        try {
            for (Future<List<Product>> future : productsFuture) {
                resultProducts.addAll(future.get());
            }
        } catch (InterruptedException | ExecutionException e) {
            log.error("Future executing exception", e);
        }
        return resultProducts;
    }
}
