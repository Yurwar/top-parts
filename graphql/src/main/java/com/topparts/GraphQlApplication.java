package com.topparts;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class GraphQlApplication {
    public static void main( String[] args ) {
        SpringApplication.run(GraphQlApplication.class, args);
    }
}
