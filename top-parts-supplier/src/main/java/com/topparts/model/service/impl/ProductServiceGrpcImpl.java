package com.topparts.model.service.impl;

import com.google.protobuf.Empty;
import com.topparts.grpc.product.*;
import com.topparts.model.service.ProductService;
import io.grpc.stub.StreamObserver;
import org.lognet.springboot.grpc.GRpcService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@GRpcService
public class ProductServiceGrpcImpl extends ProductServiceGrpc.ProductServiceImplBase {
    private ProductService productService;

    public ProductServiceGrpcImpl(ProductService productService) {
        this.productService = productService;
    }

    @Override
    public void getAllProducts(Empty request, StreamObserver<ProductList> responseObserver) {
        ProductList productList = ProductList
                .newBuilder()
                .addAllProducts(convertProductsToGrpc(productService.getAllProducts()))
                .build();

        responseObserver.onNext(productList);
        responseObserver.onCompleted();
    }

    @Override
    public void getAllProductsBySearchQuery(Query request, StreamObserver<ProductList> responseObserver) {
        ProductList productList = ProductList
                .newBuilder()
                .addAllProducts(convertProductsToGrpc(productService.getAllProductsBySearchQuery(request.getQuery())))
                .build();

        responseObserver.onNext(productList);
        responseObserver.onCompleted();
    }

    private List<Product> convertProductsToGrpc(List<com.topparts.model.entity.Product> products) {
        return products.stream()
                .map(product -> {
                    Supplier supplier = Supplier.newBuilder()
                            .setId(product.getSupplier().getId())
                            .setName(product.getSupplier().getName())
                            .build();

                    return Product.newBuilder()
                            .setId(product.getId())
                            .setName(product.getName())
                            .setDescription(product.getDescription())
                            .setPrice(product.getPrice())
                            .setSupplier(supplier)
                            .build();
                })
                .collect(Collectors.toList());
    }
}
