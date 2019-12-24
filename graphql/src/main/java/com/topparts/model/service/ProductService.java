package com.topparts.model.service;

import com.netflix.appinfo.InstanceInfo;
import com.netflix.discovery.EurekaClient;
import com.topparts.model.Product;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
public class ProductService {

    private String searchServiceUrl;
    private final static String PRODUCTS_API_PATTERN = "api/product";
    private final static String SEARCH_QUERY_PARAM_NAME = "searchQuery";

    private static final String SEARCH_SERVICE_NAME = "search-service";
    private static final boolean IS_SECURE = false;
    private EurekaClient discoveryClient;

    private RestTemplate restTemplate;

    public ProductService(RestTemplateBuilder restTemplateBuilder, EurekaClient discoveryClient) {
        this.restTemplate = restTemplateBuilder.build();
        this.discoveryClient = discoveryClient;
        refreshConnection();
    }

    public void refreshConnection() {
        InstanceInfo instance = discoveryClient.getNextServerFromEureka(SEARCH_SERVICE_NAME, IS_SECURE);
        searchServiceUrl = instance.getHomePageUrl();
    }

    public Product getProductById(Long id) {
        String queryUrl = searchServiceUrl + "/" + PRODUCTS_API_PATTERN + "/" + id;
        return restTemplate.getForObject(queryUrl, Product.class);
    }

    public List<Product> getAllProducts() {
        String queryUrl = searchServiceUrl + "/" + PRODUCTS_API_PATTERN;

        ResponseEntity<List<Product>> responseEntity = restTemplate.exchange(queryUrl,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<>() {
                });

        return responseEntity.getBody();
    }

    public List<Product> getAllProductsBySearchQuery(String query) {
        String queryUrl =
                searchServiceUrl
                + "/"
                + PRODUCTS_API_PATTERN
                + "?"
                + SEARCH_QUERY_PARAM_NAME
                + "="
                + query;

        ResponseEntity<List<Product>> responseEntity = restTemplate.exchange(queryUrl,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<>() {
                });

        return responseEntity.getBody();
    }

    private List<Product> getCollectionOfProductsByQuery(String query) {
        return restTemplate.exchange(query,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<Product>>() {
                })
                .getBody();
    }
}
