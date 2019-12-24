package com.topparts.model.resolver.mutation;

import com.coxautodev.graphql.tools.GraphQLMutationResolver;
import com.topparts.model.Order;
import com.topparts.model.input.OrderInput;
import com.topparts.model.service.OrderService;
import org.springframework.stereotype.Component;

@Component
public class OrderMutationResolver implements GraphQLMutationResolver {
    private OrderService orderService;

    public OrderMutationResolver(OrderService orderService) {
        this.orderService = orderService;
    }

    public Order createOrder(OrderInput orderInput) {
        return orderService.createOrder(orderInput);
    }

}
