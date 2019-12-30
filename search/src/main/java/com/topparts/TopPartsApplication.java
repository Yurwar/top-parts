package com.topparts;

import com.topparts.grpc.product.TopPartsSupplierProductServiceGrpc;
import com.topparts.model.service.grpc.TopPartsGrpcSupplierService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
@EnableDiscoveryClient
@Import(TopPartsSupplierProductServiceGrpc.class)
public class TopPartsApplication {
    public static void main(String[] args) {
        SpringApplication.run(TopPartsApplication.class, args);
    }
}
