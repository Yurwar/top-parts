package com.topparts.model.service.grpc;

import com.topparts.grpc.product.Name;
import com.topparts.grpc.product.SupplierServiceGrpc;
import com.topparts.model.entity.Supplier;
import com.topparts.model.service.SupplierService;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

@Service
public class SupplierGrpcService implements SupplierService {
    private static final String GRPC_SERVER_NAME = "supplier-service";
    private static final boolean IS_SECURE = false;

    private SupplierServiceGrpc.SupplierServiceBlockingStub supplierServiceBlockingStub;
    private DiscoveryClient discoveryClient;

    public SupplierGrpcService(DiscoveryClient discoveryClient) {
        this.discoveryClient = discoveryClient;
        refreshConnection();
    }

    public void refreshConnection() {
        ServiceInstance instance = discoveryClient.getInstances(GRPC_SERVER_NAME)
                .stream()
                .findFirst()
                .orElseThrow(NoSuchElementException::new);

        System.out.println(instance.getUri() + ":" + instance.getPort());

        ManagedChannel managedChannel = ManagedChannelBuilder
                .forAddress(instance.getUri().getHost(), instance.getPort())
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
