package com.tomasjadzevicius.ch.demo.product.domain;

import com.tomasjadzevicius.ch.demo.product.domain.exceptions.ProductNotFoundException;
import com.tomasjadzevicius.ch.demo.product.domain.model.Product;
import com.tomasjadzevicius.ch.demo.product.domain.model.UpdateProductCommand;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class UpdateProductInteractor {

    private final ProductRepository repository;

    public Product updateProduct(String productId, UpdateProductCommand command) {
        command.validate();
        Product product = repository.findActiveById(productId).orElseThrow(() -> new ProductNotFoundException(productId));
        product.update(command);
        repository.save(product);

        return product;
    }
}
