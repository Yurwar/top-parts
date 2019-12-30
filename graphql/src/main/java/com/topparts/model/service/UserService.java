package com.topparts.model.service;

import com.topparts.model.User;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.net.MalformedURLException;
import java.util.NoSuchElementException;

@Service
public class UserService {
    private String authorizationServiceUrl;
    private final static String USERS_API_PATTERN = "api/users";

    private static final String AUTHORIZATION_SERVICE_NAME = "authorization-service";
    private static final boolean IS_SECURE = false;
    private DiscoveryClient discoveryClient;

    RestTemplate restTemplate;

    public UserService(RestTemplateBuilder restTemplateBuilder, DiscoveryClient discoveryClient) {
        this.restTemplate = restTemplateBuilder.build();
        this.discoveryClient = discoveryClient;
        refreshConnection();
    }

    public void refreshConnection() {
        ServiceInstance instance = discoveryClient.getInstances(AUTHORIZATION_SERVICE_NAME)
                .stream()
                .findFirst()
                .orElseThrow(NoSuchElementException::new);

        try {
            authorizationServiceUrl = instance.getUri().toURL().toString();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    public User getUserById(Long id) {
        String queryUrl = authorizationServiceUrl + "/" + USERS_API_PATTERN + "/" + id;
        return restTemplate.getForObject(queryUrl, User.class);
    }
}
