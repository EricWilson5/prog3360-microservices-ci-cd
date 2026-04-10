package com.prog3360.product_service.service;

import com.prog3360.product_service.entity.Product;
import com.prog3360.product_service.repository.ProductRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {

    // Declare the logger for this class
    private static final Logger log = LoggerFactory.getLogger(ProductService.class);

    private final ProductRepository repository;

    public ProductService(ProductRepository repository) {
        this.repository = repository;
    }

    public List<Product> getAllProducts() {
        List<Product> products = repository.findAll();
        log.info("Retrieved all products. Total count: {}", products.size());
        return products;
    }

    public Product getProductById(Long id) {
        return repository.findById(id).map(product -> {
            // INFO: Successfully retrieving a product [cite: 23, 35]
            log.info("Successfully retrieved product: {} (ID: {}) with price: ${}",
                    product.getName(), id, product.getPrice());
            return product;
        }).orElseGet(() -> {
            // WARN: A product lookup returning no results [cite: 24, 35, 39]
            log.warn("Product lookup failed. No product found for ID: {}", id);
            return null;
        });
    }

    public Product createProduct(Product product) {
        Product savedProduct = repository.save(product);
        log.info("New product created: {} with ID: {}", savedProduct.getName(), savedProduct.getId());
        return savedProduct;
    }

    public void deleteProduct(Long id) {
        log.info("Deleting product with ID: {}", id);
        repository.deleteById(id);
    }
}