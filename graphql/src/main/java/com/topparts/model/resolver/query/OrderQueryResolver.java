package com.topparts.model.resolver.query;

import com.coxautodev.graphql.tools.GraphQLQueryResolver;
import com.topparts.model.Order;
import com.topparts.model.service.OrderService;
import com.topparts.model.service.ProductService;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class OrderQueryResolver implements GraphQLQueryResolver {
    private OrderService orderService;

    public OrderQueryResolver(OrderService orderService) {
        this.orderService = orderService;
    }

    public List<Order> getOrders() {
        return orderService.getAllOrders();
    }

    public Order getOrderById(final long id) {
        return orderService.getOrderById(id);
    }

    public List<Order> getOrdersByUserId(final long id) {
        return orderService.getOrdersByUserId(id);
    }

    public List<Order> getOrdersBySupplierId(final long id) {
        return orderService.getOrdersBySupplierId(id);
    }
}
