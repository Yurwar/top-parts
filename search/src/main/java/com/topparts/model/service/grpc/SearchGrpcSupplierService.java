package com.topparts.model.service.grpc;

import com.google.protobuf.Empty;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.topparts.grpc.product.Query;
import com.topparts.grpc.product.SearchSupplierProductServiceGrpc;
import com.topparts.model.entity.Product;
import com.topparts.model.service.ProductService;
import com.topparts.model.service.grpc.converter.Converter;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.stereotype.Service;

import java.util.*;


@Service
public class SearchGrpcSupplierService implements ProductService {
    private static final String GRPC_SERVER_NAME = "search-supplier-service";
    private static final boolean IS_SECURE = false;

    private SearchSupplierProductServiceGrpc.SearchSupplierProductServiceBlockingStub
            searchSupplierProductServiceBlockingStub;
    private DiscoveryClient discoveryClient;

    public SearchGrpcSupplierService(DiscoveryClient discoveryClient) {
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


        searchSupplierProductServiceBlockingStub =
                SearchSupplierProductServiceGrpc.newBlockingStub(managedChannel);
    }

    @Override
    public void createProduct(Product product) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Optional<Product> getProductById(Long id) {
        throw new UnsupportedOperationException();
    }

    @Override
    @Cacheable(value = "searchSupplierProducts")
    @HystrixCommand(fallbackMethod = "defaultGetAllProducts")
    public List<Product> getAllProducts() {
        refreshConnection();
        List<Product> result = new ArrayList<>();
        System.out.println("Getting all products");
        Iterator<com.topparts.grpc.product.Product> products =
                searchSupplierProductServiceBlockingStub.getAllProducts(Empty.getDefaultInstance());

        products.forEachRemaining(product -> result.add(Converter.convertGrpcProductToEntity(product)));
        return result;
    }

    @Override
    @Cacheable(value = "searchSupplierProducts", key = "#query.toLowerCase().trim()")
    @HystrixCommand(fallbackMethod = "defaultGetAllProductsBySearchQuery")
    public List<Product> getAllProductsBySearchQuery(String query) {
        refreshConnection();
        List<Product> result = new ArrayList<>();
        Query grpcQuery = Query.newBuilder().setQuery(query).build();

        Iterator<com.topparts.grpc.product.Product> products =
                searchSupplierProductServiceBlockingStub.getAllProductsBySearchQuery(grpcQuery);

        products.forEachRemaining(product -> result.add(Converter.convertGrpcProductToEntity(product)));

        return result;
    }

    @Override
    public void updateProduct(Long id, Product product) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void deleteProduct(Long id) {
        throw new UnsupportedOperationException();
    }

    public List<Product> defaultGetAllProducts() {
        return new ArrayList<>();
    }

    public List<Product> defaultGetAllProductsBySearchQuery(String query) {
        return new ArrayList<>();
    }
}
