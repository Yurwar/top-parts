package com.topparts.model.service;

import com.topparts.model.Product;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.net.MalformedURLException;
import java.util.List;
import java.util.NoSuchElementException;

@Service
public class ProductService {

    private String searchServiceUrl;
    private final static String PRODUCTS_API_PATTERN = "api/product";
    private final static String SEARCH_QUERY_PARAM_NAME = "searchQuery";

    private static final String SEARCH_SERVICE_NAME = "search-service";
    private static final boolean IS_SECURE = false;
    private DiscoveryClient discoveryClient;

    private RestTemplate restTemplate;

    public ProductService(RestTemplateBuilder restTemplateBuilder, DiscoveryClient discoveryClient) {
        this.restTemplate = restTemplateBuilder.build();
        this.discoveryClient = discoveryClient;
        refreshConnection();
    }

    public void refreshConnection() {
        ServiceInstance instance = discoveryClient.getInstances(SEARCH_QUERY_PARAM_NAME)
                .stream()
                .findFirst()
                .orElseThrow(NoSuchElementException::new);

        try {
            searchServiceUrl = instance.getUri().toURL().toString();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
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
