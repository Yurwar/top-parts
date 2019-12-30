package com.topparts.model.resolver.field;

import com.coxautodev.graphql.tools.GraphQLResolver;
import com.topparts.model.Order;
import com.topparts.model.User;
import com.topparts.model.service.ProductService;
import com.topparts.model.service.UserService;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Component
public class OrderFieldResolver implements GraphQLResolver<Order> {
    private UserService userService;

    public OrderFieldResolver(UserService userService) {
        this.userService = userService;
    }

    public User getUser(Order order) {
        return userService.getUserById(order.getUserId());
    }
}
