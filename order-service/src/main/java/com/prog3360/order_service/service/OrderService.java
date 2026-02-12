package com.prog3360.order_service.service;

import com.prog3360.order_service.client.ProductClient;
import com.prog3360.order_service.client.ProductResponse;
import com.prog3360.order_service.entity.Order;
import com.prog3360.order_service.repository.OrderRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderService {

    private final OrderRepository repository;
    private final ProductClient productClient;

    public OrderService(OrderRepository repository, ProductClient productClient) {
        this.repository = repository;
        this.productClient = productClient;
    }

    public List<Order> getAllOrders() {
        return repository.findAll();
    }

    public Order getOrderById(Long id) {
        return repository.findById(id).orElse(null);
    }

    public Order createOrder(Order order) {

        ProductResponse product = productClient.getProductById(order.getProductId());

        if (product == null) {
            order.setStatus("FAILED - Product Not Found");
            return repository.save(order);
        }

        if (product.getQuantity() < order.getQuantity()) {
            order.setStatus("FAILED - Insufficient Stock");
            return repository.save(order);
        }

        order.setTotalPrice(product.getPrice() * order.getQuantity());
        order.setStatus("CREATED");

        return repository.save(order);
    }
}
