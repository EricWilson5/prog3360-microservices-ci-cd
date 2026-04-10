package com.prog3360.order_service.service;

import com.prog3360.order_service.client.ProductClient;
import com.prog3360.order_service.client.ProductResponse;
import com.prog3360.order_service.entity.Order;
import com.prog3360.order_service.repository.OrderRepository;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class OrderService {

    private static final Logger log = LoggerFactory.getLogger(OrderService.class);

    private final OrderRepository repository;
    private final ProductClient productClient;

    public OrderService(OrderRepository repository, ProductClient productClient) {
        this.repository = repository;
        this.productClient = productClient;
    }

    public Order createOrder(Order order) {

        log.info("Received order request for Product ID: {} with Quantity: {}",
                order.getProductId(), order.getQuantity());

        try {
            ProductResponse product = productClient.getProductById(order.getProductId());

            if (product == null) {
                // WARN: A specific data value wasn't found [cite: 24, 39]
                log.warn("Order failed: Product not found for ID: {}", order.getProductId());
                order.setStatus("FAILED - Product Not Found");
                return repository.save(order);
            }

            Integer stock = product.getQuantity();
            if (stock == null || stock < order.getQuantity()) {
                // WARN: Business rule violation [cite: 24]
                log.warn("Order failed: Insufficient stock for Product {}. Requested: {}, Available: {}",
                        product.getName(), order.getQuantity(), stock);
                order.setStatus("FAILED - Insufficient Stock");
                return repository.save(order);
            }

            order.setTotalPrice(product.getPrice() * order.getQuantity());
            order.setStatus("CREATED");
            Order savedOrder = repository.save(order);

            // INFO: Success event with data [cite: 35]
            log.info("Order successfully created! Order ID: {}, Total Price: ${}",
                    savedOrder.getId(), savedOrder.getTotalPrice());
            return savedOrder;

        } catch (Exception e) {
            // ERROR: Technical failure like communication error [cite: 25, 37]
            log.error("Critical error during order creation for Product ID: {}. Error: {}",
                    order.getProductId(), e.getMessage());
            order.setStatus("FAILED - Communication Error");
            return repository.save(order);
        }
    }

    public List<Order> getAllOrders() {
        return repository.findAll();
    }

    public Order getOrderById(Long id) {
        return repository.findById(id).orElse(null);
    }

    }
