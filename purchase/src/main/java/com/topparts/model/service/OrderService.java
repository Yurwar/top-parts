package com.topparts.model.service;

import com.topparts.model.dto.OrderDto;
import com.topparts.model.entity.Order;
import com.topparts.model.entity.OrderEntry;
import com.topparts.model.repository.OrderRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class OrderService {
    private OrderRepository orderRepository;

    public OrderService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    public void createOrder(OrderDto orderDto) {
        Order order = Order.builder()
                .userId(orderDto.getUserId())
                .dateOfPurchase(
                        Objects.isNull(orderDto.getDateOfPurchase())
                                ?
                                LocalDateTime.now()
                                :
                                orderDto.getDateOfPurchase())
                .build();

        Set<OrderEntry> entries = orderDto
                .getEntries()
                .stream()
                .map(orderEntryDto -> OrderEntry.builder()
                        .productId(orderEntryDto.getProductId())
                        .supplierId(orderEntryDto.getSupplierId())
                        .quantity(orderEntryDto.getQuantity())
                        .order(order)
                        .build())
                .collect(Collectors.toSet());

        order.setEntries(entries);

        orderRepository.save(order);
    }

    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    public Order getOrderById(Long id) {
        return orderRepository.findById(id)
                .orElseThrow(NoSuchElementException::new);
    }

    public List<Order> getAllOrdersByUserId(Long id) {
        return orderRepository.findAllByUserId(id);
    }
}
