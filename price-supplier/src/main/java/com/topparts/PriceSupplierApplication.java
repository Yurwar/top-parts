package com.topparts;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class PriceSupplierApplication {
    public static void main(String[] args) {
        SpringApplication.run(PriceSupplierApplication.class, args);
    }
}
