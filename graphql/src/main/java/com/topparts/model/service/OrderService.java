package com.topparts.model.service;

import com.netflix.appinfo.InstanceInfo;
import com.netflix.discovery.EurekaClient;
import com.topparts.model.Order;
import com.topparts.model.input.OrderInput;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
public class OrderService {
    private String purchaseServiceUrl;
    private final static String ORDERS_API_PATTERN = "api/orders";
    private final static String USERS_PATH_PATTERN = "users";
    private final static String SUPPLIERS_PATH_PATTERN = "suppliers";

    private static final String PURCHASE_SERVICE_NAME = "purchase-service";
    private static final boolean IS_SECURE = false;
    private EurekaClient discoveryClient;

    private RestTemplate restTemplate;

    public OrderService(RestTemplateBuilder restTemplateBuilder, EurekaClient discoveryClient) {
        this.restTemplate = restTemplateBuilder.build();
        this.discoveryClient = discoveryClient;
        refreshConnection();
    }

    public void refreshConnection() {
        InstanceInfo instance = discoveryClient.getNextServerFromEureka(PURCHASE_SERVICE_NAME, IS_SECURE);
        purchaseServiceUrl = instance.getHomePageUrl();
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
