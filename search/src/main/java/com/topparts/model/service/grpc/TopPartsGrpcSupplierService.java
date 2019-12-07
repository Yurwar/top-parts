package com.topparts.model.service.grpc;

import com.google.protobuf.Empty;
import com.netflix.appinfo.InstanceInfo;
import com.netflix.discovery.EurekaClient;
import com.topparts.grpc.product.*;
import com.topparts.model.entity.Product;
import com.topparts.model.service.ProductService;
import com.topparts.model.service.grpc.converter.Converter;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class TopPartsGrpcSupplierService implements ProductService {
    private static final String GRPC_SERVER_NAME = "top-parts-supplier-service";
    private static final boolean IS_SECURE = false;

    private TopPartsSupplierProductServiceGrpc.TopPartsSupplierProductServiceBlockingStub
            topPartsSupplierProductServiceBlockingStub;
    private EurekaClient discoveryClient;

    public TopPartsGrpcSupplierService(EurekaClient discoveryClient) {
        this.discoveryClient = discoveryClient;
        refreshConnection();
    }

    public void refreshConnection() {
        InstanceInfo instance = discoveryClient.getNextServerFromEureka(GRPC_SERVER_NAME, IS_SECURE);

        System.out.println(instance.getIPAddr() + ":" + instance.getPort());

        ManagedChannel managedChannel = ManagedChannelBuilder
                .forAddress(instance.getIPAddr(), instance.getPort())
                .usePlaintext().build();


        topPartsSupplierProductServiceBlockingStub =
                TopPartsSupplierProductServiceGrpc.newBlockingStub(managedChannel);
    }

    @Override
    @CachePut(value = "products", key = "#product.getId()")
    public void createProduct(Product product) {
        com.topparts.grpc.product.Product grpcProduct = Converter.convertEntityProductToGrpc(product);
        topPartsSupplierProductServiceBlockingStub.createProduct(grpcProduct);
    }

    @Override
    @Cacheable(value = "products", key = "#id")
    public Optional<Product> getProductById(Long id) {
        Id grpcId = Id.newBuilder().setId(id).build();

        com.topparts.grpc.product.Product product = topPartsSupplierProductServiceBlockingStub.getProductById(grpcId);

        if (Objects.isNull(product)) {
            return Optional.empty();
        } else {
            return Optional.of(Converter.convertGrpcProductToEntity(product));
        }
    }

    @Override
    @Cacheable(value = "products")
    public List<Product> getAllProducts() {
        List<Product> result = new ArrayList<>();
        System.out.println("Getting all products");
        Iterator<com.topparts.grpc.product.Product> products =
                topPartsSupplierProductServiceBlockingStub.getAllProducts(Empty.getDefaultInstance());

        products.forEachRemaining(product -> result.add(Converter.convertGrpcProductToEntity(product)));

        return result;
    }

    @Override
    @Cacheable(value = "products", key = "#query.toLowerCase().trim()")
    public List<Product> getAllProductsBySearchQuery(String query) {
        List<Product> result = new ArrayList<>();
        Query grpcQuery = Query.newBuilder().setQuery(query).build();

        Iterator<com.topparts.grpc.product.Product> products =
                topPartsSupplierProductServiceBlockingStub.getAllProductsBySearchQuery(grpcQuery);

        products.forEachRemaining(product -> result.add(Converter.convertGrpcProductToEntity(product)));

        return result;
    }

    @Override
    @CachePut(value = "products", key = "#id")
    public void updateProduct(Long id, Product product) {
        UpdateRequest updateRequest = UpdateRequest.newBuilder()
                .setId(Id.newBuilder().setId(id).build())
                .setProduct(Converter.convertEntityProductToGrpc(product))
                .build();

        topPartsSupplierProductServiceBlockingStub.updateProduct(updateRequest);
    }

    @Override
    @CacheEvict(value = "products", key = "#id")
    public void deleteProduct(Long id) {
        Id grpcId = Id.newBuilder().setId(id).build();

        topPartsSupplierProductServiceBlockingStub.deleteProduct(grpcId);
    }
}
