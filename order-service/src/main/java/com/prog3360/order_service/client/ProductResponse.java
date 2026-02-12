package com.prog3360.order_service.client;

import lombok.Data;

@Data
public class ProductResponse {

    private Long id;
    private String name;
    private double price;
    private int quantity;
}
