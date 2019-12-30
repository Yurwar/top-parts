package com.topparts;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;


@SpringBootApplication
@EnableDiscoveryClient
public class TopPartsSupplierApplication {
    public static void main( String[] args ) {
        SpringApplication.run(TopPartsSupplierApplication.class, args);
    }
}
