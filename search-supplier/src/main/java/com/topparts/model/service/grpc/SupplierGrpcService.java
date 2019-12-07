package com.topparts.model.service.grpc;

import com.netflix.appinfo.InstanceInfo;
import com.netflix.discovery.EurekaClient;
import com.topparts.grpc.product.Name;
import com.topparts.grpc.product.SupplierServiceGrpc;
import com.topparts.model.entity.Supplier;
import com.topparts.model.service.SupplierService;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import org.springframework.stereotype.Service;

@Service
public class SupplierGrpcService implements SupplierService {
    private static final String GRPC_SERVER_NAME = "supplier-service";
    private static final boolean IS_SECURE = false;

    private SupplierServiceGrpc.SupplierServiceBlockingStub supplierServiceBlockingStub;
    private EurekaClient discoveryClient;

    public SupplierGrpcService(EurekaClient discoveryClient) {
        this.discoveryClient = discoveryClient;
        refreshConnection();
    }

    public void refreshConnection() {
        InstanceInfo instance = discoveryClient.getNextServerFromEureka(GRPC_SERVER_NAME, IS_SECURE);

        System.out.println(instance.getIPAddr() + ":" + instance.getPort());

        ManagedChannel managedChannel = ManagedChannelBuilder
                .forAddress(instance.getIPAddr(), instance.getPort())
                .usePlaintext().build();


        supplierServiceBlockingStub =
                SupplierServiceGrpc.newBlockingStub(managedChannel);
    }
    @Override
    public Supplier getByName(String name) {
        Name grpcName = Name.newBuilder().setName(name).build();

        return convertGrpcSupplierToEntity(supplierServiceBlockingStub.getSupplierByName(grpcName));
    }

    private Supplier convertGrpcSupplierToEntity(com.topparts.grpc.product.Supplier supplier) {
        return Supplier.builder()
                .id(supplier.getId())
                .name(supplier.getName())
                .build();
    }
}
