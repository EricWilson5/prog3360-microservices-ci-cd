package com.prog3360.order_service.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "product-service", url = "http://product-service:8081", configuration = FeignConfig.class)
public interface ProductClient {

    @GetMapping("/api/products/{id}")
    ProductResponse getProductById(@PathVariable("id") Long id);
}
