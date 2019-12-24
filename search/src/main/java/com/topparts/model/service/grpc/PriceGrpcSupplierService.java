package com.topparts.model.service.grpc;

import com.google.protobuf.Empty;
import com.netflix.appinfo.InstanceInfo;
import com.netflix.discovery.EurekaClient;
import com.topparts.grpc.product.PriceSupplierProductServiceGrpc;
import com.topparts.grpc.product.Query;
import com.topparts.model.entity.Product;
import com.topparts.model.service.ProductService;
import com.topparts.model.service.grpc.converter.Converter;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

@Service
public class PriceGrpcSupplierService implements ProductService {
    private static final String GRPC_SERVER_NAME = "price-supplier-service";
    private static final boolean IS_SECURE = false;

    private PriceSupplierProductServiceGrpc.PriceSupplierProductServiceBlockingStub
            priceSupplierProductServiceBlockingStub;
    private EurekaClient discoveryClient;

    public PriceGrpcSupplierService(EurekaClient discoveryClient) {
        this.discoveryClient = discoveryClient;
        refreshConnection();
    }

    public void refreshConnection() {
        InstanceInfo instance = discoveryClient.getNextServerFromEureka(GRPC_SERVER_NAME, IS_SECURE);

        System.out.println(instance.getIPAddr() + ":" + instance.getPort());

        ManagedChannel managedChannel = ManagedChannelBuilder
                .forAddress(instance.getIPAddr(), instance.getPort())
                .usePlaintext().build();


        priceSupplierProductServiceBlockingStub =
                PriceSupplierProductServiceGrpc.newBlockingStub(managedChannel);
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
    @Cacheable(value = "priceSupplierProducts")
    public List<Product> getAllProducts() {
        List<Product> result = new ArrayList<>();
        System.out.println("Getting all products");
        Iterator<com.topparts.grpc.product.Product> products =
                priceSupplierProductServiceBlockingStub.getAllProducts(Empty.getDefaultInstance());

        products.forEachRemaining(product -> result.add(Converter.convertGrpcProductToEntity(product)));
        return result;
    }

    @Override
    @Cacheable(value = "priceSupplierProducts", key = "#query.toLowerCase().trim()")
    public List<Product> getAllProductsBySearchQuery(String query) {
        List<Product> result = new ArrayList<>();
        Query grpcQuery = Query.newBuilder().setQuery(query).build();

        Iterator<com.topparts.grpc.product.Product> products =
                priceSupplierProductServiceBlockingStub.getAllProductsBySearchQuery(grpcQuery);

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
}