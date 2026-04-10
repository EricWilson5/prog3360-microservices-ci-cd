package com.prog3360.order_service.controller;

import com.prog3360.order_service.entity.Order;
import com.prog3360.order_service.service.OrderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
public class OrderController {



    private static final Logger logger = LoggerFactory.getLogger(OrderController.class);
    private final OrderService service;

    public OrderController(OrderService service) {
        this.service = service;
    }

    @GetMapping
    public List<Order> getAllOrders() {
        List<Order> orders = service.getAllOrders();
        // Logic simplified: Always apply discount if applicable for the assignment demo
        orders.forEach(this::applyDiscount);
        return orders;
    }

    @GetMapping("/{id}")
    public Order getOrderById(@PathVariable Long id) {
        Order order = service.getOrderById(id);
        if (order != null) {
            applyDiscount(order);
        }
        return order;
    }

    @PostMapping
    public Order createOrder(@RequestBody Order order) {
        Order savedOrder = service.createOrder(order);

        // Log confirmation (replaces the notification flag logic)
        logger.info("ORDER CREATED: Order #{} confirmed. Product ID: {}, Quantity: {}, Total: ${}",
                savedOrder.getId(), savedOrder.getProductId(), savedOrder.getQuantity(), savedOrder.getTotalPrice());

        return savedOrder;
    }

    private void applyDiscount(Order order) {
        if (order.getQuantity() != null && order.getQuantity() > 5) {
            if (order.getTotalPrice() != null) {
                order.setTotalPrice(order.getTotalPrice() * 0.85);
            }
        }
    }
}