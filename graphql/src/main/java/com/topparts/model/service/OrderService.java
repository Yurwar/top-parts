package com.topparts.model.service;

import com.topparts.model.Order;
import com.topparts.model.input.OrderInput;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.net.MalformedURLException;
import java.util.List;
import java.util.NoSuchElementException;

@Service
public class OrderService {
    private String purchaseServiceUrl;
    private final static String ORDERS_API_PATTERN = "api/orders";
    private final static String USERS_PATH_PATTERN = "users";
    private final static String SUPPLIERS_PATH_PATTERN = "suppliers";

    private static final String PURCHASE_SERVICE_NAME = "purchase-service";
    private static final boolean IS_SECURE = false;
    private DiscoveryClient discoveryClient;

    private RestTemplate restTemplate;

    public OrderService(RestTemplateBuilder restTemplateBuilder, DiscoveryClient discoveryClient) {
        this.restTemplate = restTemplateBuilder.build();
        this.discoveryClient = discoveryClient;
        refreshConnection();
    }

    public void refreshConnection() {
        ServiceInstance instance = discoveryClient.getInstances(PURCHASE_SERVICE_NAME)
                .stream()
                .findFirst()
                .orElseThrow(NoSuchElementException::new);

        try {
            purchaseServiceUrl = instance.getUri().toURL().toString();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    public List<Order> getAllOrders() {
        String queryUrl = purchaseServiceUrl + "/" + ORDERS_API_PATTERN;

        return getCollectionOfOrdersByQuery(queryUrl);
    }

    public Order getOrderById(Long id) {
        String queryUrl = purchaseServiceUrl + "/" + ORDERS_API_PATTERN + "/" + id;
        return restTemplate.getForObject(queryUrl, Order.class);
    }

    public List<Order> getOrdersByUserId(Long id) {
        String queryUrl = purchaseServiceUrl + "/" + ORDERS_API_PATTERN + "/" + USERS_PATH_PATTERN + "/" + id;

        return getCollectionOfOrdersByQuery(queryUrl);
    }

    public List<Order> getOrdersBySupplierId(Long id) {
        String queryUrl = purchaseServiceUrl + "/" + ORDERS_API_PATTERN + "/" + SUPPLIERS_PATH_PATTERN + "/" + id;

        return getCollectionOfOrdersByQuery(queryUrl);
    }

    public Order createOrder(OrderInput orderInput) {
        String queryUrl = purchaseServiceUrl + "/" + ORDERS_API_PATTERN;
        return restTemplate
                .postForEntity(queryUrl, orderInput, Order.class)
                .getBody();
    }

    private List<Order> getCollectionOfOrdersByQuery(String query) {
        return restTemplate.exchange(query,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<Order>>() {
                })
                .getBody();
    }
}
