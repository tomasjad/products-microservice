package com.tomasjadzevicius.ch.demo.product.domain.exceptions;

public class ProductNotFoundException extends RuntimeException {

    public ProductNotFoundException(String productId) {
        super(String.format("Product %s not found", productId));
    }
}
