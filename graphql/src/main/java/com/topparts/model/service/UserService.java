package com.topparts.model.service;

import com.netflix.appinfo.InstanceInfo;
import com.netflix.discovery.EurekaClient;
import com.topparts.model.User;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class UserService {
    private String authorizationServiceUrl;
    private final static String USERS_API_PATTERN = "api/users";

    private static final String AUTHORIZATION_SERVICE_NAME = "authorization-service";
    private static final boolean IS_SECURE = false;
    private EurekaClient discoveryClient;

    RestTemplate restTemplate;

    public UserService(RestTemplateBuilder restTemplateBuilder, EurekaClient discoveryClient) {
        this.restTemplate = restTemplateBuilder.build();
        this.discoveryClient = discoveryClient;
        refreshConnection();
    }

    public void refreshConnection() {
        InstanceInfo instance = discoveryClient.getNextServerFromEureka(AUTHORIZATION_SERVICE_NAME, IS_SECURE);
        authorizationServiceUrl = instance.getHomePageUrl();
    }

    public User getUserById(Long id) {
        String queryUrl = authorizationServiceUrl + "/" + USERS_API_PATTERN + "/" + id;
        return restTemplate.getForObject(queryUrl, User.class);
    }
}
