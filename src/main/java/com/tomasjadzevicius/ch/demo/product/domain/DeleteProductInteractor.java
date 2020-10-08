package com.tomasjadzevicius.ch.demo.product.domain;

import com.tomasjadzevicius.ch.demo.product.domain.exceptions.ProductNotFoundException;
import com.tomasjadzevicius.ch.demo.product.domain.model.Product;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class DeleteProductInteractor {

    private final ProductRepository repository;

    public void deleteProduct(String productId) {
        Product product = repository.findActiveById(productId).orElseThrow(() -> new ProductNotFoundException(productId));
        product.deactivate();
        repository.save(product);
    }

}
