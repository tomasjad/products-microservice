package com.tomasjadzevicius.ch.demo.product.domain;

import com.tomasjadzevicius.ch.demo.product.domain.exceptions.ProductNotFoundException;
import com.tomasjadzevicius.ch.demo.product.domain.model.Product;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class GetProductInteractor {

    private final ProductRepository repository;

    public Product getProduct(String productId) {
        return repository.findActiveById(productId).orElseThrow(() -> new ProductNotFoundException(productId));
    }
}
