package com.topparts.model.service.impl;

import com.google.protobuf.Empty;
import com.topparts.grpc.product.Product;
import com.topparts.grpc.product.Query;
import com.topparts.grpc.product.SearchSupplierProductServiceGrpc;
import com.topparts.model.service.ProductService;
import com.topparts.model.service.impl.converter.Converter;
import io.grpc.stub.StreamObserver;
import org.lognet.springboot.grpc.GRpcService;

@GRpcService
public class ProductServiceGrpcImpl extends SearchSupplierProductServiceGrpc.SearchSupplierProductServiceImplBase {

    private final ProductService productService;

    public ProductServiceGrpcImpl(ProductService productService) {
        this.productService = productService;
    }

    @Override
    public void getAllProducts(Empty request, StreamObserver<Product> responseObserver) {
        productService.getAllProducts().stream()
                .map(Converter::convertEntityProductToGrpc)
                .forEach(responseObserver::onNext);

        responseObserver.onCompleted();
    }

    @Override
    public void getAllProductsBySearchQuery(Query request, StreamObserver<Product> responseObserver) {
        productService.getAllProductsBySearchQuery(request.getQuery()).stream()
                .map(Converter::convertEntityProductToGrpc)
                .forEach(responseObserver::onNext);
        responseObserver.onCompleted();
    }
}
