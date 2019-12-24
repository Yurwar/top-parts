package com.topparts.controller;

import com.topparts.model.dto.OrderDto;
import com.topparts.model.service.OrderService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping
    public OrderDto createOrder(@RequestBody OrderDto orderDto) {
        return new OrderDto(orderService.createOrder(orderDto));
    }

    @GetMapping
    public List<OrderDto> getAllOrders() {
        return orderService.getAllOrders()
                .stream()
                .map(OrderDto::new)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public OrderDto getOrderById(@PathVariable Long id) {
        return new OrderDto(orderService.getOrderById(id));
    }

    @GetMapping("/users/{id}")
    public List<OrderDto> getAllOrdersByUserId(@PathVariable Long id) {
        return orderService.getAllOrdersByUserId(id)
                .stream()
                .map(OrderDto::new)
                .collect(Collectors.toList());
    }

    @GetMapping("/suppliers/{id}")
    public List<OrderDto> getAllOrdersBySupplierId(@PathVariable Long id) {
        return orderService.getAllOrdersBySupplierId(id)
                .stream()
                .map(OrderDto::new)
                .collect(Collectors.toList());
    }
}
