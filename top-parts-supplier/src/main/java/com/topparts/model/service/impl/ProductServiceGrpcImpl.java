package com.topparts.model.service.impl;

import com.google.protobuf.Empty;
import com.topparts.grpc.product.*;
import com.topparts.model.service.ProductService;
import io.grpc.stub.StreamObserver;
import lombok.extern.slf4j.Slf4j;
import org.lognet.springboot.grpc.GRpcService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@GRpcService
public class ProductServiceGrpcImpl extends TopPartsSupplierProductServiceGrpc.TopPartsSupplierProductServiceImplBase {
    private ProductService productService;

    public ProductServiceGrpcImpl(ProductService productService) {
        this.productService = productService;
    }

    @Override
    public void createProduct(Product request, StreamObserver<Empty> responseObserver) {
        productService.createProduct(convertGrpcProductToEntity(request));
        responseObserver.onNext(Empty.getDefaultInstance());
        responseObserver.onCompleted();
    }

    @Override
    public void getProductById(Id request, StreamObserver<Product> responseObserver) {
        Optional<com.topparts.model.entity.Product> product = productService.getProductById(request.getId());

        if (product.isPresent()) {
            responseObserver.onNext(convertEntityProductToGrpc(product.get()));
        } else {
            responseObserver.onNext(null);
        }

        responseObserver.onCompleted();
    }

    @Override
    public void getAllProducts(Empty request, StreamObserver<Product> responseObserver) {
        log.info("Returning all products");
        System.out.println("Returning all products;");
        productService.getAllProducts().stream()
                .map(this::convertEntityProductToGrpc)
                .forEach(responseObserver::onNext);

        responseObserver.onCompleted();
    }

    @Override
    public void getAllProductsBySearchQuery(Query request, StreamObserver<Product> responseObserver) {
        productService.getAllProductsBySearchQuery(request.getQuery()).stream()
                .map(this::convertEntityProductToGrpc)
                .forEach(responseObserver::onNext);
        responseObserver.onCompleted();
    }

    @Override
    public void updateProduct(UpdateRequest request, StreamObserver<Empty> responseObserver) {
        productService.updateProduct(request.getId().getId(), convertGrpcProductToEntity(request.getProduct()));
        responseObserver.onNext(Empty.getDefaultInstance());
        responseObserver.onCompleted();
    }

    @Override
    public void deleteProduct(Id request, StreamObserver<Empty> responseObserver) {
        productService.deleteProduct(request.getId());
        responseObserver.onNext(Empty.getDefaultInstance());
        responseObserver.onCompleted();
    }

    private List<Product> convertProductListToGrpc(List<com.topparts.model.entity.Product> products) {
        return products.stream()
                .map(this::convertEntityProductToGrpc)
                .collect(Collectors.toList());
    }

    private com.topparts.model.entity.Product convertGrpcProductToEntity(com.topparts.grpc.product.Product product) {
        return com.topparts.model.entity.Product.builder()
                .id(product.getId())
                .name(product.getName())
                .description(product.getDescription())
                .price(product.getPrice())
                .supplier(convertGrpcSupplierToEntity(product.getSupplier()))
                .build();
    }

    private com.topparts.model.entity.Supplier convertGrpcSupplierToEntity(com.topparts.grpc.product.Supplier supplier) {
        return com.topparts.model.entity.Supplier.builder()
                .id(supplier.getId())
                .name(supplier.getName())
                .build();
    }

    private com.topparts.grpc.product.Product convertEntityProductToGrpc(com.topparts.model.entity.Product product) {
        return com.topparts.grpc.product.Product.newBuilder()
                .setId(product.getId())
                .setName(product.getName())
                .setDescription(product.getDescription())
                .setPrice(product.getPrice())
                .setSupplier(convertEntitySupplierToGrpc(product.getSupplier()))
                .build();
    }

    private com.topparts.grpc.product.Supplier convertEntitySupplierToGrpc(com.topparts.model.entity.Supplier supplier) {
        return com.topparts.grpc.product.Supplier.newBuilder()
                .setId(supplier.getId())
                .setName(supplier.getName())
                .build();
    }
}
