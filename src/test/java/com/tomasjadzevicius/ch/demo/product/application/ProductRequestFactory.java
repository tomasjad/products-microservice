package com.tomasjadzevicius.ch.demo.product.application;

import com.tomasjadzevicius.ch.demo.product.model.ProductRequest;

import java.math.BigDecimal;

class ProductRequestFactory {

    static ProductRequest emptyProductRequest() {
        return new ProductRequest();
    }

    static ProductRequest productRequestA() {
        ProductRequest request = new ProductRequest();
        request.setName("productA");
        request.setPrice(BigDecimal.ONE);
        return request;
    }

    static ProductRequest productRequestB() {
        ProductRequest request = new ProductRequest();
        request.setName("productB");
        request.setPrice(BigDecimal.TEN);
        return request;
    }
}
