package com.topparts.model.service.impl;

import com.topparts.grpc.product.Name;
import com.topparts.grpc.product.Supplier;
import com.topparts.grpc.product.SupplierServiceGrpc;
import com.topparts.model.service.SupplierService;
import io.grpc.stub.StreamObserver;
import org.lognet.springboot.grpc.GRpcService;

@GRpcService
public class SupplierServiceGrpcImpl extends SupplierServiceGrpc.SupplierServiceImplBase {
    private final SupplierService supplierService;

    public SupplierServiceGrpcImpl(SupplierService supplierService) {
        this.supplierService = supplierService;
    }

    @Override
    public void getSupplierByName(Name request, StreamObserver<Supplier> responseObserver) {
        Supplier supplier = convertEntitySupplierToGrpc(supplierService.getByName(request.getName()));

        responseObserver.onNext(supplier);
        responseObserver.onCompleted();
    }

    private Supplier convertEntitySupplierToGrpc(com.topparts.model.entity.Supplier supplier) {
        return Supplier.newBuilder()
                .setId(supplier.getId())
                .setName(supplier.getName())
                .build();
    }
}
