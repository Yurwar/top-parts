package com.topparts.model.service.grpc;

import com.google.protobuf.Empty;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.topparts.grpc.product.Id;
import com.topparts.grpc.product.Query;
import com.topparts.grpc.product.TopPartsSupplierProductServiceGrpc;
import com.topparts.grpc.product.UpdateRequest;
import com.topparts.model.entity.Product;
import com.topparts.model.service.ProductService;
import com.topparts.model.service.grpc.converter.Converter;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class TopPartsGrpcSupplierService implements ProductService {
    private static final String GRPC_SERVER_NAME = "top-parts-supplier-service";
    private static final boolean IS_SECURE = false;

    private TopPartsSupplierProductServiceGrpc.TopPartsSupplierProductServiceBlockingStub
            topPartsSupplierProductServiceBlockingStub;
    private DiscoveryClient discoveryClient;

    public TopPartsGrpcSupplierService(DiscoveryClient discoveryClient) {
        this.discoveryClient = discoveryClient;
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


        topPartsSupplierProductServiceBlockingStub =
                TopPartsSupplierProductServiceGrpc.newBlockingStub(managedChannel);
    }

    @Override
    @HystrixCommand(fallbackMethod = "defaultCreateProduct")
    @CachePut(value = "products", key = "#product.getId()")
    public void createProduct(Product product) {
        refreshConnection();
        com.topparts.grpc.product.Product grpcProduct = Converter.convertEntityProductToGrpc(product);
        topPartsSupplierProductServiceBlockingStub.createProduct(grpcProduct);
    }

    @Override
    @Cacheable(value = "products", key = "#id")
    @HystrixCommand(fallbackMethod = "defaultGetProductById")
    public Optional<Product> getProductById(Long id) {
        refreshConnection();
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
    @HystrixCommand(fallbackMethod = "defaultGetAllProducts")
    public List<Product> getAllProducts() {
        refreshConnection();
        List<Product> result = new ArrayList<>();
        System.out.println("Getting all products");
        Iterator<com.topparts.grpc.product.Product> products =
                topPartsSupplierProductServiceBlockingStub.getAllProducts(Empty.getDefaultInstance());

        products.forEachRemaining(product -> result.add(Converter.convertGrpcProductToEntity(product)));

        return result;
    }

    @Override
    @Cacheable(value = "products", key = "#query.toLowerCase().trim()")
    @HystrixCommand(fallbackMethod = "defaultGetAllProductsBySearchQuery")
    public List<Product> getAllProductsBySearchQuery(String query) {
        refreshConnection();
        List<Product> result = new ArrayList<>();
        Query grpcQuery = Query.newBuilder().setQuery(query).build();

        Iterator<com.topparts.grpc.product.Product> products =
                topPartsSupplierProductServiceBlockingStub.getAllProductsBySearchQuery(grpcQuery);

        products.forEachRemaining(product -> result.add(Converter.convertGrpcProductToEntity(product)));

        return result;
    }

    @Override
    @CachePut(value = "products", key = "#id")
    @HystrixCommand(fallbackMethod = "defaultUpdateProduct")
    public void updateProduct(Long id, Product product) {
        refreshConnection();
        UpdateRequest updateRequest = UpdateRequest.newBuilder()
                .setId(Id.newBuilder().setId(id).build())
                .setProduct(Converter.convertEntityProductToGrpc(product))
                .build();

        topPartsSupplierProductServiceBlockingStub.updateProduct(updateRequest);
    }

    @Override
    @CacheEvict(value = "products", key = "#id")
    @HystrixCommand(fallbackMethod = "defaultDeleteProduct")
    public void deleteProduct(Long id) {
        refreshConnection();
        Id grpcId = Id.newBuilder().setId(id).build();

        topPartsSupplierProductServiceBlockingStub.deleteProduct(grpcId);
    }

    public void defaultCreateProduct(Product product) {
    }

    public Optional<Product> defaultGetProductById(Long id) {
        return Optional.empty();
    }

    public List<Product> defaultGetAllProducts() {
        return new ArrayList<>();
    }

    public List<Product> defaultGetAllProductsBySearchQuery(String query) {
        return new ArrayList<>();
    }

    public void defaultUpdateProduct(Long id, Product product) {
    }

    public void defaultDeleteProduct(Long id) {
    }

}
