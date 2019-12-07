package com.topparts.model.service.grpc.converter;

import com.topparts.model.entity.Product;
import com.topparts.model.entity.Supplier;

public class Converter {
    public static Product convertGrpcProductToEntity(com.topparts.grpc.product.Product product) {
        return Product.builder()
                .id(product.getId())
                .name(product.getName())
                .description(product.getDescription())
                .price(product.getPrice())
                .supplier(convertGrpcSupplierToEntity(product.getSupplier()))
                .build();
    }

    public static Supplier convertGrpcSupplierToEntity(com.topparts.grpc.product.Supplier supplier) {
        return Supplier.builder()
                .id(supplier.getId())
                .name(supplier.getName())
                .build();
    }

    public static com.topparts.grpc.product.Product convertEntityProductToGrpc(Product product) {
        return com.topparts.grpc.product.Product.newBuilder()
                .setId(product.getId())
                .setName(product.getName())
                .setDescription(product.getDescription())
                .setPrice(product.getPrice())
                .setSupplier(convertEntitySupplierToGrpc(product.getSupplier()))
                .build();
    }

    public static com.topparts.grpc.product.Supplier convertEntitySupplierToGrpc(Supplier supplier) {
        return com.topparts.grpc.product.Supplier.newBuilder()
                .setId(supplier.getId())
                .setName(supplier.getName())
                .build();
    }
}
