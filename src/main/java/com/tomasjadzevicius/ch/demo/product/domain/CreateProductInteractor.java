package com.tomasjadzevicius.ch.demo.product.domain;

import com.tomasjadzevicius.ch.demo.product.domain.model.CreateProductCommand;
import com.tomasjadzevicius.ch.demo.product.domain.model.Product;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.Clock;

@Component
@AllArgsConstructor
public class CreateProductInteractor {

    private final ProductRepository repository;
    private final Clock clock;

    public Product createProduct(CreateProductCommand command) {
        command.validate();
        Product product = Product.newProduct(command, clock);
        repository.save(product);

        return product;
    }

}
